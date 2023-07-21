package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.batch.service.impl.AbstractArchiveBatchServiceImpl;
import com.dr.archive.enums.CategoryType;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverDetail;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverDetailInfo;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLib;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLibInfo;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverDetailService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverLibService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 到期移交库service实现类
 *
 * @author dr
 */
@Service
public class ArchiveBatchHandOverLibServiceImpl extends AbstractArchiveBatchServiceImpl<ArchiveBatchHandOverLib> implements ArchiveBatchHandOverLibService {
    private final AtomicBoolean runningStatus = new AtomicBoolean(false);
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    FondService fondService;
    @Autowired
    ArchiveBatchHandOverDetailService detailService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    CategoryConfigService categoryConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateStatus(String ids, String status) {
        Assert.isTrue(StringUtils.hasText(ids), "待移交库id不能为空！");
        String[] idArr = ids.split(",");

        SqlQuery<ArchiveBatchHandOverLib> sqlQuery = SqlQuery.from(ArchiveBatchHandOverLib.class)
                .set(ArchiveBatchHandOverLibInfo.STATUS, status)
                .in(ArchiveBatchHandOverLibInfo.ID, idArr);

        Person person = SecurityHolder.get().currentPerson();
        if (person != null) {
            sqlQuery.set(ArchiveBatchHandOverLibInfo.UPDATEDATE, System.currentTimeMillis())
                    .set(ArchiveBatchHandOverLibInfo.UPDATEPERSON, person.getId());
        }
        return commonMapper.updateByQuery(sqlQuery);
    }

    @Override
    public String computeSumInfo(String id) {
        ArchiveBatchHandOverLib lib = selectById(id);
        StringBuilder sb = new StringBuilder(lib.getFondName())
                .append("【")
                .append(lib.getArchiveYear())
                .append("】");
        SqlQuery<ArchiveBatchHandOverDetail> detailSqlQuery = SqlQuery.from(ArchiveBatchHandOverDetail.class)
                .equal(ArchiveBatchHandOverDetailInfo.BATCHID, id)
                .orderBy(ArchiveBatchHandOverDetailInfo.ARCHIVECODE);
        //查询第一个档号的详情数据
        ArchiveBatchHandOverDetail start = commonMapper.selectLimitByQuery(detailSqlQuery, 0, 1).get(0);
        sb.append("(").append(start.getArchiveCode()).append(")");
        if (lib.getDetailNum() > 1) {
            ArchiveBatchHandOverDetail end = commonMapper.selectLimitByQuery(detailSqlQuery, (int) (lib.getDetailNum() - 1), (int) lib.getDetailNum()).get(0);
            sb.append("至(").append(end.getArchiveCode()).append(")");
        }
        return sb.append("共(")
                .append(lib.getDetailNum())
                .append(")件。")
                .toString();
    }

    @Async
    @Override
    public void start(SecurityHolder securityHolder) {
        SecurityHolder.set(securityHolder);
        if (!runningStatus.get()) {
            //更新运行中的状态
            runningStatus.compareAndSet(false, true);
            try {
                doStart();
            } catch (Exception e) {
                runningStatus.compareAndSet(true, false);
            }
        }
    }

    /**
     * TODO 拆分多线程
     * 真正执行到期移交自动检测
     */
    private void doStart() {
        FormDefinitionQuery query = new FormDefinitionQuery();
        query.typeEqual(String.valueOf(CategoryType.ARC.getCode()));

        FormDefinitionQuery query1 = new FormDefinitionQuery();
        query1.typeEqual(String.valueOf(CategoryType.FILE.getCode()));

        //遍历所有案卷和文件的数据
        List<? extends FormModel> formModelsAJ = formDefinitionService.selectFormDefinitionByQuery(query);
        List<? extends FormModel> formModelsWJ = formDefinitionService.selectFormDefinitionByQuery(query1);
        for (FormModel aj : formModelsAJ) {
            doSelectForm(aj);
        }
        for (FormModel wj : formModelsWJ) {
            doSelectForm(wj);
        }
        //更新程序运行状态
        runningStatus.compareAndSet(true, false);
    }

    private void doSelectForm(FormModel formModel) {
        Set<String> batchs = new HashSet<>();
        List<FormData> formDataList = new ArrayList<>();
        //如果类型是案卷，则查询名下的卷内文件表单
        if(String.valueOf(CategoryType.ARC.getCode()).equals(formModel.getFormType())){
            //查询指定表单所有档案数据(档案未锁定且未移交到档案馆)
            List<FormData> aj = getFormDataList(formModel.getId(), "1");
            if(aj.size() > 0){
                List<CategoryConfig> categoryConfigs = categoryConfigService.selectList(SqlQuery.from(CategoryConfig.class,false)
                        .column(CategoryConfigInfo.FILEFORMID.distinct())
                        .equal(CategoryConfigInfo.ARCFORMID, formModel.getId()));
                List<FormData> jnwj = new ArrayList<>();
                for (CategoryConfig config : categoryConfigs) {
                    FormModel formJN = formDefinitionService.selectFormDefinitionById(config.getFileFormId());
                    if("4".equals(formJN.getFormType())){
                        jnwj.addAll(getFormDataList(formJN.getId(), "4"));
                    }
                }
                formDataList.addAll(aj);
                formDataList.addAll(jnwj);
            }
        }else{
            //如果类型是文件
            formDataList = getFormDataList(formModel.getId(), "0");
        }
        if(formDataList.size() > 0){
            for (FormData formDatum : formDataList) {
                doCheck(formDatum, batchs);
            }
            //更新批次统计数据
            for (String batch : batchs) {
                long detailCount = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchHandOverDetail.class).equal(ArchiveBatchHandOverDetailInfo.BATCHID, batch));
                commonMapper.updateByQuery(
                        SqlQuery.from(ArchiveBatchHandOverLib.class)
                                .set(ArchiveBatchHandOverLibInfo.DETAILNUM, detailCount)
                                .set(ArchiveBatchHandOverLibInfo.STATUS, STATUS_WAITING) //更新状态为待移交中
                                .equal(ArchiveBatchHandOverLibInfo.ID, batch)
                );
            }
        }

    }

    /**
     * 查询指定表单所有档案数据(档案未锁定且未移交到档案馆)
     */
    public List<FormData> getFormDataList(String formId, String type){
        List<FormData> formData = archiveDataManager.findDataByBuilder(formId, (sqlQuery, formRelationWrapper) -> {
            Column lockColumn = formRelationWrapper.getColumn(ArchiveEntity.COLUMN_SUB_STATUS);
            Column statusColumn = formRelationWrapper.getColumn(ArchiveEntity.COLUMN_STATUS);
            Column orgId = formRelationWrapper.getColumn(ArchiveEntity.COLUMN_ORGANISEID);
            sqlQuery
                    .equal(lockColumn, ArchiveDataManager.SUBSTATUS_UNLOCKED)
                    .or()
                    .isNull(lockColumn)
                    .andNew().isNull(orgId)
                    .equal(statusColumn, archiveDataManager.STATUS_MANAGE);
        });
        for (FormData data : formData) {
            //只是单独使用note字段做个标记，无其他作用
            data.put(ArchiveEntity.COLUMN_NOTE, type);
        }
        return formData;
    }


    /**
     * 真正执行一条鉴定
     *
     * @param formData
     * @param batchs
     */
    private void doCheck(FormData formData, Set<String> batchs) {
        //先判断鉴定数据所属全宗机构年度
        String fondCode = formData.getString(ArchiveEntity.COLUMN_FOND_CODE);
        //档案年度
        String archiveYear = formData.getString(ArchiveEntity.COLUMN_YEAR);

        if (StringUtils.hasText(fondCode) && StringUtils.hasText(archiveYear)) {
            //全宗编码不为空才做检测
            //先查询详情表中是否有数据
            if (!detailService.exists(formData.getId())) {
                Fond fond = fondService.findFondByCode(fondCode);
                if (fond != null) {
                    //没有数据在创建批次
                    ArchiveBatchHandOverLib batch = selectOrCreateBatch(fond, archiveYear);
                    detailService.newDetail(batch, formData, fond, formData.getString(ArchiveEntity.COLUMN_NOTE));
                    //档案加锁
                    archiveDataManager.updateSubStatus(formData.getId(), ArchiveDataManager.SUBSTATUS_LOCKED, formData.getFormDefinitionId());
                    batchs.add(batch.getId());
                }
            }
        }
    }

    /**
     * 根据表单查询或者创建批次信息
     *
     * @param fond
     * @param archiveYear
     * @return
     */
    protected ArchiveBatchHandOverLib selectOrCreateBatch(Fond fond, String archiveYear) {
        //根据全宗编码查询全宗
        SqlQuery<ArchiveBatchHandOverLib> handOverSqlQuery = SqlQuery.from(ArchiveBatchHandOverLib.class)
                .equal(ArchiveBatchHandOverLibInfo.FONDID, fond.getId())
                .equal(ArchiveBatchHandOverLibInfo.ARCHIVEYEAR, archiveYear)
                .in(ArchiveBatchHandOverLibInfo.STATUS, STATUS_WAITING, STATUS_CHECK);
        synchronized (runningStatus) {
            ArchiveBatchHandOverLib handOver = selectOne(handOverSqlQuery);
            if (handOver == null) {
                handOver = newHandOver(fond, archiveYear);
            } else {
                //更新状态为在检测中
                if (STATUS_WAITING.equals(handOver.getStatus())) {
                    commonMapper.updateByQuery(
                            SqlQuery.from(ArchiveBatchHandOverLib.class)
                                    .set(ArchiveBatchHandOverLibInfo.STATUS, STATUS_CHECK)
                                    .equal(ArchiveBatchHandOverLibInfo.ID, handOver.getId())
                    );
                }
            }
            return handOver;
        }
    }

    private ArchiveBatchHandOverLib newHandOver(Fond fond, String archiveYear) {
        Organise organise = organisePersonService.getOrganise(new OrganiseQuery.Builder().idEqual(fond.getPartyId()).build());
        ArchiveBatchHandOverLib handOver = new ArchiveBatchHandOverLib();

        handOver.setFondId(fond.getId());
        handOver.setFondName(fond.getName());

        handOver.setArchiveYear(archiveYear);

        handOver.setOrganiseId(organise.getId());

        handOver.setBatchName(handOver.getFondName() + "【" + archiveYear + "】到期移交");

        handOver.setCreateDate(System.currentTimeMillis());
        handOver.setCreatePerson("admin");

        handOver.setStatus(STATUS_CHECK);

        insert(handOver);
        return handOver;
    }

}
