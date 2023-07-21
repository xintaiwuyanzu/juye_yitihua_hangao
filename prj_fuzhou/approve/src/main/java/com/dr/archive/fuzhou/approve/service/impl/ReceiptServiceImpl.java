package com.dr.archive.fuzhou.approve.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.entity.TestRecordInfo;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.enums.CategoryType;
import com.dr.archive.fuzhou.approve.ApproveCenterConfig;
import com.dr.archive.fuzhou.approve.bo.ApproveArchiveBaseInfo;
import com.dr.archive.fuzhou.approve.bo.ArchiveReceiveBo;
import com.dr.archive.fuzhou.approve.bo.ArchiveReceiveBo.ArchiveFileInfo;
import com.dr.archive.fuzhou.approve.bo.TransferInfo;
import com.dr.archive.fuzhou.approve.entity.ArchivePackResult;
import com.dr.archive.fuzhou.approve.exception.ReceiveException;
import com.dr.archive.fuzhou.approve.service.ApproveCenterClient;
import com.dr.archive.fuzhou.approve.service.ArchiveReceiptService;
import com.dr.archive.fuzhou.approve.service.ReceiptService;
import com.dr.archive.fuzhou.configManager.bo.FieldConfig;
import com.dr.archive.fuzhou.configManager.bo.FieldMetaData;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.ApproveItemConfig;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.MetaRule;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.SaveItemRule;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.query.CategoryFormQuery;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.service.ArchiveBatchDetailReceiveOnlineService;
import com.dr.archive.receive.online.service.ArchiveOnlineReceiveService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.file.FileSaveHandler;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;

import static com.dr.archive.fuzhou.configManager.bo.itemconfig.MetaRule.*;

/**
 * @author caor
 * @date 2021-08-17 17:06
 */
@Service
public class ReceiptServiceImpl implements ReceiptService, InitializingBean {

    static final Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

    static final String BASIC_INFO_XML_NAME = "基本信息元数据.xml";

    @Autowired
    ApproveCenterConfig centerConfig;
    @Autowired
    MappingJackson2XmlHttpMessageConverter converter;
    /**
     * 这个是用来解析和转换xml的
     */
    ObjectMapper objectMapper;


    @Autowired
    ArchiveOnlineReceiveService archiveOnlineReceiveService;
    @Autowired
    ArchiveBatchDetailReceiveOnlineService archiveBatchDetailReceiveOnlineService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    FileSaveHandler fileSaveHandler;
    @Autowired
    ConfigManagerClient configManagerClient;
    @Autowired
    TestRecordService testRecordService;
    @Autowired
    ArchiveReceiptService archiveReceiptService;
    @Autowired
    ApproveCenterClient approveCenterClient;
    @Autowired
    CategoryConfigService categoryConfigService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    FormDefinitionService formDefinitionService;

    @Autowired
    @Lazy
    Executor executor;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;

    /**
     * 创建批次信息
     *
     * @param receiveBo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveArchive(ArchiveReceiveBo receiveBo) {
        if (!StringUtils.hasText(receiveBo.getXmlPath())) {
            throw new ReceiveException("归档目录元数据路径不能为空！");
        }
//        SecurityHolder securityHolder = checkSecurityHolder(organisePersonService);
//        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService);
        //下载归档目录xml
        UrlFileResource xmlFileResource;
        if ("数字化加工工具".equalsIgnoreCase(receiveBo.getFiles().get(0).getSystemName())) {
            xmlFileResource = new UrlFileResource(receiveBo.getXmlPath());
        } else {
            xmlFileResource = downloadUrlFile(centerConfig.fullPath(receiveBo.getXmlPath()), receiveBo.getXmlPath(), "下载归档目录失败", "7", receiveBo);
        }
        //解析xml
        TransferInfo transferInfo = parseXml(xmlFileResource.getInputStream(), objectMapper, TransferInfo.class, "解析归档目录元数据失败", "8", receiveBo);
        if (transferInfo.getDirectories().size() != receiveBo.getFiles().size()) {
            throw new ReceiveException("参数中文件数量与xml归档目录文件数量不同");
        }
        //创建归档批次
        ArchiveBatchReceiveOnline archiveBatch = ReceiptUtil.newBatch(transferInfo, receiveBo);
        archiveOnlineReceiveService.insert(archiveBatch);
        UrlFileResource finalXmlFileResource = xmlFileResource;
        logger.warn("接收批次数据：{}", receiveBo.getXmlPath());
        executor.execute(() -> {
            SecurityHolder.set(securityHolder);
            createBatchDetail(archiveBatch, finalXmlFileResource, transferInfo, receiveBo);
        });
    }

    /**
     * 异步执行
     *
     * @param archiveBatch
     * @param xmlFileResource
     * @param transferInfo
     * @param receiveBo
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void createBatchDetail(AbstractArchiveBatch archiveBatch, UrlFileResource xmlFileResource, TransferInfo transferInfo, ArchiveReceiveBo receiveBo) {
        //将附件绑定到档案批次中
        try {
            commonFileService.addFile(xmlFileResource, archiveBatch.getId(), "PRE_ARCHIVE", "PRE_ARCHIVE");
        } catch (IOException e) {
            logger.error("绑定归档目录xml失败", e);
        }
        //创建归档详情
        List<ArchiveFileInfo> archiveFileInfoList = receiveBo.getFiles();
        archiveFileInfoList.forEach(archiveFileInfo -> {
            //归档详情赋值
            ArchiveBatchDetailReceiveOnline receiptBatchDetail = ReceiptUtil.newDetail(archiveBatch, archiveFileInfo);
            //TODO 文件名称没传过来用的是path 下载ofd
            UrlFileResource ofdFileResource;
            //电子档案类型 1：电子文件 2：数字化副本  李闯数字化加工工具的为2，其他的为1
            String classify = "1";
            if ("数字化加工工具".equalsIgnoreCase(archiveFileInfo.getSystemName())) {
                ofdFileResource = new UrlFileResource(archiveFileInfo.getPath());
                classify = "2";
            } else {
                ofdFileResource = downloadUrlFile(centerConfig.fullPath(archiveFileInfo.getPath()),
                        //TODO directory.getDocumentnumber()
                        archiveFileInfo.getPath(), "下载ofd文件失败", "3", receiveBo);
            }
            FileInfo ofdFileInfo = null;
            List<TransferInfo.TransferDirectory> directories = transferInfo.getDirectories();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (TransferInfo.TransferDirectory directory : directories) {
                //获取到元数据配置
                List<FieldMetaData> metadata;
                // if (StringUtils.hasText(transferInfo.getStandard())) {
                //用标准 查元数据配置  1:案件整理 TODO 案卷方式需要再处理
//                    metadata = getFieldMetaDataByStandard(directory.getCategoryCode(), classify, transferInfo.getStandard());
                // metadata = getFieldMetaDataByStandard(directory.getCategoryCode(), classify, transferInfo.getStandard(), "1");
                //  } else { //用归档时间 查元数据配置
                metadata = getFieldMetaData(directory.getCategoryCode(), directory.getArchiveTime(), classify);
                //  }
                Assert.isTrue(ObjectUtils.isNotEmpty(metadata), "未找到元数据配置!!!");
                //获取 表单定义对象
                Fond fond = fondService.findFondByCode(directory.getFondsIdentifier());
                Category category = categoryService.findCategoryByCode(directory.getCategoryCode(), fond.getId());
                CategoryFormQuery categoryFormQuery = new CategoryFormQuery();
                categoryFormQuery.setCategoryId(category.getId());
                categoryFormQuery.setCategoryCode(directory.getCategoryCode());
                categoryFormQuery.setFondId(fond.getId());
                categoryFormQuery.setCategoryType(CategoryType.FILE.getValue());
                CategoryConfig categoryConfig = categoryConfigService.getCategoryForms(categoryFormQuery);
                FormDefinition formDefinition = (FormDefinition) formDefinitionService.selectFormDefinitionById(categoryConfig.getFileFormId());
                FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinition.getId());
                UrlFileResource finalOfdFileResource = ofdFileResource;
                //临时代码
                TestFile(receiveBo, ofdFileResource.getInputStream());
                //TODO 目前都按文件级别处理,案卷级别先不处理
                //根据归档系统查到的元数据配置比对解析xml返回数据.
                Map<String, String> map = XPathParserXml(finalOfdFileResource.getInputStream(), BASIC_INFO_XML_NAME, "解析基本信息元数据失败", "9", receiveBo, metadata);

                //塞入数据的具体 表单
                FormData formData = new FormData(formDefinition.getId(), UUID.randomUUID().toString());
                //根据解析出来的数据,比对咱系统上的表字段有,则添加.
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    FieldModel fieldModel = Optional.ofNullable(formModel.getFieldByAlias(entry.getKey())).orElse(formModel.getFieldByCode(entry.getKey()));
                    if (ObjectUtils.isNotEmpty(fieldModel)) {
                        //处理时间 转换为 long类型
                        long aLong;
                        try {
                            aLong = simpleDateFormat.parse(entry.getValue()).getTime();
                            formData.put(fieldModel.getFieldCode(), aLong);
                        } catch (Exception e) {
                            formData.put(fieldModel.getFieldCode(), entry.getValue());
                        }
                    }
                }
//                formData.put(AbstractArchiveEntity.COLUMN_YEAR, "2021");
                formDataService.addFormData(formData);
                //TODO 添加其他元数据属性
                receiptBatchDetail.setFondCode(directory.getFondsIdentifier());
                receiptBatchDetail.setTaskCode(directory.getTaskCode());
                receiptBatchDetail.setTaskVersion(directory.getTaskVersion());
                receiptBatchDetail.setRegionCode(directory.getRegionCode());
                receiptBatchDetail.setSocialCode(directory.getSocialCode());
                receiptBatchDetail.setTitle(directory.getProjectName());
                receiptBatchDetail.setTasktype(formData.get(ArchiveEntity.COLUMN_TASK_TYPE));
                receiptBatchDetail.setTaskmname(formData.get(ArchiveEntity.COLUMN_TASK_NAME));
                receiptBatchDetail.setSaveTerm(formData.get(ArchiveEntity.COLUMN_SAVE_TERM));
                receiptBatchDetail.setFormDataId(formData.getId());
                receiptBatchDetail.setFormDefinitionId(formDefinition.getId());
                archiveBatchDetailReceiveOnlineService.insert(receiptBatchDetail);

                try {
                    //下载OFD文件并上传到系统中
                    ofdFileInfo = commonFileService.addFile(finalOfdFileResource, formData.getId(), "archive");
                } catch (IOException e) {
                    logger.error("绑定归档目录xml失败", e);
                }
                //默认安全性检测通过
                TestRecord testRecord = new TestRecord();
                //testRecord.setTestRecordType(TestRecordService.security);
                //testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
                // testRecord.setTestResultCode("1");
                testRecord.setStatus("1");
                // testRecord.setTestResult("数据包未被杀毒软件冻结，安全性检测通过！");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                //四性检测   这里是同步的，正是代码应该处理成异步
                testItems(receiveBo, formData, directory, archiveFileInfo, ofdFileInfo, receiptBatchDetail, formDefinition, category);
            }
//            checkSecurityHolder(organisePersonService);
        });
    }

    private Map<String, String> newXPathParserXml(ZipArchiveInputStream fis, List<FieldMetaData> metadata) {
        Map<String, String> map = new HashMap();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(fis);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String root = "";
            for (FieldMetaData fieldMetaData : metadata) {
                if ("0".equals(fieldMetaData.getParentID())) {
                    root = fieldMetaData.geteName();
                }
            }
            //TODO  暂时没处理fileset下面的
            for (FieldMetaData fieldMetaData : metadata) {
                // String titleXpath = "/" + root + "/" + fieldMetaData.geteName();
                //暂时写死
                String titleXpath = "/base_info/" + fieldMetaData.geteName();
                NodeList nodeList = (NodeList) xPath.evaluate(titleXpath, document, XPathConstants.NODESET);
                if (nodeList.getLength() == 1) {
                    Node item = nodeList.item(0);
                    NodeList childNodes = item.getChildNodes();
                    if (childNodes.getLength() <= 1) {
                        String titleValue = (String) xPath.evaluate(titleXpath, document, XPathConstants.STRING);
                        map.put(fieldMetaData.geteName(), titleValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    void TestFile(ArchiveReceiveBo receiveBo, InputStream inputStream) {
        ArchiveFileInfo archiveFileInfo = receiveBo.getFiles().get(0);
        try (ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream, "gbk", true)) {
            while (true) {
                ZipArchiveEntry archiveEntry = zais.getNextZipEntry();
                TestRecord testRecord = new TestRecord();
                //testRecord.setTestRecordType(TestRecordService.usability);
                //testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
                //testRecord.setTestResultCode("1");
                //testRecord.setTestResult("检测通过：【" + archiveEntry.getName() + "】打开成功");
                testRecord.setStatus("2");
                insertTestRecord(testRecord, null, archiveFileInfo);
            }

        } catch (Exception ignored) {

        }
    }


    private Map<String, String> XPathParserXml(InputStream inputStream, String zipFilePath, String errorMessage, String receiptCode, ArchiveReceiveBo receiveBo, List<FieldMetaData> metadata) {
        try (ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream, "gbk", true)) {
            while (true) {
                ZipArchiveEntry archiveEntry = zais.getNextZipEntry();
                if (archiveEntry.getName().endsWith(zipFilePath)) {
                    return newXPathParserXml(zais, metadata);
                }
            }
        } catch (IOException e) {
            ArchiveFileInfo archiveFileInfo = receiveBo.getFiles().get(0);
            TestRecord testRecord = new TestRecord();
            //testRecord.setTestRecordType(TestRecordService.usability);
            //testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
            //testRecord.setTestResultCode(receiptCode);
            //testRecord.setTestResult(errorMessage);
            testRecord.setStatus("2");
            insertTestRecord(testRecord, null, archiveFileInfo);
            throw new ReceiveException(errorMessage, e);
        }
    }

    private List<FieldMetaData> getFieldMetaData(String categoryCode, String time, String s) {
        List<FieldConfig> metadata = configManagerClient.getMetadata(categoryCode, s);
        Assert.isTrue(metadata.size() > 0, "未找到元数据配置!!!");
        Long aLong = DateTimeUtils.stringToMillis(time, "yyyy-MM-dd");
        FieldConfig config = null;
        for (FieldConfig fieldConfig : metadata) {
            Long startTime = DateTimeUtils.stringToMillis(fieldConfig.getStartTime(), "yyyy-MM-dd");
            if (StringUtils.hasText(fieldConfig.getEndTime())) {
                Long endTime = DateTimeUtils.stringToMillis(fieldConfig.getEndTime(), "yyyy-MM-dd");
                if (aLong >= startTime && aLong < endTime) {
                    config = fieldConfig;
                    break;
                }
            } else {
                config = fieldConfig;
                break;
            }
        }
        return config.getMetadata();
    }

    private List<FieldMetaData> getFieldMetaDataByStandard(String categoryCode, String s, String standard) {
        List<FieldConfig> metadata = configManagerClient.getCategoryMetadata(categoryCode, s, standard);
        Assert.isTrue(metadata.size() > 0, "未找到元数据配置!!!");
        return metadata.get(0).getMetadata();
    }

    private List<FieldMetaData> getFieldMetaDataByStandard(String categoryCode, String s, String standard, String arrange) {
        List<FieldConfig> metadata = configManagerClient.getCategoryMetadata(categoryCode, s, standard, arrange);
        Assert.isTrue(metadata.size() > 0, "未找到元数据配置!!!");
        return metadata.get(0).getMetadata();
    }


    /**
     * 主动推送回执消息
     *
     * @param receiptBatchDetail
     */
    private List<TestRecord> archiveResult(ArchiveBatchDetailReceiveOnline receiptBatchDetail) {
        SqlQuery<TestRecord> sqlQuery = SqlQuery.from(TestRecord.class)
                .equal(TestRecordInfo.BUSINESSID, receiptBatchDetail.getBatchId())
                /*.equal(TestRecordInfo.SYSTEMNUM, receiptBatchDetail.getSystemNum())
                .equal(TestRecordInfo.SYSTEMNAME, receiptBatchDetail.getSystemName())*/;
        List<TestRecord> testRecordList = testRecordService.selectList(sqlQuery);
       /* testRecordList.stream().map(TestRecord::getTestResult).collect(Collectors.joining(";"));
        testRecordList.forEach(testRecord -> {
            Receipt receipt = new Receipt();
            receipt.setSystemNum(receiptBatchDetail.getSystemNum());
            receipt.setSystemName(receiptBatchDetail.getSystemName());
            receipt.setReceiptCode(testRecord.getTestResultCode());
            receipt.setReceiptDescription(testRecord.getTestResult());

            approveCenterClient.getEngineMesasge(new ArchivePackResult(testRecord.getBusinessId(), "1".equals(receiptBatchDetail.getStatus()) ? receiptBatchDetail.getStatus() : "3", testRecord.getTestResultCode(), testRecord.getTestResult()));
        });*/
        if (testRecordList.size() == 0) {
            approveCenterClient.getEngineMesasge(new ArchivePackResult(receiptBatchDetail.getBusinessId(), "1", "1", null));
        }
        return testRecordList;
    }

    /**
     * @param testRecord
     */
    private void insertTestRecord(TestRecord testRecord, ArchiveBatchDetailReceiveOnline receiptBatchDetail, ArchiveFileInfo archiveFileInfo) {
        testRecord.setBusinessId(archiveFileInfo.getBusinessId());
        if (null != receiptBatchDetail) {
            AbstractArchiveBatch archiveBatch = archiveOnlineReceiveService.selectById(receiptBatchDetail.getBatchId());
            //添加批次信息
            // testRecord.setTestRecordBatchCode(archiveBatch.getId());
        }
        testRecordService.insert(testRecord);
    }

    /**
     * //TODO 检测通过之后插入表单中
     * //TODO　更新详情状态
     * //TODO 更新批次状态
     * 执行四性检测
     *
     * @param receiveBo          json参数
     * @param formData
     * @param directory
     * @param archiveFileInfo    文件基本信息参数
     * @param ofdFileInfo        ofd文件流
     * @param receiptBatchDetail 归档详情表
     */
    @Async
    public void testItems(ArchiveReceiveBo receiveBo, FormData formData, TransferInfo.TransferDirectory directory, ArchiveFileInfo archiveFileInfo, FileInfo ofdFileInfo, ArchiveBatchDetailReceiveOnline receiptBatchDetail, FormDefinition formDefinition, Category category) {
        //先读取归档检测配置信息
        ApproveItemConfig approveItemConfig = configManagerClient.getConfigDetailsByItemId(receiptBatchDetail.getTaskCode(), receiptBatchDetail.getSocialCode(), receiptBatchDetail.getRegionCode(), receiptBatchDetail.getTaskVersion(), directory.getCategoryCode(), directory.getFondsIdentifier());
        //文件包检测
        boolean testResult = testOfdFile(approveItemConfig, receiveBo, archiveFileInfo, ofdFileInfo, receiptBatchDetail);
        //元数据检测
        testResult = testResult & testMetaData(approveItemConfig, formData, archiveFileInfo, receiptBatchDetail);
        //材料清单检测  TODO
        // testResult = testResult & testMaterial(approveItemConfig, formData, ofdFileInfo, archiveFileInfo, receiptBatchDetail);

        //这里是临时代码
        TestRecord testRecord = new TestRecord();
        //  testRecord.setTestRecordType(TestRecordService.integrity);
//    //            testRecord.setTestRecordlinkCode();
        //  testRecord.setTestResultCode("1");
        //  testRecord.setStatus("1");
        //  testRecord.setTestResult("对比元数据和归档数据包中的文件数量通过！");

        insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);


        //判断检测结果，更新详情状态
        if (!testResult) {
            // 更新详情状态
            receiptBatchDetail.setStatus("4");
        } else {
            receiptBatchDetail.setStatus("3");
        }
        archiveBatchDetailReceiveOnlineService.updateById(receiptBatchDetail);
        //更新批次信息
        ArchiveBatchReceiveOnline archiveBatch = archiveOnlineReceiveService.selectById(receiptBatchDetail.getBatchId());
        archiveBatch.setEndDate(System.currentTimeMillis());
        archiveBatch.setStatus(StatusEntity.STATUS_ENABLE_STR);
        archiveOnlineReceiveService.updateById(archiveBatch);
        //主动推送回执信息
        archiveResult(receiptBatchDetail);
    }

    private void getSaveTerm(FormData data, ApproveArchiveBaseInfo baseInfo) {
        //根据typeId获取到保管期限规则
        List<SaveItemRule> saveItemRules = configManagerClient.getSaveTermConfig(baseInfo.getTaskCode());
        //根据保管期限规则排序，第一规则为Y、D30、D10；第二规则为优先级
        saveItemRules.sort((o1, o2) -> {
            int result = -(o1.getPeriod().compareTo(o2.getPeriod()));
            if (result == 0) {
                result = o1.getPriority() - o2.getPriority();
            }
            return result;
        });
        //根据保管期限规则确定保管期限
        for (SaveItemRule rule : saveItemRules) {
            boolean flag = false;
            List<SaveItemRule.MetadataItem> metadata = rule.getMetadata();
            for (SaveItemRule.MetadataItem item : metadata) {
                //TODO 目前字段还没对应，大致逻辑如下
                //if baseInfo.get(item.eName).contains(item.content) ->
                // data.put("保管期限", rule.getPeriod) flag = 1 break
                //else continue
            }
            if (flag) {
                break;
            }
        }
    }

    /**
     * 检测材料清单规则
     * <p>
     * 要么有，要么有多个
     *
     * @param approveItemConfig
     * @param baseInfo
     * @param ofdFileInfo
     * @return
     */
    private boolean testMaterial(ApproveItemConfig approveItemConfig, ApproveArchiveBaseInfo baseInfo, FileInfo ofdFileInfo, ArchiveFileInfo archiveFileInfo, ArchiveBatchDetailReceiveOnline receiptBatchDetail) {
        boolean result = true;
        //先读取zip包中的所有附件
        try {
            //所有文件列表
            List<String> zipContentFiles = ReceiptUtil.readZipFiles(fileSaveHandler.readFile(ofdFileInfo));
            //获取元数据中声明的所有文件
            Set<String> baseInfoFiles = baseInfo.getAllFileNames();
            //1、对比元数据和归档数据包中的文件名称
            Set<String> allNoContentFiles = ReceiptUtil.filterNoContent(baseInfoFiles, zipContentFiles);
            if (!allNoContentFiles.isEmpty()) {
                // 元数据中声明的文件在归档数据包中没有定义
                TestRecord testRecord = new TestRecord();
                //  testRecord.setTestRecordType(TestRecordService.integrity);
//            //    testRecord.setTestRecordlinkCode();
                //  testRecord.setTestResultCode("5");
                //  testRecord.setStatus("2");
                //  testRecord.setTestResult("元数据中声明的文件在归档数据包中没有定义:" + String.join(",", allNoContentFiles));
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                result = false;
            } else {
                TestRecord testRecord = new TestRecord();
                // testRecord.setTestRecordType(TestRecordService.integrity);
//             //   testRecord.setTestRecordlinkCode();
                // testRecord.setTestResultCode("1");
                // testRecord.setStatus("1");
                // testRecord.setTestResult("对比元数据和归档数据包中的文件名称通过！");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
            }
            //2、对比材料清单说明con
            //获取材料清单配置
            Set<String> configFileList = approveItemConfig.getStdFileList("");
            //获取元数据材料清单
            Set<String> baseInfoStdFiles = baseInfo.getStdFiles();
            Set<String> stdNocontentFiles = ReceiptUtil.filterNoContent(baseInfoStdFiles, configFileList);
            if (!stdNocontentFiles.isEmpty()) {
                //材料清单不存在
                TestRecord testRecord = new TestRecord();
                // testRecord.setTestRecordType(TestRecordService.integrity);
//            //   testRecord.setTestRecordlinkCode();
                // testRecord.setTestResultCode("5");
                // testRecord.setStatus("2");
                // testRecord.setTestResult("材料清单不存在:" + String.join(",", stdNocontentFiles));
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                result = false;
            } else {
                TestRecord testRecord = new TestRecord();
                // testRecord.setTestRecordType(TestRecordService.integrity);
//             //   testRecord.setTestRecordlinkCode();
                // testRecord.setTestResultCode("1");
                // testRecord.setTestResult("对比材料清单说明通过！");
                // testRecord.setStatus("1");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
            }
            //3、对比文件数量
            Map<String, Integer> materialCountMap = baseInfo.getMaterialCountMap();

            materialCountMap.entrySet().removeIf(entry -> entry.getKey() == null);
            List<String> linkRuleResult = null;
            if (materialCountMap.size() > 0 && ObjectUtils.isNotEmpty(approveItemConfig.getLinkRule())) {
                linkRuleResult = approveItemConfig.getLinkRule().testLinkRule(materialCountMap, approveItemConfig);
            }
            if (null != linkRuleResult && !linkRuleResult.isEmpty()) {
                //材料清单数量不对
                TestRecord testRecord = new TestRecord();
                //   testRecord.setTestRecordType(TestRecordService.integrity);
//           //     testRecord.setTestRecordlinkCode();
                //   testRecord.setTestResultCode("5");
                //   testRecord.setTestResult("材料清单数量不对:" + String.join(",", linkRuleResult));
                //   testRecord.setStatus("2");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                result = false;
            } else {
                TestRecord testRecord = new TestRecord();
                //  testRecord.setTestRecordType(TestRecordService.integrity);
//            //    testRecord.setTestRecordlinkCode();
                //  testRecord.setTestResultCode("1");
                //  testRecord.setStatus("1");
                //  testRecord.setTestResult("对比文件数量通过！");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 测试文件包完整性
     *
     * @param approveItemConfig
     * @param receiveBo
     * @param archiveFileInfo
     * @param ofdFileInfo
     * @return
     */
    private boolean testOfdFile(ApproveItemConfig approveItemConfig, ArchiveReceiveBo receiveBo, ArchiveFileInfo archiveFileInfo, FileInfo ofdFileInfo, ArchiveBatchDetailReceiveOnline receiptBatchDetail) {
        if (approveItemConfig.testFile(ApproveItemConfig.HUAN_JIE_GUI_DANG)) {
            //这里只做hash检测
            if (!StringUtils.hasText(archiveFileInfo.getDigitaldigest())) {
                //数字摘要不能为空！
                TestRecord testRecord = new TestRecord();
                //  testRecord.setTestRecordType(TestRecordService.authenticity);
                //  testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
                //  testRecord.setTestResultCode("10");
                //  testRecord.setTestResult("数字摘要为空!");
                testRecord.setStatus("2");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                return false;
            } else {
                boolean result = archiveFileInfo.getDigitaldigest().equals(ofdFileInfo.getFileHash());
                if (result) {
                   /* // 数字摘要不对
                    TestRecord testRecord = new TestRecord();
                    testRecord.setTestRecordType(TestRecordService.authenticity);
                    testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
                    testRecord.setTestResultCode("6");
                    testRecord.setTestResult("数字摘要校验失败！");
                    testRecord.setStatus("2");
                    insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                } else {*/
                    TestRecord testRecord = new TestRecord();
                    //   testRecord.setTestRecordType(TestRecordService.authenticity);
                    //   testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
                    //   testRecord.setTestResultCode("1");
                    //   testRecord.setTestResult("数字摘要校验通过！");
                    testRecord.setStatus("1");
                    insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                }
                return result;
            }
        }
        return true;
    }

    private boolean testMetaData(ApproveItemConfig approveItemConfig, FormData baseInfo, ArchiveFileInfo archiveFileInfo, ArchiveBatchDetailReceiveOnline receiptBatchDetail) {
        //获取归档环节元数据检测方案
        List<MetaRule> rules = approveItemConfig.getMetaRules(ApproveItemConfig.HUAN_JIE_GUI_DANG);
        boolean result = true;
        //根据检测规则执行具体的元数据检测
        for (MetaRule rule : rules) {
            result = result & doTestMeta(rule, baseInfo, archiveFileInfo, receiptBatchDetail);
        }
        return true;
    }

    /**
     * 根据具体的检测规则对单个元数据执行检测
     *
     * @param rule
     * @param baseInfo
     * @return
     */
    private boolean doTestMeta(MetaRule rule, FormData baseInfo, ArchiveFileInfo archiveFileInfo, ArchiveBatchDetailReceiveOnline receiptBatchDetail) {
        String metaName = rule.geteName();
        Object metaValue = baseInfo.get(metaName);
        if (metaValue == null) {
            if (rule.isRequired()) {
                //保存检测结果 元数据不能为空！
                TestRecord testRecord = new TestRecord();
                // testRecord.setTestRecordType(TestRecordService.authenticity);
                // testRecord.setTestRecordlinkCode(rule.getLinkCode());
                // testRecord.setTestResultCode("5");
                // testRecord.setTestResult("元数据【" + metaName + "】不能为空");
                testRecord.setStatus("2");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                return false;
            } else {
                //如果数据可以为空，直接返回通过
                TestRecord testRecord = new TestRecord();
                // testRecord.setTestRecordType(TestRecordService.authenticity);
                // testRecord.setTestRecordlinkCode(rule.getLinkCode());
                // testRecord.setTestResultCode("1");
                // testRecord.setTestResult("元数据【" + metaName + "】检测通过！");
                testRecord.setStatus("1");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
            }
        }
        //检测数据类型
        Class valueType = metaValue.getClass();
        switch (rule.getType()) {
            case TYPE_INTEGER:
                if (!NumberUtils.STANDARD_NUMBER_TYPES.contains(valueType)) {
                    //数据类型不对，
                    TestRecord testRecord = new TestRecord();
                    //   testRecord.setTestRecordType(TestRecordService.authenticity);
                    //   testRecord.setTestRecordlinkCode(rule.getLinkCode());
                    //   testRecord.setTestResultCode("5");
                    //   testRecord.setTestResult("数据类型" + metaValue + "不对，应该为" + TYPE_INTEGER + "!");
                    testRecord.setStatus("2");
                    insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                    return false;
                }
                break;
            case TYPE_DATE_TYPE:
                if (!(Date.class.isAssignableFrom(valueType) || Long.class.equals(valueType) || long.class.equals(valueType))) {
                    TestRecord testRecord = new TestRecord();
                    // testRecord.setTestRecordType(TestRecordService.authenticity);
                    // testRecord.setTestRecordlinkCode(rule.getLinkCode());
                    // testRecord.setTestResultCode("5");
                    // testRecord.setStatus("2");
                    // testRecord.setTestResult("数据类型" + metaValue + "不对，应该为" + TYPE_DATE_TYPE + "!");
                    insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                    return false;
                }
                break;
            case TYPE_CHARACTER:
                if (!String.class.isAssignableFrom(valueType)) {
                    TestRecord testRecord = new TestRecord();
                    //  testRecord.setTestRecordType(TestRecordService.authenticity);
                    //  testRecord.setTestRecordlinkCode(rule.getLinkCode());
                    //  testRecord.setTestResultCode("5");
                    //  testRecord.setTestResult("数据类型" + metaValue + "不对，应该为" + TYPE_CHARACTER + "!");
                    //  testRecord.setStatus("2");
                    insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                    return false;
                }
                break;
            default:
                //未识别数据类型
                TestRecord testRecord = new TestRecord();
                //  testRecord.setTestRecordType(TestRecordService.authenticity);
                //  testRecord.setTestRecordlinkCode(rule.getLinkCode());
                //  testRecord.setTestResultCode("5");
                //  testRecord.setTestResult("未识别数据类型" + metaValue + "未识别！");
                testRecord.setStatus("2");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                return false;
        }
        //检测五项具体的元数据规则
        String[] testItems = rule.getTestItems().split(",");
        for (String item : testItems) {
            boolean result = true;
            switch (item) {
                case "Authenticity001":
                    //
                    result = testMaxLength(metaValue, rule.getMaxLen());
                    if (!result) {
                        TestRecord testRecord = new TestRecord();
                        // testRecord.setTestRecordType(TestRecordService.authenticity);
                        // testRecord.setTestRecordlinkCode(rule.getLinkCode());
                        // testRecord.setTestResultCode("5");
                        // testRecord.setTestResult(metaValue + "长度校验失败,限制长度为：" + rule.getMaxLen() + "！");
                        testRecord.setStatus("2");
                        insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                    }
                    break;
                case "Authenticity002":
                    //TODO 著录项目值域符合度检测
                    break;
                case "Authenticity003":
                    //数据类型检测，上面已经实现了
                    break;
                case "Authenticity004":
                    result = testMaxMin(metaValue, rule.getMaxVal(), rule.getMinVal());
                    if (!result) {
                        TestRecord testRecord = new TestRecord();
                        // testRecord.setTestRecordType(TestRecordService.authenticity);
                        // testRecord.setTestRecordlinkCode(rule.getLinkCode());
                        // testRecord.setTestResultCode("5");
                        // testRecord.setTestResult(metaValue + "最值校验失败,限制最值为：" + rule.getMaxLen() + "！");
                        testRecord.setStatus("2");
                        insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
                    }
                    break;
                default:
                    break;
            }
            if (result) {
                TestRecord testRecord = new TestRecord();
                // testRecord.setTestRecordType(TestRecordService.authenticity);
                // testRecord.setTestRecordlinkCode(rule.getLinkCode());
                // testRecord.setTestResultCode("1");
                // testRecord.setTestResult("元数据规则检测通过");
                testRecord.setStatus("1");
                insertTestRecord(testRecord, receiptBatchDetail, archiveFileInfo);
            }
            //todo 保存检测结果

        }
        //所有检测都通过则是成功
        return true;
    }

    /**
     * @param metaValue
     * @param maxVal
     * @param minVal
     * @return
     */
    private boolean testMaxMin(Object metaValue, String maxVal, String minVal) {
        return metaValue.toString().length() <= Integer.parseInt(maxVal) && metaValue.toString().length() <= Integer.parseInt(minVal);
    }

    /**
     * 检测数据最大长度
     *
     * @param metaValue
     * @param maxLen
     * @return
     */
    private boolean testMaxLength(Object metaValue, String maxLen) {
        return metaValue.toString().length() <= Integer.parseInt(maxLen);
    }


    @Override
    public void afterPropertiesSet() {
        objectMapper = converter.getObjectMapper();
    }

    /**
     * 根据路径下载文件，异常直接抛出异常信息
     *
     * @param path             文件网络地址
     * @param fileName         文件名称
     * @param errorMessage     错误信息
     * @param receiptCode      回执编码
     * @param archiveReceiveBo 档案归档信息
     * @return
     */
    UrlFileResource downloadUrlFile(String path, String fileName, String errorMessage, String receiptCode, ArchiveReceiveBo archiveReceiveBo) {
        logger.warn("开始下载文件：{}", path);
        try {
            int index = fileName.lastIndexOf("/");
            if (index > 0) {
                fileName = fileName.substring(index + 1);
            }
            return new UrlFileResource(path, fileName);
        } catch (IOException e) {
            ArchiveFileInfo archiveFileInfo = archiveReceiveBo.getFiles().get(0);
            TestRecord testRecord = new TestRecord();
            // testRecord.setTestRecordType(TestRecordService.usability);
            // testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
            // testRecord.setTestResultCode(receiptCode);
            // testRecord.setTestResult(errorMessage + ":" + fileName + "！");
            testRecord.setStatus("2");
            insertTestRecord(testRecord, null, archiveFileInfo);
            throw new ReceiveException(errorMessage, e);
        }
    }

    /**
     * 根据流解析xml对象
     *
     * @param inputStream
     * @param objectMapper
     * @param targetClass
     * @param errorMessage     解析失败的异常信息
     * @param receiptCode      回执编码
     * @param archiveReceiveBo 档案归档信息
     * @param <T>
     * @return
     */
    <T> T parseXml(InputStream inputStream, ObjectMapper objectMapper, Class<T> targetClass, String errorMessage, String receiptCode, ArchiveReceiveBo archiveReceiveBo) {
        try {
            return objectMapper.readValue(inputStream, targetClass);
        } catch (IOException e) {
            ArchiveFileInfo archiveFileInfo = archiveReceiveBo.getFiles().get(0);
            TestRecord testRecord = new TestRecord();
            // testRecord.setTestRecordType(TestRecordService.usability);
            // testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
            // testRecord.setTestResultCode(receiptCode);
            // testRecord.setTestResult(errorMessage);
            testRecord.setStatus("2");
            insertTestRecord(testRecord, null, archiveFileInfo);
            throw new ReceiveException(errorMessage, e);
        }
    }

    /**
     * 解析zip文件中的xml数据
     *
     * @param inputStream
     * @param zipFilePath
     * @param targetClass
     * @param errorMessage
     * @param <T>
     * @return
     */
    <T> T parseZipXml(InputStream inputStream, String zipFilePath, Class<T> targetClass, String errorMessage, String receiptCode, ArchiveReceiveBo archiveReceiveBo) {
        try (ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream, "gbk")) {
            ZipArchiveEntry archiveEntry = zais.getNextZipEntry();
            while (true) {
                if (archiveEntry.getName().equals(zipFilePath)) {
                    return objectMapper.readValue(zais, targetClass);
                }
                archiveEntry = zais.getNextZipEntry();
            }
        } catch (IOException e) {
            ArchiveFileInfo archiveFileInfo = archiveReceiveBo.getFiles().get(0);
            TestRecord testRecord = new TestRecord();
            // testRecord.setTestRecordType(TestRecordService.usability);
            // testRecord.setTestRecordlinkCode(ApproveItemConfig.HUAN_JIE_GUI_DANG);
            // testRecord.setTestResultCode(receiptCode);
            // testRecord.setTestResult(errorMessage);
            testRecord.setStatus("2");
            insertTestRecord(testRecord, null, archiveFileInfo);
            throw new ReceiveException(errorMessage, e);
        }
    }
}
