package com.dr.archive.receive.online.service.impl;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchServiceImpl;
import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.entity.TestRecordInfo;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fournaturescheck.entity.FourNatureRecord;
import com.dr.archive.fournaturescheck.service.FourNatureRecordService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.online.bo.ArchiveReceiveBo;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnlineInfo;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManage;
import com.dr.archive.receive.online.service.*;
import com.dr.archive.utilization.search.service.EsDataService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.file.FileResource;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.common.file.resource.MultipartFileResource;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.form.engine.model.display.FormDisplay;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import static com.dr.archive.manage.form.service.ArchiveDataManager.STATUS_RECEIVE;

/**
 * 档案在线接收批次service实现类
 *
 * @author dr
 */
@Service
public class ArchiveOnlineReceiveServiceImpl extends AbstractArchiveBatchServiceImpl<ArchiveBatchReceiveOnline> implements ArchiveOnlineReceiveService, ProcessTypeProvider {

    static final Logger logger = LoggerFactory.getLogger(ArchiveOnlineReceiveService.class);
    @Autowired
    OnlineReceiveContextBuilder onlineReceiveContextBuilder;
    @Autowired
    List<OnLineReceiveProvider> receiveProviders;
    /**
     * TODO
     * 这里线程池可能会太大，把内存撑爆，
     * 合理的解决思路是先把数据存到数据库中，内存中只放少量的数据，搞一个数据队列
     */
    @Autowired
    ThreadPoolTaskExecutor executor;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    TestRecordService testRecordService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    ArchiveBatchDetailReceiveOnlineService detailReceiveOnlineService;
    @Autowired
    ArchiveBatchDetailReceiveOnlineService archiveBatchDetailReceiveOnlineService;
    @Autowired
    protected CommonMapper commonMapper;
    @Autowired
    EsDataService esDataService;
    @Autowired
    ArchiveFormDefinitionService archiveFormService;
    @Autowired
    FourNatureRecordService fourNatureRecordService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;
    @Override
    protected String buildBatchName(ArchiveBatchReceiveOnline instance, Person person, BatchCreateQuery query) {
        return null;
    }

    /**
     * 同步在线接收数据
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object receiveOnline(HttpServletRequest request, HttpServletResponse response, ArchiveReceiveBo receiveBo) {
        //构造上下文参数
        OnlineReceiveBatchContext context = onlineReceiveContextBuilder.buildReceiveContext(request, response, receiveBo);
        //构造批次数据
        context.setBatchReceiveOnline(newBatch(context));

        //查找指定的接收实现类
        for (OnLineReceiveProvider receiveProvider : receiveProviders) {
            if (receiveProvider.canHandle(context)) {
                Object result = receiveProvider.syncReceiveResult(context);
                saveBatch(context);
                //启动异步接收数据线程
                executor.execute(() -> receiveAsync(receiveProvider, context));
                return result;
            }
        }
        throw new RuntimeException("不支持该类型在线接收");
    }

    private ArchiveBatchReceiveOnline newBatch(OnlineReceiveBatchContext context) {

        ArchiveBatchReceiveOnline online = new ArchiveBatchReceiveOnline();

        online.setId(UUID.randomUUID().toString());
        online.setSystemNum(context.getSysManage().getSysCode());
        online.setSystemName(context.getSysManage().getSysName());

        return online;
    }

    @Override
    public File buildBatchFile(ArchiveBatchReceiveOnline batchReceiveOnline) {
        String dir = commonFileConfig.getFullDirPath(RECEIVE_DIR, batchReceiveOnline.getSystemNum(), null);
        return new File(dir, batchReceiveOnline.getFileName());
    }

    @Override
    public File buildDetailDir(ArchiveBatchDetailReceiveOnline detailReceiveOnline) {
        return new File(commonFileConfig.getFullDirPath(RECEIVE_DIR, detailReceiveOnline.getBatchId(), null));
    }

    @Override
    public Map<String, Long> getReport(String batchId) {
        Map<String, Long> integerMap = new HashMap<>();
        //接收目录数据成功数量
        long metadataSucessNum = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, batchId)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.STATUS, "3"));
        //接收目录数据失败数量
        long metadataFalseNum = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, batchId)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.STATUS, "4"));
        //四性检测通过数量
        long fourSexTestSucessNum = commonMapper.countByQuery(SqlQuery.from(TestRecord.class)
                .equal(TestRecordInfo.BUSINESSID, batchId)
                .equal(TestRecordInfo.STATUS, "1"));
        //四性检测未通过数量
        long fourSexTestFalseNum = commonMapper.countByQuery(SqlQuery.from(TestRecord.class)
                .equal(TestRecordInfo.BUSINESSID, batchId)
                .equal(TestRecordInfo.STATUS, "0"));
        integerMap.put("metadataSucessNum", metadataSucessNum);
        integerMap.put("metadataFalseNum", metadataFalseNum);
        integerMap.put("fourSexTestSucessNum", fourSexTestSucessNum);
        integerMap.put("fourSexTestFalseNum", fourSexTestFalseNum);
        return integerMap;
    }

    /**
     * 保存在线接收批次数据
     *
     * @param context
     */
    private void saveBatch(OnlineReceiveBatchContext context) {
        ArchiveBatchReceiveOnline online = context.getBatchReceiveOnline();
        ArchiveReceiveOnlineSysManage sysManage = context.getSysManage();
        //关联数据系统基本信息数据
        online.setSystemName(sysManage.getSysName());
        online.setSystemNum(sysManage.getSysCode());
        //更新数据状态
        online.setStatus(StatusEntity.STATUS_DISABLE_STR);
        //保存在线接收批次数据
        insert(online);
    }

    /**
     * 执行异步接收任务
     *
     * @param receiveProvider
     * @param context
     */
    private void receiveAsync(OnLineReceiveProvider receiveProvider, OnlineReceiveBatchContext context) {
        TransactionStatus status = null;
        String batchTestStatus = TestRecordService.TEST_STATUS_SUCCESS;
        try {
            //手动管理事务
            status = transactionManager.getTransaction(TransactionDefinition.withDefaults());
            //上一次的档号/电子文件号
            String lastArchivalCode = "";
            //先保存接收批次信息
            //循环保存详情和文件信息
            while (receiveProvider.hasNext(context)) {
                try {
                    OnlineReceiveBatchContext.ReceiveDataContext dataContext = receiveProvider.receiveNext(context);
                    //保存表单数据
                    saveArchiveDataAndFiles(context, dataContext);
                    //判断是否需要执行四性检测
                    if (dataContext.isNeedTest()) {
                        //执行四性检测
                        Map<String, Object> attr = new HashMap<>();
                        attr.putAll(dataContext.getSessionMap());
                        attr.putAll(context.getSessionMap());
                        attr.put(TestRecordService.CONTEXT_BUSINESS_ID_KEY, context.getBatchReceiveOnline().getId());
                        attr.put("lastArchivalCode", lastArchivalCode);
                        attr.put("gdType", "Online");//判断接收方式是离线还是在线
                        TestRecord record = testRecordService.testData(
                                dataContext.getFormData(),
                                dataContext.getFormDefinition(),
                                null,
                                dataContext.getFond(),
                                dataContext.getCategory(),
                                LinkType.ARCHIVING,
                                attr
                        );
                        //更新明细中四性检测信息
                        ArchiveBatchDetailReceiveOnline detailReceiveOnline = detailReceiveOnlineService.selectById(dataContext.getDetailReceiveOnline().getId());
                        detailReceiveOnline.setTestStatus(record.getStatus());
                        detailReceiveOnline.setSuccessCount(record.getSuccessCount());
                        detailReceiveOnline.setTotalCount(record.getTotalCount());
                        detailReceiveOnline.setLastTestRecordId(record.getId());
                        detailReceiveOnlineService.updateById(detailReceiveOnline);
                        if (TestRecordService.TEST_STATUS_FAIL.equals(record.getStatus())) {
                            batchTestStatus = TestRecordService.TEST_STATUS_FAIL;
                        }
                    }
                    lastArchivalCode = dataContext.getDetailReceiveOnline().getArchiveCode();
                } catch (Exception e) {
                    logger.error("执行在线接收数据失败", e);
                }
            }
            //更改批次状态为结束
            ArchiveBatchReceiveOnline online = context.getBatchReceiveOnline();
            online.setStatus("1");
            online.setEndDate(System.currentTimeMillis());
            online.setTestStatus(batchTestStatus);
            updateById(online);
            transactionManager.commit(status);
        } catch (Exception e) {
            if (status != null) {
                transactionManager.rollback(status);
            }
            //更改批次状态为失败
            ArchiveBatchReceiveOnline online = context.getBatchReceiveOnline();
            online.setStatus("2");
            online.setEndDate(System.currentTimeMillis());
            updateById(online);

            logger.error("执行在线接收批次异常", e);
        }
    }

    /**
     * 保存单次接收的档案数据和附件数据
     *
     * @param context
     * @param dataContext
     */
    private void saveArchiveDataAndFiles(OnlineReceiveBatchContext context, OnlineReceiveBatchContext.ReceiveDataContext dataContext) throws IOException {
        //保存表单数据
        FormData formData = archiveDataManager.insertFormData(dataContext.getFormData(), dataContext.getFond().getId(), dataContext.getCategory().getId());
        //表单放到暂存库
        archiveDataManager.updateStatus(formData.getId(), STATUS_RECEIVE, formData.getFormDefinitionId());
        //保存文件数据
        for (FileResource fileInfo : dataContext.getFileInfos()) {
            commonFileService.addFile(fileInfo, dataContext.getFormData().getId(), "archive");
        }
        //保存接收详情数据 TODO
        ArchiveBatchDetailReceiveOnline detailReceiveOnline = dataContext.getDetailReceiveOnline();

        detailReceiveOnline.bindAll(formData, dataContext.getFond(), dataContext.getCategory());
        detailReceiveOnline.setTasktype(formData.get(ArchiveEntity.COLUMN_TASK_TYPE));
        detailReceiveOnline.setTaskmname(formData.get(ArchiveEntity.COLUMN_TASK_NAME));
        detailReceiveOnline.setStatus("3");
        detailReceiveOnlineService.insert(detailReceiveOnline);
    }

    /**
     * 启动入库申请
     *
     * @param context
     */
    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String businessId = (String) context.getBusinessParams().get("businessId");
        Assert.isTrue(StringUtils.hasText(businessId), "在线接收记录不能为空！");
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, businessId);

        ArchiveBatchReceiveOnline online = selectById(businessId);
        Assert.isTrue(!"6".equalsIgnoreCase(online.getStatus()), "该批次已经入库，不得重复入库！");
        //入库审核中
        online.setStatus("5");
        updateById(online);

        Person person = context.getPerson();
        context.setProcessInstanceTitle(person.getUserName() + "提交的在线接收入库申请");
    }

    @Override
    public void onBeforeEndProcess(TaskContext context) {
        String businessId = (String) context.getTaskInstance().getProcessVariables().get(ProcessConstants.PROCESS_BUSINESS_KEY);
        Assert.isTrue(StringUtils.hasText(businessId), "离线接收入库记录不能为空！");
        ArchiveBatchReceiveOnline online = selectById(businessId);
        //入库审核中
        online.setStatus("6");
        updateById(online);
        detailReceiveOnlineService.filling(online);
        //TODO 应该在办结通过后入库，并且进入es，不应该在在线目录接收时进入，重建索引应该只处理管理库中的数据
        List<ArchiveBatchDetailReceiveOnline> detailList = detailReceiveOnlineService.selectByBatchId(businessId);
        detailList.forEach(archiveBatchDetailReceiveOnline -> {
            try {
                esDataService.updateContentData(archiveBatchDetailReceiveOnline.getFormDefinitionId(), archiveBatchDetailReceiveOnline.getFormDataId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public String getType() {
        return "online_filling";
    }

    @Override
    public String getName() {
        return "在线接收入库";
    }

    /**
     * 创建批次
     * @param query
     * @param person
     * @param organise
     */
    @Override
    public void doCreateDetail(BatchCreateQuery query, Person person, Organise organise){
        ArchiveBatchReceiveOnline archiveBatchReceiveOnline = new ArchiveBatchReceiveOnline();
        archiveBatchReceiveOnline.setBatchName(person.getUserName()+"发起的移交");
        archiveBatchReceiveOnline.setTransferUnit(organise.getOrganiseName());
        archiveBatchReceiveOnline.setTransferUnitPerson(person.getUserName());
        archiveBatchReceiveOnline.setFondCode(query.getFondCode());
        archiveBatchReceiveOnline.setStatus("0");
        archiveBatchReceiveOnline.setStartDate(System.currentTimeMillis());
        archiveBatchReceiveOnline.setFileName(query.getFileName());
        insert(archiveBatchReceiveOnline);
        //handover(query,archiveBatchReceiveOnline);
        SecurityHolder securityHolder = SecurityHolder.get();
        executor.execute(() -> {
            SecurityHolder.set(securityHolder);
            //异步执行同步基本信息
            handover(query, archiveBatchReceiveOnline);
        });

    }
    //@Async
    @Transactional(rollbackFor = Exception.class)
    public void handover(BatchCreateQuery query,ArchiveBatchReceiveOnline archiveBatchReceiveOnline){
        //查询所有档案
        List<FormData> dataList = dataManager.findDataByQuery(query);
        archiveBatchReceiveOnline.setDetailNum(dataList.size());
        archiveBatchReceiveOnline.setFormDefinitionId(dataList.get(0).getFormDefinitionId());
        //上一次的档号/电子文件号
        String lastArchivalCode = "";
        Fond fond = fondService.selectById(query.getFondId());
        Category category = categoryService.selectById(query.getCategoryId());
        for (FormData formData:dataList) {
            ArchiveBatchDetailReceiveOnline archiveBatchDetailReceiveOnline = new ArchiveBatchDetailReceiveOnline();
            archiveBatchDetailReceiveOnline.setTitle(formData.get(ArchiveEntity.COLUMN_TITLE));
            archiveBatchDetailReceiveOnline.setBatchId(archiveBatchReceiveOnline.getId());
            archiveBatchDetailReceiveOnline.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            archiveBatchDetailReceiveOnline.setSaveTerm(formData.get(ArchiveEntity.COLUMN_SAVE_TERM));
            archiveBatchDetailReceiveOnline.setFondCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));//全总编码
            archiveBatchDetailReceiveOnline.setCategoryCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE));//分类编码
            archiveBatchDetailReceiveOnline.setOrgCode(formData.get(ArchiveEntity.COLUMN_ORG_CODE));//机构编码
            archiveBatchDetailReceiveOnline.setFormDataId(formData.getId());
            archiveBatchDetailReceiveOnline.setFormDefinitionId(query.getFormDefinitionId());
            archiveBatchDetailReceiveOnline.setStatus("2");
            archiveBatchDetailReceiveOnline.setTestStatus("0");
            archiveBatchDetailReceiveOnlineService.insert(archiveBatchDetailReceiveOnline);
            //四性检测所需参数
            FormDefinition formDefinition = (FormDefinition) formDefinitionService.selectFormDefinitionById(query.getFormDefinitionId());
            //Fond fond = fondService.findFondByCode(query.getFondCode());
            Map<String, Object> attr = new HashMap<>();
            attr.put(TestRecordService.CONTEXT_BUSINESS_ID_KEY, archiveBatchReceiveOnline.getId());
            attr.put("lastArchivalCode", lastArchivalCode);
            attr.put("gdType", "Offline");//判断接收方式是离线还是在线
            attr.put("formCode", formDefinition.getFormCode());
            //Category category = categoryService.findCategoryByCode(query.getCategoryCode(), fond.getId());
            TestRecord record =  fourNatureRecordService.startTest(formData,archiveBatchDetailReceiveOnline.getId(),"FILING");
            //TestRecord record = testRecordService.testData(formData, formDefinition, null, fond, category, LinkType.ARCHIVING, attr);
            archiveBatchDetailReceiveOnline.setTotalCount(record.getTotalCount());
            archiveBatchDetailReceiveOnline.setSuccessCount(record.getSuccessCount());
            if (record.getTotalCount()==record.getSuccessCount()){
                archiveBatchDetailReceiveOnline.setTestStatus("1");
                archiveBatchReceiveOnline.setTestStatus("1");
            }else{
                archiveBatchReceiveOnline.setTestStatus("0");
            }
            lastArchivalCode = archiveBatchDetailReceiveOnline.getArchiveCode();
            archiveBatchDetailReceiveOnlineService.updateById(archiveBatchDetailReceiveOnline);
        }
        archiveBatchReceiveOnline.setEndDate(System.currentTimeMillis());
        updateById(archiveBatchReceiveOnline);
        //archiveDataManager.insertFormData()
    }

    /**
     * 进行移交
     * @param batchId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArchives(String batchId,String categoryId,String fondId){
        ArchiveBatchReceiveOnline archiveBatchReceiveOnline = selectById(batchId);
        FormDefinition formDefinition =  (FormDefinition)formDefinitionService.selectFormDefinitionById(archiveBatchReceiveOnline.getFormDefinitionId());
        String formType = formDefinition.getFormType();
        CategoryConfig categoryConfig = commonMapper.selectOneByQuery(SqlQuery.from(CategoryConfig.class).equal(CategoryConfigInfo.BUSINESSID, categoryId).equal(CategoryConfigInfo.ISDEFAULT, "1"));
        if (archiveBatchReceiveOnline.getStatus().equals("1")){
            Assert.isTrue(false, "已入库，请勿重复入库！");
        }
        List<ArchiveBatchDetailReceiveOnline> archiveBatchDetailReceiveOnlines = archiveBatchDetailReceiveOnlineService.selectList(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, batchId));
        Category category = categoryService.selectById(categoryId);
        archiveBatchDetailReceiveOnlines.forEach(tiem->{
            FormData formData1 = archiveDataManager.selectOneFormData(tiem.getFormDefinitionId(), tiem.getFormDataId());
            //formData1.setId(UUID.randomUUID().toString());
            //保存表单数据
            //FormData formData = archiveDataManager.insertFormData(formData1, null, tiem.getCategoryId());
            //表单放到暂存库
            //archiveDataManager.updateStatus(formData.getId(), STATUS_RECEIVE, formData.getFormDefinitionId());
            List<FileInfo> fileInfos = commonFileService.list(formData1.getId(),"archive","default");
            formData1.setId(UUID.randomUUID().toString());
            formData1.put("CATE_GORY_CODE",category.getCode());
            if (formType.equals("0")){
                formData1.put("formDefinitionId",categoryConfig.getFileFormId());
            }else {
                formData1.put("formDefinitionId",categoryConfig.getArcFormId());
            }
            FormData formData = archiveDataManager.insertFormData(formData1, fondId,categoryId);
            archiveDataManager.updateStatus(formData.getId(), STATUS_RECEIVE, formData.getFormDefinitionId());
            //保存文件数据
            for (FileInfo fileInfo : fileInfos) {
                    commonFileService.addFile(fileInfo.getFileHash(),formData1.getId(), "archive");
            }
            tiem.setStatus("3");
            archiveBatchDetailReceiveOnlineService.updateById(tiem);
        });
        archiveBatchReceiveOnline.setStatus("1");
        updateById(archiveBatchReceiveOnline);
    }
}
