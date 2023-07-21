package com.dr.archive.receive.offline.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.EntityFilesInfo;
import com.dr.archive.kufang.entityfiles.service.EntityFilesService;
import com.dr.archive.kufang.entityfiles.service.ImpBatchService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.RegisterWarehousingDetailsService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.offline.entity.TempToFormalBatch;
import com.dr.archive.receive.offline.entity.TempToFormalBatchDetail;
import com.dr.archive.receive.offline.entity.TempToFormalBatchDetailInfo;
import com.dr.archive.receive.offline.service.TempToFormalBatchDetailService;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.jdbc.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 临时库到正式库前的四性检测批次详情
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TempToFormalBatchDetailServiceImpl extends DefaultBaseService<TempToFormalBatchDetail> implements TempToFormalBatchDetailService {
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    TestRecordService testRecordService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    EntityFilesService entityFilesService;
    @Autowired
    ImpBatchService impBatchService;
    @Autowired
    RegisterWarehousingDetailsService registerWarehousingDetailsService;
    @Autowired
    protected OrganisePersonService organisePersonService;

    final ExecutorService executorService = Executors.newFixedThreadPool(20);

    int count = 0;

    @Override
    public TempToFormalBatch createDetail2(ArchiveDataQuery query, AbstractArchiveBatch archiveBatch, String status, String archiveType) {
        final TempToFormalBatch[] batch = {(TempToFormalBatch) archiveBatch};
        List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));

        final String[] lastArchivalCode = {""};
        String batchTestStatus = "1";
        String impId = UUID.randomUUID().toString();
        CountDownLatch latch = new CountDownLatch(formDataList.size());
        for (FormData formData : formDataList) {
            //入库批次详情表
            TempToFormalBatchDetail detail = addTempDetail(formData, batch[0].getId());
            //四性检测
            TestRecord record = fourDetection(formData, batch[0].getId(), lastArchivalCode[0]);
            //更新四性检测信息
            detail.setTestStatus(record.getStatus());
            detail.setLastTestRecordId(record.getId());
            updateById(detail);
            if (TestRecordService.TEST_STATUS_FAIL.equals(record.getStatus())) {
                batchTestStatus = TestRecordService.TEST_STATUS_FAIL;
            } else if (TestRecordService.TEST_STATUS_SUCCESS.equals(record.getStatus())) {
                String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
                SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
                String finalBatchTestStatus = batchTestStatus;
                executorService.execute(() -> {
                    SecurityHolder.set(securityHolder);
                    //加线程创建批次详情
                    try {
                        batch[0] =getCount(query, (TempToFormalBatch) archiveBatch, status, archiveType, batch[0], formData,formDataList,impId, finalBatchTestStatus);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            lastArchivalCode[0] = detail.getArchiveCode();
        }
        return batch[0];
    }

    /**
     * 正式库退回临时库时删除对应案卷和卷内的实体档案
     * @param query
     */
    public void deleteEntityFiles(BatchCreateQuery query) {
        // 由于updateStatusByQuery方法在backArchives方法之前执行，status_info已经由RECEIVE变成了MANAGE，所以此时再根据MANAGE查已经查不出数据了，必须改成临时库的状态去查询
        if (query.getQueryItems().stream().anyMatch(q -> q.getKey().equals("status_info"))) {
            query.getQueryItems().stream()
                    .filter(q -> q.getKey().equals("status_info"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("档案状态信息未找到"))
                    .setValue("RECEIVE");
        }
        List<FormData> ajformDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
        for (FormData formData: ajformDataList) {
            commonMapper.deleteByQuery(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.DATAID,formData.getId()));
        }
        List<FormData> jnFormDataList = new ArrayList<>();
        for (FormData formData : ajformDataList) {
            List<FormData> jnFormData = archiveDataManager.getJNFormData(formData);
            if(!StringUtils.isEmpty(jnFormData)){
                jnFormDataList.addAll(archiveDataManager.getJNFormData(formData));
            }
        }
        for (FormData jnFormData: jnFormDataList) {
            commonMapper.deleteByQuery((SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.DATAID,jnFormData.getId())));
        }

    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    TempToFormalBatch getCount(ArchiveDataQuery query, TempToFormalBatch archiveBatch, String status, String archiveType, TempToFormalBatch batch, FormData formData,List<FormData> formDataList,String impId,String batchTestStatus) {
        count++;
        //四性检测成功后，再修改档案状态   进入正式库
        archiveDataManager.updateStatusByFormData(formData, status);
        //状态修改成功后，进入正式库
        if (status.equals(formData.get(ArchiveEntity.COLUMN_STATUS))) {

            FormModel formModel = formDefinitionService.selectFormDefinitionById(query.getFormDefinitionId());
            //添加到长期保存库
            eArchiveService.addEArchiveOne(formData, formModel.getFormType(), "");
            //Todo 四性检测通过，将移动。
            if (StringUtils.hasText(archiveBatch.getRegisterWarehousingID())) {//出入库详情信息
                registerWarehousingDetailsService.insertFormData(formData, archiveBatch.getRegisterWarehousingID(), formModel.getFormType());
            }
            //添加到实体档案
            if (StringUtils.hasText(archiveType)) {
                entityFilesService.addEntityByRuKu(archiveType, formData, formModel.getFormType(), "");
            }
            //修改卷内文件的状态并添加到长期保存库
            List<FormData> wjs = archiveDataManager.getJNFormData(formData);//Todo 卷内文件查询出来。
            if (wjs != null && wjs.size() > 0) {
                for (FormData wj : wjs) {
                    if (!wj.get(ArchiveEntity.STATUS_COLUMN_KEY).equals(ArchiveDataManager.STATUS_TRASH)){
                        archiveDataManager.updateStatusByFormData(wj, status);
                        eArchiveService.addEArchiveOne(wj, "4", formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
                        //TODO 将卷内文件保存进详情中。
                        if (StringUtils.hasText(archiveBatch.getRegisterWarehousingID())) {//出入库详情信息
                            registerWarehousingDetailsService.insertFormData(wj, archiveBatch.getRegisterWarehousingID(), "2");
                        }
                        if (StringUtils.hasText(archiveType)) {
                            entityFilesService.addEntityByRuKu(archiveType, wj, "4", formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
                        }
                    }
                }
            }
        }
        FormData data = formDataList.get(formDataList.size() - 1);
        if (data.get(ArchiveEntity.ID_COLUMN_NAME).equals(formData.get(ArchiveEntity.ID_COLUMN_NAME))){
            try {
                List<TempToFormalBatchDetail> tempToFormalBatchDetails = commonMapper.selectByQuery(SqlQuery.from(TempToFormalBatchDetail.class).equal(TempToFormalBatchDetailInfo.BATCHID, batch.getId()).equal(TempToFormalBatchDetailInfo.TESTSTATUS,StatusEntity.STATUS_ENABLE_STR));
                batch.setTestTrueNum(tempToFormalBatchDetails.size());
                //保存结果
                batch.setStatus(StatusEntity.STATUS_ENABLE_STR);
                batch.setEndDate(System.currentTimeMillis());
                batch.setDetailNum(formDataList.size());
                batch.setTestStatus(batchTestStatus);
                commonMapper.updateById(batch);
                //添加实体档案导入批次
                if (StringUtils.hasText(archiveType) && count > 0) {
                    impBatchService.addBatch(archiveType, String.valueOf(count), impId);
                }
                RegisterWarehousing registerWarehousing = commonMapper.selectById(RegisterWarehousing.class, batch.getRegisterWarehousingID());
                if (TestRecordService.TEST_STATUS_SUCCESS.equals(batch.getTestStatus())) {
                    registerWarehousing.setStateType("成功");
                } else {
                    registerWarehousing.setStateType("失败");
                }
                registerWarehousing.setQuantity((int) batch.getDetailNum());
                commonMapper.updateById(registerWarehousing);
                return batch;
            } catch (Exception e) {
                e.printStackTrace();
                batch.setStatus(StatusEntity.STATUS_UNKNOW_STR);
                batch.setEndDate(System.currentTimeMillis());
                commonMapper.updateById(batch);
                return batch;
            }
        }
        return batch;
    }

    public List<FormData> getWJS(FormData formData) {
        Fond fond = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
        if ("1".equals(category.getArchiveType())) {//案卷类型
            CategoryConfig categoryConfig = commonMapper.selectOneByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, category.getId()));
            ArchiveDataQuery dataQuery = new ArchiveDataQuery();
            List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
            items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_AJDH, formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE), ArchiveDataQuery.QueryType.EQUAL));
            dataQuery.setFormDefinitionId(categoryConfig.getFileFormId());
            dataQuery.setQueryItems(items);
            List<FormData> wjs = formDataService.selectFormData(dataQuery.getFormDefinitionId(), newBuilder(dataQuery));
            return wjs;
        }
        return null;
    }

    public TempToFormalBatchDetail addTempDetail(FormData formData, String batchId) {
        TempToFormalBatchDetail detail = new TempToFormalBatchDetail();
        detail.setBatchId(batchId);
        detail.setFormDataId(formData.getId());
        detail.setFormDefinitionId(formData.getFormDefinitionId());
        detail.setFondCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        detail.setFondName(formData.get(ArchiveEntity.COLUMN_FOND_NAME));
        detail.setCategoryCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE));
        detail.setCategoryName(formData.get(ArchiveEntity.COLUMN_CATEGORY_NAME));
        detail.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        detail.setTitle(formData.get(ArchiveEntity.COLUMN_TITLE));
        detail.setKeyWords(formData.get(ArchiveEntity.COLUMN_KEY_WORDS));
        detail.setNote(formData.get(ArchiveEntity.COLUMN_NOTE));
        detail.setYear(formData.get(ArchiveEntity.COLUMN_YEAR));
        insert(detail);
        return detail;
    }

    /**
     * 进入正式库前的四性检测
     */
    public TestRecord fourDetection(FormData formData, String batchId, String lastArchivalCode) {
        //四性检测需要的参数
        FormDefinition formDefinition = (FormDefinition) formDefinitionService.selectFormDefinitionById(formData.getFormDefinitionId());
        Fond fond = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
        Map<String, Object> attr = new HashMap<>();
        attr.put(TestRecordService.CONTEXT_BUSINESS_ID_KEY, batchId);
        attr.put("lastArchivalCode", lastArchivalCode);
        attr.put("gdType", "Offline");//判断接收方式是离线还是在线
        attr.put("formCode", formDefinition.getFormCode());
        TestRecord record = testRecordService.testData(formData, formDefinition, null, fond, category, LinkType.ARCHIVING, attr);
        return record;
    }

    private SqlBuilder newBuilder(ArchiveDataQuery query) {
        return ((sqlQuery, wrapper) -> {
            for (ArchiveDataQuery.QueryItem item : query.getQueryItems()) {
                Column column = wrapper.getColumn(item.getKey());
                if (column == null) {
                    continue;
                }
                switch (item.getType()) {
                    case IN:
                        String[] data = item.getValue().split(",");
                        sqlQuery.in(column, data);
                        break;
                    case LIKE:
                        sqlQuery.like(column, item.getValue());
                        break;
                    case EQUAL:
                        sqlQuery.equal(column, item.getValue());
                        break;
                    case END_WITH:
                        sqlQuery.endingWith(column, item.getValue());
                        break;
                    case START_WITH:
                        sqlQuery.startingWith(column, item.getValue());
                        break;
                    default:
                        break;
                }
            }
            if ("ascending".equals(query.getOrderType())) {
                sqlQuery.orderBy(wrapper.getColumn(query.getOrderKey()));
            } else if ("dscending".equals(query.getOrderType())) {
                sqlQuery.orderByDesc(wrapper.getColumn(query.getOrderKey()));
            } else {
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            }
        });
    }

}
