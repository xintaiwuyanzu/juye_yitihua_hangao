package com.dr.archive.receive.offline.service.impl;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchServiceImpl;
import com.dr.archive.common.dataClient.entity.DataBatch;
import com.dr.archive.common.dataClient.entity.DataBatchFileDetail;
import com.dr.archive.common.dataClient.entity.DataBatchFileDetailInfo;
import com.dr.archive.common.dataClient.service.DataBatchFileDetailService;
import com.dr.archive.common.dataClient.service.DataBatchService;
import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fournaturescheck.service.FourNatureRecordService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.offline.entity.*;
import com.dr.archive.receive.offline.service.ArchiveBatchOfflineDetailService;
import com.dr.archive.receive.offline.service.ArchiveBatchOfflineService;
import com.dr.archive.transfer.file.ZipFileResource;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.archive.utilization.search.service.EsDataService;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.*;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 离线接收详情
 *
 * @Author: caor
 * @Date: 2021-12-17 13:05
 * @Description:
 */
@Service
public class ArchiveBatchOfflineServiceImpl extends AbstractArchiveBatchServiceImpl<ArchiveBatchReceiveOffline> implements ArchiveBatchOfflineService, ProcessTypeProvider {
    /**
     * 上传批次相关
     */
    @Autowired
    DataBatchFileDetailService dataBatchFileDetailService;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Autowired
    DataBatchService dataBatchService;
    @Autowired
    ArchiveBatchOfflineDetailService archiveBatchOfflineDetailService;
    @Autowired
    TaskInstanceService taskInstanceService;
    @Autowired
    TestRecordService testRecordService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    EsDataService esDataService;
    @Autowired
    protected ArchiveDataManager dataManager;
    @Autowired
    ArchiveFormDefinitionService archiveFormService;

    @Value("${client.url}")
    private String clientUrl;

    @Autowired
    ArchiveBatchOfflineDetailService offlineDetailService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    FormDataService formDataService;
    /**
     * 用来执行异步操作
     */
    @Autowired
    protected Executor executor;
    final ExecutorService executorService = Executors.newFixedThreadPool(15);
    @Autowired
    protected OrganisePersonService organisePersonService;
    @Autowired
    CategoryConfigService categoryConfigService;
    /**
     * 创建批次信息
     *
     * @param person 当前登录人
     * @param query  批次查询条件
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArchiveBatchReceiveOffline newBatch(BatchCreateQuery query, Person person) {
        ArchiveBatchReceiveOffline batch = super.newBatch(query, person);
        //初始化数据
        batch.setStatus(StatusEntity.STATUS_DISABLE_STR);
        batch.setMineType(query.getMineType());
        batch.setFileLocation(query.getFileLocation());
        batch.setFileName(query.getFileName());
        batch.setImpType(query.getImpType());
        batch.setTypologic("CatalogImport");
        batch.setOrgId(SecurityHolder.get().currentOrganise().getId());
        batch.setHookStatus(StatusEntity.STATUS_DISABLE_STR);

        /**
         *保存批次数据
         */
        insert(batch);
        /**
         *创建详情信息
         * 导入方式为包结构的要解析xml
         */
        if ("2".equals(query.getImpType())) {
            archiveBatchOfflineDetailService.createDetail2(query, batch);
        } else {
            archiveBatchOfflineDetailService.createDetail(query, batch);
        }

        return batch;
    }

    @Override
    protected String buildBatchName(ArchiveBatchReceiveOffline instance, Person person, BatchCreateQuery query) {
        if (!StringUtils.isEmpty(query.getBatchName())) {
            return query.getBatchName();
        } else {
            return String.format("%s提交的%s", person.getUserName(), "离线接收");
        }
    }

    @Autowired
    FourNatureRecordService fourNatureRecordService;

    int hookTotal = 0;
    String batchTestStatus = TestRecordService.TEST_STATUS_SUCCESS;
    //上一次的档号/电子文件号
    String lastArchivalCode = "";
    int testCount = 0;
    int fileHookFalseNum = 0;
    @Override
    @Async
    public void hookByBatchId(String type, String fileLocations, String filePath, String impBatchId, String clientBatchId, boolean isDeleteFile, String coverOrAdd) {
        Assert.isTrue(StringUtils.hasText(type), "挂接类型不能为空!");
        Assert.isTrue(StringUtils.hasText(fileLocations), "来源不能为空!");
        Assert.isTrue(StringUtils.hasText(impBatchId), "离线接收的批次Id不能为空!");
        if ("CLIENT".equalsIgnoreCase(fileLocations)) {
            Assert.isTrue(StringUtils.hasText(clientBatchId), "客户端工具上传的批次Id不能为空!");
        } else if ("SERVER".equalsIgnoreCase(fileLocations)) {
            Assert.isTrue(StringUtils.hasText(filePath), "服务器地址不能为空!");
            Assert.isTrue(isExists(filePath), "路径不存在，请重新选择");
        }
        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
        if (HOOK_TYPE_FILE.equals(type)) {
            //更新批次记录 状态为开始，开始时间
            ArchiveBatchReceiveOffline archiveBatch = selectById(impBatchId);
            archiveBatch.setHookStartTime(System.currentTimeMillis());
            updateById(archiveBatch);
            hookTotal = 0;
            // 根据impBatchId，查找导入详情记录
            List<ArchiveBatchReceiveOfflineDetail> archiveBatchReceiveOfflineDetailList = archiveBatchOfflineDetailService.selectByBatchId(impBatchId);
             batchTestStatus = TestRecordService.TEST_STATUS_SUCCESS;
             //上一次的档号/电子文件号
             lastArchivalCode = "";
             testCount = 0;
             fileHookFalseNum = 0;
            List<DataBatchFileDetail> allpp = new ArrayList<>();
            List<DataBatchFileDetail> allFileList = dataBatchFileDetailService.selectList(SqlQuery.from(DataBatchFileDetail.class).equal(DataBatchFileDetailInfo.BATCHID, clientBatchId));
            CountDownLatch latch = new CountDownLatch(archiveBatchReceiveOfflineDetailList.size());
            for (ArchiveBatchReceiveOfflineDetail archiveBatchReceiveOfflineDetail : archiveBatchReceiveOfflineDetailList) {

                //加线程
                ArchiveBatchReceiveOffline finalArchiveBatch = archiveBatch;
                executorService.execute(()->{
                    SecurityHolder.set(securityHolder);
                    try {
                        extracted(fileLocations, filePath, impBatchId, clientBatchId, isDeleteFile, coverOrAdd, finalArchiveBatch, allpp, archiveBatchReceiveOfflineDetail,archiveBatchReceiveOfflineDetailList,allFileList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            try {
                //等待所有线程执行完毕
                latch.await();//主程序执行到await()函数会阻塞等待线程的执行，直到计数为0
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Async
    public void extracted(String fileLocations, String filePath, String impBatchId, String clientBatchId, boolean isDeleteFile, String coverOrAdd, ArchiveBatchReceiveOffline archiveBatch, List<DataBatchFileDetail> allpp, ArchiveBatchReceiveOfflineDetail archiveBatchReceiveOfflineDetail,List<ArchiveBatchReceiveOfflineDetail> archiveBatchReceiveOfflineDetailList,List<DataBatchFileDetail> allFileList) {
        //挂接详情开始时间
        archiveBatchReceiveOfflineDetail.setHookStartTime(System.currentTimeMillis());
        //追加的情况，之前成功的状态不再处理
        archiveBatchReceiveOfflineDetail.setHookStatus((HOOK_MODE_ADD.equals(coverOrAdd) && StatusEntity.STATUS_ENABLE_STR.equals(archiveBatchReceiveOfflineDetail.getHookStatus())) ? archiveBatchReceiveOfflineDetail.getHookStatus() : StatusEntity.STATUS_DISABLE_STR);
        FormData formData = archiveDataManager.selectOneFormData(archiveBatchReceiveOfflineDetail.getFormDefinitionId(), archiveBatchReceiveOfflineDetail.getFormDataId());

        if ("CLIENT".equalsIgnoreCase(fileLocations)) {
            // 根据批次id、目录档号规则，查找挂接详情中
            List<DataBatchFileDetail> dataBatchFileDetailList = dataBatchFileDetailService.selectList(
                    SqlQuery.from(DataBatchFileDetail.class).equal(DataBatchFileDetailInfo.BATCHID, clientBatchId).like(DataBatchFileDetailInfo.FILENAME, archiveBatchReceiveOfflineDetail.getArchiveCode()));
            removeFile(dataBatchFileDetailList.size(), coverOrAdd, archiveBatchReceiveOfflineDetail);
            allpp.addAll(dataBatchFileDetailList);
            //更新详情
            commonMapper.updateIgnoreNullById(archiveBatchReceiveOfflineDetail);
            for (DataBatchFileDetail dataBatchFileDetail : dataBatchFileDetailList) {
                //包结构 要解析zip包
                if ("2".equals(archiveBatch.getImpType())) {
                    hookByZip(archiveBatchReceiveOfflineDetail, new File(dataBatchFileDetailService.buildFilePath(dataBatchFileDetail)), formData);
                } else {
                    //执行挂接
                    doHook(archiveBatchReceiveOfflineDetail.getFormDataId(), dataBatchFileDetailService.buildFilePath(dataBatchFileDetail));
                    //TODO 更新es，将来改成plugin方式
                    //esDataService.updateContentData(impBatchDetail.getFormDefinitionId(), impBatchDetail.getFormDataId());
                    if (commonFileService.count(archiveBatchReceiveOfflineDetail.getFormDataId(), "archive") > 0) {
                        archiveDataManager.updateHaveYuanwen(archiveBatchReceiveOfflineDetail.getCategoryId(), archiveBatchReceiveOfflineDetail.getFormDataId(), archiveBatchReceiveOfflineDetail.getFormDefinitionId(), "1");
                    } else {
                        archiveDataManager.updateHaveYuanwen(archiveBatchReceiveOfflineDetail.getCategoryId(), archiveBatchReceiveOfflineDetail.getFormDataId(), archiveBatchReceiveOfflineDetail.getFormDefinitionId(), "0");
                    }
                    //挂接成功
                    archiveBatchReceiveOfflineDetail.setHookStatus(StatusEntity.STATUS_ENABLE_STR);
                }
            }
            //更新详情记录,挂接记录和导入目录使用统一详情记录
            archiveBatchReceiveOfflineDetail.setHookNum(String.valueOf(dataBatchFileDetailList.size()));

            // 删除整个客户端上传的批次原文
            if (isDeleteFile) {
                DataBatch dataBatch = dataBatchService.selectById(clientBatchId);
                //todo 删除上传批次相关信息
                dataBatchFileDetailService.deleteByBatch(dataBatch);
            }
        } else if ("SERVER".equalsIgnoreCase(fileLocations)) {
            //获取服务器位置下的所有文件列表
            File file = new File(filePath);
            List<File> fileList = new ArrayList<>();
            getFile(file, fileList, archiveBatchReceiveOfflineDetail.getArchiveCode());
            removeFile(fileList.size(), coverOrAdd, archiveBatchReceiveOfflineDetail);
            //是否有该目录原文
            ArchiveBatchReceiveOffline finalArchiveBatch = archiveBatch;
            fileList.forEach(file1 -> {
                //包结构 要解析zip包
                if ("2".equals(finalArchiveBatch.getImpType())) {
                    hookByZip(archiveBatchReceiveOfflineDetail, file1, formData);
                } else {
                    //进行挂接
                    FileSystemFileResource fileSystemFileResource = new FileSystemFileResource(file1);
                    try {
                        //附件表添加数据
                        commonFileService.addFileLast(fileSystemFileResource, formData.get(IdEntity.ID_COLUMN_NAME), "archive", "default");
                        //挂接成功
                        archiveBatchReceiveOfflineDetail.setHookStatus(StatusEntity.STATUS_ENABLE_STR);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 删除服务器位置下的原文
                if (isDeleteFile) {
                    file1.delete();
                }
            });
            archiveBatchReceiveOfflineDetail.setHookNum(String.valueOf(fileList.size()));

        }
        //挂接成功,做四性检测
        if (StatusEntity.STATUS_ENABLE_STR.equals(archiveBatchReceiveOfflineDetail.getHookStatus())) {
            testCount++;
            //四性检测需要的参数
            FormDefinition formDefinition = (FormDefinition) formDefinitionService.selectFormDefinitionById(archiveBatchReceiveOfflineDetail.getFormDefinitionId());
            Fond fond = fondService.findFondByCode(archiveBatchReceiveOfflineDetail.getFondCode());
            Category category = categoryService.findCategoryByCode(archiveBatchReceiveOfflineDetail.getCategoryCode(), fond.getId());
            Map<String, Object> attr = new HashMap<>();
            attr.put(TestRecordService.CONTEXT_BUSINESS_ID_KEY, impBatchId);
            attr.put("lastArchivalCode", lastArchivalCode);
            attr.put("gdType", "Offline");//判断接收方式是离线还是在线
            attr.put("formCode", formDefinition.getFormCode());
            TestRecord record = testRecordService.testData(formData, formDefinition, null, fond, category, LinkType.ARCHIVING, attr);
            //  FourNatureRecord fourNatureRecord =  fourNatureRecordService.startTest(formData);
            //更新挂接详情中四性检测信息
            archiveBatchReceiveOfflineDetail.setTestStatus(record.getStatus());
            archiveBatchReceiveOfflineDetail.setLastTestRecordId(record.getId());
            if (TestRecordService.TEST_STATUS_FAIL.equals(record.getStatus())) {
                batchTestStatus = TestRecordService.TEST_STATUS_FAIL;
            }
        }
        lastArchivalCode = archiveBatchReceiveOfflineDetail.getArchiveCode();


        //挂接详情结束时间
        archiveBatchReceiveOfflineDetail.setHookEndTime(System.currentTimeMillis());
        commonMapper.updateIgnoreNullById(archiveBatchReceiveOfflineDetail);
        hookTotal++;
        ArchiveBatchReceiveOfflineDetail archiveBatchReceiveOfflineDetail1 = archiveBatchReceiveOfflineDetailList.get(archiveBatchReceiveOfflineDetailList.size() - 1);
        if (archiveBatchReceiveOfflineDetail.getId().equals(archiveBatchReceiveOfflineDetail1.getId())){
            try {
                // todo 暂时睡眠1秒等待其他线程执行完
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //更新批次记录 状态为结束，结束时间
            archiveBatch = selectById(impBatchId);
            archiveBatch.setHookEndTime(System.currentTimeMillis());
            //已挂接
            archiveBatch.setHookStatus(StatusEntity.STATUS_ENABLE_STR);
            archiveBatch.setHookNum(String.valueOf(hookTotal));
            //查询挂接后详情
            long hookSuccessNum = archiveBatchOfflineDetailService.count(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class).equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, impBatchId).equal(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, StatusEntity.STATUS_ENABLE_STR));
            archiveBatch.setHookSuccessNum(hookSuccessNum);
            archiveBatch.setHookFalseNum(archiveBatchReceiveOfflineDetailList.size() - hookSuccessNum);
            if (testCount > 0) {
                archiveBatch.setTestStatus(batchTestStatus);
            }
            if ("CLIENT".equalsIgnoreCase(fileLocations)) {  //原文挂接失败数量
                fileHookFalseNum = allFileList.size() - allpp.size();
                archiveBatch.setFileHookFalseNum(fileHookFalseNum);
                if (fileHookFalseNum > 0) {
                    archiveBatch.setTestStatus(TestRecordService.TEST_STATUS_FAIL);
                }
            }
            List<ArchiveBatchReceiveOfflineDetail> archiveBatchReceiveOfflineDetails = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class).equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, archiveBatch.getId()).equal(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, StatusEntity.STATUS_ENABLE_STR));
            archiveBatch.setHookNum(String.valueOf(archiveBatchReceiveOfflineDetails.size()));
            updateById(archiveBatch);
        }
    }

    /*
     * zip包结构数据进行挂接
     * targetFile zip包*/
    public void hookByZip(ArchiveBatchReceiveOfflineDetail archiveBatchReceiveOfflineDetail, File targetFile, FormData formData) {
        //1,解析zip包中的基本信息元数据
        //FormData formData = dataManager.selectOneFormData(archiveBatchReceiveOfflineDetail.getFormDefinitionId(), archiveBatchReceiveOfflineDetail.getFormDataId());
        // 不再重新调智能归档系统的元数据了
        List<FormField> formFieldList = archiveFormService.findFieldList(archiveBatchReceiveOfflineDetail.getFormDefinitionId());
        try {
            Map<String, String> xmlMapData = newXPathParserXml(new ZipFileResource(targetFile.getPath(), "基本信息元数据.xml"), formFieldList);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Map.Entry<String, String> entry : xmlMapData.entrySet()) {
                //处理时间 转换为 long类型
                long aLong;
                try {
                    aLong = simpleDateFormat.parse(entry.getValue()).getTime();
                    formData.put(entry.getKey(), aLong);
                } catch (Exception e) {
                    formData.put(entry.getKey(), entry.getValue());
                }
            }
            archiveDataManager.updateFormData(formData, "", archiveBatchReceiveOfflineDetail.getCategoryId());
            //2,挂接原文
            ZipFile zipFile = new ZipFile(targetFile);
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    //附件表添加数据
                    commonFileService.addFileLast(new ZipFileResource(entry, targetFile.toString()), formData.get(IdEntity.ID_COLUMN_NAME), "archive", "default");
                }
            }
            archiveBatchReceiveOfflineDetail.setHookStatus(StatusEntity.STATUS_ENABLE_STR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> newXPathParserXml(ZipFileResource fis, List<FormField> metadata) {
        Map<String, String> map = new HashMap();
        try (InputStream ips = fis.getInputStream()) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(ips);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String root = "";

            //TODO  暂时没处理fileset下面的
            for (FormField formField : metadata) {

                String code = formField.getFieldAliasStr();
                if (!StringUtils.hasText(code)) {
                    code = formField.getFieldCode();
                }
                String titleXpath = "/base_info/" + code;
                NodeList nodeList = (NodeList) xPath.evaluate(titleXpath, document, XPathConstants.NODESET);
                if (nodeList.getLength() == 1) {
                    Node item = nodeList.item(0);
                    NodeList childNodes = item.getChildNodes();
                    if (childNodes.getLength() <= 1) {
                        String titleValue = (String) xPath.evaluate(titleXpath, document, XPathConstants.STRING);
                        map.put(formField.getFieldCode(), titleValue);
                    }
                }
            }
            ips.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /*
     * 重新进行四性检测*/
    @Override
    public long doTestOffline(String batchId) {
        //获取批次信息
        ArchiveBatchReceiveOffline batchReceiveOffline = selectById(batchId);
        //获取导入详情数据
        List<ArchiveBatchReceiveOfflineDetail> archiveBatchReceiveOfflineDetailList = archiveBatchOfflineDetailService.selectByBatchId(batchId);
        String batchTestStatus = TestRecordService.TEST_STATUS_SUCCESS;
        //上一次的档号/电子文件号
        String lastArchivalCode = "";
        for (ArchiveBatchReceiveOfflineDetail batchReceiveOfflineDetail : archiveBatchReceiveOfflineDetailList) {
            if (StatusEntity.STATUS_ENABLE_STR.equals(batchReceiveOfflineDetail.getHookStatus())) {
                //四性检测需要的参数
                FormDefinition formDefinition = (FormDefinition) formDefinitionService.selectFormDefinitionById(batchReceiveOfflineDetail.getFormDefinitionId());
                //此处报空指针的情况，删除了导入批次对应的表单数据。
                FormData formData = archiveDataManager.selectOneFormData(batchReceiveOfflineDetail.getFormDefinitionId(), batchReceiveOfflineDetail.getFormDataId());
                Fond fond = fondService.findFondByCode(batchReceiveOfflineDetail.getFondCode());
                Category category = categoryService.findCategoryByCode(batchReceiveOfflineDetail.getCategoryCode(), fond.getId());
                Map<String, Object> attr = new HashMap<>();
                attr.put(TestRecordService.CONTEXT_BUSINESS_ID_KEY, batchId);
                attr.put("lastArchivalCode", lastArchivalCode);
                attr.put("gdType", "Offline");//判断接收方式是离线还是在线
                attr.put("formCode", formDefinition.getFormCode());
                TestRecord record = testRecordService.testData(formData, formDefinition, null, fond, category, LinkType.ARCHIVING, attr);
                //更新挂接详情中四性检测信息
                batchReceiveOfflineDetail.setTestStatus(record.getStatus());
                batchReceiveOfflineDetail.setSuccessCount(record.getSuccessCount());
                batchReceiveOfflineDetail.setTotalCount(record.getTotalCount());
                batchReceiveOfflineDetail.setLastTestRecordId(record.getId());
                if (TestRecordService.TEST_STATUS_FAIL.equals(record.getStatus())) {
                    batchTestStatus = TestRecordService.TEST_STATUS_FAIL;
                }
                if (TestRecordService.TEST_STATUS_SUCCESS.equals(record.getStatus()) && batchTestStatus.equals(TestRecordService.TEST_STATUS_SUCCESS)) {
                    batchTestStatus = TestRecordService.TEST_STATUS_SUCCESS;
                }
                commonMapper.updateIgnoreNullById(batchReceiveOfflineDetail);
            } else {
                batchTestStatus = TestRecordService.TEST_STATUS_FAIL;
            }
        }
        batchReceiveOffline.setTestStatus(batchTestStatus);
        return updateById(batchReceiveOffline);
    }


    void removeFile(int num, String coverOrAdd, ArchiveBatchReceiveOfflineDetail archiveBatchReceiveOfflineDetail) {
        //如果查找到则挂接原文,并且是删除所有之前原文信息，则先删除原文，在进行挂接
        if (num > 0 && HOOK_MODE_COVER.equals(coverOrAdd)) {
            //默认挂接中
            archiveBatchReceiveOfflineDetail.setHookStatus(StatusEntity.STATUS_DISABLE_STR);
            //TODO 这种方式可能删不干净，refType可能还有其他类型,archive是原文相关的类型
            commonFileService.removeFileByRef(archiveBatchReceiveOfflineDetail.getFormDataId(), "archive");
        }
    }

    /**
     * 正真挂接原文
     *
     * @param formDataId
     * @param filePath
     */
    void doHook(String formDataId, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            FileSystemFileResource fileSystemFileResource = new FileSystemFileResource(file, "原文批量挂接");
            //附件表添加数据
            try {
                commonFileService.addFile(fileSystemFileResource, formDataId, "archive");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断路径是否存在
     *
     * @param path
     * @return
     */
    public boolean isExists(String path) {
        boolean isExist = true;
        File file = new File(path);
        if (!file.exists()) {
            isExist = false;
        }
        return isExist;
    }

    /**
     * 获取文件夹及其子文件夹下是否有某个文件（包含路径）
     */

    public void getFile(File file, List<File> fileList, String fileName) {
        if (file != null) {
            File[] f = file.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    getFile(f[i], fileList, fileName);
                }
            } else {
                if (file.getName().contains(fileName)) {
                    fileList.add(file);
                }
            }
        }
    }

    /*统计接收报告*/
    @Override
    public Map<String, Long> getReport(String batchId) {
        Map<String, Long> integerMap = new HashMap<>();
        long detailNum = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batchId));
        long hookSucessNum = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batchId)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, "1"));
        long fourSexTestSucessNum = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batchId)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.TESTSTATUS, "1"));
        integerMap.put("detailNum", detailNum);
        integerMap.put("hookSucessNum", hookSucessNum);
        integerMap.put("fourSexTestSucessNum", fourSexTestSucessNum);
        integerMap.put("hookFalseNum", detailNum - hookSucessNum);
        integerMap.put("fourSexTestFalseNum", detailNum - fourSexTestSucessNum);
        return integerMap;
    }


    /**
     * 启动入库申请
     *
     * @param context
     */
    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String businessId = (String) context.getBusinessParams().get("businessId");
        Assert.isTrue(StringUtils.hasText(businessId), "离线接收入库记录不能为空！");
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, businessId);

        ArchiveBatchReceiveOffline offline = selectById(businessId);
        Assert.isTrue(!"6".equalsIgnoreCase(offline.getStatus()), "该批次已经入库，不得重复入库！");
        //入库审核中
        offline.setStatus("5");
        updateById(offline);

        Person person = context.getPerson();
        context.setProcessInstanceTitle(person.getUserName() + "提交的离线接收入库申请");
    }

    @Override
    public void onBeforeEndProcess(TaskContext context) {
        String businessId = (String) context.getTaskInstance().getProcessVariables().get(ProcessConstants.PROCESS_BUSINESS_KEY);
        Assert.isTrue(StringUtils.hasText(businessId), "离线接收入库记录不能为空！");
        ArchiveBatchReceiveOffline offline = selectById(businessId);
        //TODO 应该在入库时，进入es，不应该在导入时进入，重建索引应该只处理管理库中的数据
        List<ArchiveBatchReceiveOfflineDetail> detailList = archiveBatchOfflineDetailService.selectByBatchId(businessId);
        detailList.forEach(archiveBatchReceiveOfflineDetail -> {
            try {
                esDataService.updateContentData(archiveBatchReceiveOfflineDetail.getFormDefinitionId(), archiveBatchReceiveOfflineDetail.getFormDataId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //入库审核中
        offline.setStatus("6");
        updateById(offline);
        archiveBatchOfflineDetailService.filling(offline);
    }

    @Override
    public String getType() {
        return "offline-filling";
    }

    @Override
    public String getName() {
        return "离线接收入库";
    }

    @Override
    public long getReceiveOfflineCountByFond() {
        //离线接收没有全宗信息 只能先用机构id 查当前机构的离线接收
        SqlQuery<ArchiveBatchReceiveOffline> sqlQuery = SqlQuery.from(ArchiveBatchReceiveOffline.class);
        sqlQuery.equal(ArchiveBatchReceiveOfflineInfo.ORGID, SecurityHolder.get().currentOrganise().getId());
        sqlQuery.orderByDesc(ArchiveBatchReceiveOfflineInfo.CREATEDATE);
        List<ArchiveBatchReceiveOffline> onlineList = this.selectList(sqlQuery);
        return onlineList.stream().mapToLong(ArchiveBatchReceiveOffline::getDetailNum).sum();
    }

    @Override
    public String getError(String batchId) {
        CodeLogSheet codeLogSheet = commonMapper.selectOneByQuery(SqlQuery.from(CodeLogSheet.class, false)
                .column(CodeLogSheetInfo.ERROR_REASON).equal(CodeLogSheetInfo.BATCHID, batchId));
        return codeLogSheet.getError_reason();
    }

    @Override
    public String getClientUrl() {
        return clientUrl;
    }
    @Override
    public void extracted(ArchiveBatchReceiveOffline entity) {
        List<ArchiveBatchReceiveOfflineDetail> detailList = offlineDetailService.selectList(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, entity.getId()));
        CountDownLatch latch = new CountDownLatch(Math.round(detailList.size()));
        //删除明细和档案目录
        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
        for (ArchiveBatchReceiveOfflineDetail detail : detailList) {
            executorService.execute(() -> {
                SecurityHolder.set(securityHolder);
                //异步执行删除
                extracted(detail,detailList,entity);
                latch.countDown();
            });
        }
    }
    @Async
    @Transactional
    public void extracted(ArchiveBatchReceiveOfflineDetail detail,List<ArchiveBatchReceiveOfflineDetail> detailList,ArchiveBatchReceiveOffline entity) {
        offlineDetailService.deleteById(detail.getId());
        //删除目录,原文
        List<FormData> formDataList = formDataService.selectFormData(detail.getFormDefinitionId(), (sqlQuery, formRelationWrapper) -> {
            sqlQuery.equal(formRelationWrapper.getColumn(ArchiveEntity.ID_COLUMN_NAME), detail.getFormDataId()).equal(formRelationWrapper.getColumn(ArchiveEntity.STATUS_COLUMN_KEY), "RECEIVE");
        });
        if (formDataList.size() > 0) {
            archiveDataManager.deleteFormData(detail.getCategoryId(), detail.getFormDefinitionId(), detail.getFormDataId());
        }
        ArchiveBatchReceiveOfflineDetail archiveBatchReceiveOfflineDetail = detailList.get(detailList.size() - 1);
        if (detail.getId().equals(archiveBatchReceiveOfflineDetail.getId())){
            deleteById(entity.getId());
        }
    }

    @Override
    public void deleteFormData(String categoryId, String formId, String id) {
        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
        executor.execute(() -> {
            SecurityHolder.set(securityHolder);
            //异步执行删除
            deleteForm(categoryId, formId, id);
        });
    }
    @Async
    @Transactional
    public void deleteForm(String categoryId, String formId, String id) {
        Category category = categoryService.selectById(categoryId);
        String[] split = id.split(",");
        if (category.getArchiveType().equals("1")) {
            // 查出选中删除的所有案卷
            List<FormData> formDataList = formDataService.selectFormData(formId, (sqlQuery, formRelationWrapper) -> {
                sqlQuery.in(formRelationWrapper.getColumn(ArchiveEntity.ID_COLUMN_NAME), Arrays.asList(split));
            });
            for (FormData formData : formDataList) {
                String archiveCode = formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE);
                // 通过门类ID查询门类配置
                CategoryConfig categoryConfig = categoryConfigService.selectOneByCategoryId(categoryId);
                // 拿到卷内表单ID  根据案卷档号 和 档号 查案卷对应的所有卷内
                List<FormData> jnformDataList = formDataService.selectFormData(categoryConfig.getFileFormId(), (sqlQuery, formRelationWrapper) -> {
                    sqlQuery.equal(formRelationWrapper.getColumn(ArchiveEntity.COLUMN_AJDH), archiveCode);
                });
                String ids = jnformDataList.stream().map(FormData::getId).collect(Collectors.joining(","));
                if (!StringUtils.isEmpty(ids)) {
                    dataManager.deleteFormData(categoryId, categoryConfig.getFileFormId(), ids);
                }
            }
        }

        dataManager.deleteFormData(categoryId,formId,id);
    }
}
