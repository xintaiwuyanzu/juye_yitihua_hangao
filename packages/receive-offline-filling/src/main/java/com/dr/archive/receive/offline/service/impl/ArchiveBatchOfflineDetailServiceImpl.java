package com.dr.archive.receive.offline.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.EArchiveBatchService;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.common.dzdacqbc.service.EFileInfoService;
import com.dr.archive.common.dzdacqbc.service.ESaveSpacesService;
import com.dr.archive.common.packet.util.PacketDataParserUtil;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.formMap.bo.FormKeyMap;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecordInfo;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.impexpscheme.service.impl.AbstractDataParserArchiveBatchDetailService;
import com.dr.archive.manage.template.entity.CompilationTemplate;
import com.dr.archive.manage.template.service.CompilationTemplateService;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetailInfo;
import com.dr.archive.receive.offline.service.ArchiveBatchOfflineDetailService;
import com.dr.archive.receive.offline.service.ArchiveBatchOfflineService;
import com.dr.archive.transfer.bo.TransferInfo;
import com.dr.archive.util.Constants;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.util.ZipUtil;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.file.service.impl.DefaultFileHandler;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 离线目录导入详情
 *
 * @author: dr
 * @date: 2020/11/18 1:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveBatchOfflineDetailServiceImpl extends AbstractDataParserArchiveBatchDetailService<ArchiveBatchReceiveOfflineDetail> implements ArchiveBatchOfflineDetailService, InitializingBean {
    @Autowired
    ArchiveMetadataRecordService archiveMetadataRecordService;
    @Autowired
    ManagementService managementService;
    @Autowired
    CompilationTemplateService compilationTemplateService;

    /**
     * 长期保存电子档案
     */
    @Autowired
    EArchiveService eArchiveService;
    /**
     * 入库批次
     */
    @Autowired
    EArchiveBatchService eArchiveBatchService;
    @Lazy
    @Autowired
    ArchiveBatchOfflineService archiveBatchOfflineService;
    @Autowired
    ESaveSpacesService eSaveSpacesService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    DefaultFileHandler defaultFileHandler;
    @Autowired
    EFileInfoService eFileInfoService;
    @Autowired
    protected FileInfoHandler fileInfoHandler;
    @Autowired
    MappingJackson2XmlHttpMessageConverter converter;
    protected ObjectMapper xmlObjectMapper;
    @Autowired
    FormDataService formDataService;

    final ExecutorService executorService = Executors.newFixedThreadPool(40);
    long count = 0;
    boolean ifNest=false;
    @Override
    @Async
    protected void doCreateDetail(AbstractArchiveBatch archiveBatch, BatchCreateQuery query) {

        count = 0;
        ArchiveBatchReceiveOffline batch = (ArchiveBatchReceiveOffline) archiveBatch;
        //更新状态
        batch.setStartDate(System.currentTimeMillis());
        batch.setTransferUnit(query.getTransferingUnit());
        commonMapper.updateById(batch);

        List<FormKeyMap> formKeyMaps = formKeyMapService.getFormKeyMap(query.getImpSchemaId());
        try {
            Iterator<Map<String, Object>> iterator = dataParser.readData(Paths.get(batch.getFileLocation()), batch.getMineType());
            String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
            SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
            while (iterator.hasNext()) {
                Map<String, Object> map = iterator.next();
                if (!iterator.hasNext()){
                    ifNest=true;
                }
                //加线程
                executorService.execute(()->{
                    SecurityHolder.set(securityHolder);
                    getCount(query, batch, formKeyMaps, map,ifNest);

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            batch.setStatus(StatusEntity.STATUS_UNKNOW_STR);
            batch.setEndDate(System.currentTimeMillis());
            commonMapper.updateById(batch);
        }
    }
    @Async
    @Transactional
    public void getCount(BatchCreateQuery query, ArchiveBatchReceiveOffline batch, List<FormKeyMap> formKeyMaps, Map<String, Object> map,boolean ifNest) {
        count++;
        FormData data = new FormData(query.getFormDefinitionId());
        if (map.containsKey(IdEntity.ID_COLUMN_NAME)) {
            data.setId((String) map.get(IdEntity.ID_COLUMN_NAME));
        } else {
            data.setId(UUIDUtils.getUUID());
        }
        for (FormKeyMap formKeyMap : formKeyMaps) {
            Object value = map.get(formKeyMap.getTargetCode());
            if (value != null) {
                data.put(formKeyMap.getFieldCode(), (Serializable) value);
            }
        }
        //设置数据来源
        data.put(AbstractArchiveEntity.COLUMN_SOURCE_TYPE, query.getSourceCode());
        //自动设置全宗号
        data.put(AbstractArchiveEntity.COLUMN_FOND_CODE, query.getFondCode());
        //自动设置分类编号
        data.put(AbstractArchiveEntity.COLUMN_CATEGORY_CODE, query.getCategoryCode());
        String status = query.getName();
        if (StringUtils.isEmpty(status)) {
            status = ArchiveDataManager.STATUS_PRE;
        }
        //导入数据设置为预归档状态
        data.put(AbstractArchiveEntity.STATUS_COLUMN_KEY, status);
        Organise organise = SecurityHolder.get().currentOrganise();
        if (null != organise) {
            if ("dag".equals(organise.getOrganiseType())) {
                data.put(AbstractArchiveEntity.COLUMN_ORGANISEID, SecurityHolder.get().currentOrganise().getId());
            }
        }
        String statusKey = data.get(ArchiveEntity.STATUS_COLUMN_KEY);
        if ("SPECIAL".equals(statusKey) || "special".equals(statusKey)) {//只修改专题档案的数据
            data.put(AbstractArchiveEntity.COLUMN_ORGANISEID, SecurityHolder.get().currentOrganise().getId());
        }
        //执行保存数据
        //TODO 需要实现四性检测未通过的不能插入
        dataManager.insertFormData(data, null, query.getCategoryId());
        //创建批次记录
        query.setFourDetection(data.getString("fourDetection"));
        newBatchDetail(data, batch, query);
        //状态 未挂接 未检测
        SqlQuery sqlQuery = SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                .set(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, "0")
                .set(ArchiveBatchReceiveOfflineDetailInfo.TESTSTATUS, "-1")
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batch.getId());
        updateBySqlQuery(sqlQuery);
        if (ifNest){
            List<ArchiveBatchReceiveOfflineDetail> archiveBatchReceiveOfflineDetails = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class).equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batch.getId()));
            batch.setDetailNum(archiveBatchReceiveOfflineDetails.size());
            //保存结果
            batch.setStatus(StatusEntity.STATUS_ENABLE_STR);
            batch.setEndDate(System.currentTimeMillis());
            commonMapper.updateById(batch);
        }
    }
    /*包结构 建明细*/
    @Override
    public void createDetail2(BatchCreateQuery query, AbstractArchiveBatch archiveBatch) {
        ArchiveBatchReceiveOffline batch = (ArchiveBatchReceiveOffline) archiveBatch;
        //更新状态
        batch.setStartDate(System.currentTimeMillis());
        batch.setTransferUnit(query.getTransferingUnit());
        batch.setTransferUnitPerson(query.getTransferingUnitPerson());
        commonMapper.updateById(batch);

        List<FormKeyMap> formKeyMaps = formKeyMapService.getFormKeyMap(query.getImpSchemaId());
        File localFile = new File(batch.getFileLocation());
        try {
            TransferInfo transferInfo = xmlObjectMapper.readValue(localFile, TransferInfo.class);
            long count = transferInfo.getDirectories().size();
            /*先根据交接xml创建目录,之后挂接后根据解析出的基本信息元数据进行更新*/
            for (TransferInfo.TransferDirectory directory : transferInfo.getDirectories()) {
                FormData data = new FormData(query.getFormDefinitionId());
                data.setId(UUIDUtils.getUUID());
                //设置数据来源
                data.put(AbstractArchiveEntity.COLUMN_SOURCE_TYPE, query.getSourceCode());
                //自动设置全宗号
                data.put(AbstractArchiveEntity.COLUMN_FOND_CODE, query.getFondCode());
                //自动设置分类编号
                data.put(AbstractArchiveEntity.COLUMN_CATEGORY_CODE, query.getCategoryCode());
                //导入数据设置为预归档状态
                data.put(AbstractArchiveEntity.STATUS_COLUMN_KEY, ArchiveDataManager.STATUS_RECEIVE);
                //档号
                String archive_code = directory.getDocumentNumber();
                if (!StringUtils.hasText(archive_code)) {
                    archive_code = directory.getArchivalCode();
                }
                data.put(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE, archive_code);
                //题名
                data.put(AbstractArchiveEntity.COLUMN_TITLE, directory.getTitle());
                //年度
                data.put(AbstractArchiveEntity.COLUMN_YEAR, directory.getYear());
                Organise organise = SecurityHolder.get().currentOrganise();
                if (null != organise) {
                    if ("dag".equals(organise.getOrganiseType())) {
                        data.put(AbstractArchiveEntity.COLUMN_ORGANISEID, SecurityHolder.get().currentOrganise().getId());
                    }
                }
                //执行保存数据
                dataManager.insertFormData(data, null, query.getCategoryId());
                query.setFourDetection(data.getString("fourDetection"));
                newBatchDetail(data, batch, query);
                //状态 未挂接 未检测
                SqlQuery sqlQuery = SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                        .set(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, "0")
                        .set(ArchiveBatchReceiveOfflineDetailInfo.TESTSTATUS, "-1")
                        .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batch.getId());
                updateBySqlQuery(sqlQuery);
            }
            batch.setDetailNum(count);
            //保存结果
            batch.setStatus(StatusEntity.STATUS_ENABLE_STR);
            batch.setEndDate(System.currentTimeMillis());
            commonMapper.updateById(batch);
        } catch (Exception e) {
            e.printStackTrace();
            batch.setStatus(StatusEntity.STATUS_UNKNOW_STR);
            batch.setEndDate(System.currentTimeMillis());
            commonMapper.updateById(batch);
        }
    }

    @Override
    public Page<ArchiveBatchReceiveOfflineDetail> selectPage(AbstractArchiveBatch batch, BatchCreateQuery query, Integer start, Integer pageSize) {
        ArchiveBatchReceiveOffline offBatch = (ArchiveBatchReceiveOffline) batch;

        SqlQuery<ArchiveBatchReceiveOfflineDetail> sqlQuery = SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batch.getId())
                .equal(ArchiveBatchReceiveOfflineDetailInfo.STATUS, batch.getStatus())
                .equal(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, offBatch.getHookStatus());
        if (!ObjectUtils.isEmpty(query)) {
            //TODO
            // sqlQuery.equal(ArchiveBatchReceiveOfflineDetailInfo.FOURDETECTION, query.getFourDetection());
        }
        return commonMapper.selectPageByQuery(sqlQuery, start * pageSize, (start + 1) * pageSize);
    }

    @Override
    public String getType() {
        return BATCH_TYPE_IMP;
    }

    @Override
    public String getName() {
        return "导入";
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void filling(ArchiveBatchReceiveOffline offline) {
        List<ArchiveBatchReceiveOfflineDetail> details = selectByBatchId(offline.getId());
        Organise organise = SecurityHolder.get().currentOrganise();
        String organiseType = organise.getOrganiseType();
        //推送生成全宗卷信息,TODO 青岛港去掉该功能
//        insertManageInfo(offline, details, organise);
        //追加入库批次
        EArchiveBatch archiveBatch = insertEarchiveBatch(offline);

        for (ArchiveBatchReceiveOfflineDetail detail : details) {
            dataManager.updateStatus(detail.getFormDataId(), ArchiveDataManager.STATUS_MANAGE, detail.getFormDefinitionId());
            //  添加接收行为的管理过程元数据记录  档案馆 的入库成为移交操作
            if (Constants.ORG_TYPE_DAG.equals(organiseType)) {
                archiveMetadataRecordService.insert(new ArchiveMetadataRecord(detail.getFormDefinitionId(), detail.getFormDataId(), detail.getFondCode(), detail.getCategoryCode(), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_OFFLINE_YIJIAO, "离线移交", "离线移交", "离线移交", "", "", "", "", null, offline.getCreateDate()));
            } else {
                archiveMetadataRecordService.insert(new ArchiveMetadataRecord(detail.getFormDefinitionId(), detail.getFormDataId(), detail.getFondCode(), detail.getCategoryCode(), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_OFFLINE_RECEIVE, "离线归档", "离线归档", "离线归档", "", "", "", "", null, offline.getCreateDate()));
            }
            // 添加入库行为的管理过程元数据记录
            archiveMetadataRecordService.insert(new ArchiveMetadataRecord(detail.getFormDefinitionId(), detail.getFormDataId(), detail.getFondCode(), detail.getCategoryCode(), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_INGL, "入库", "入库", "入库", "", "", "", "", null));
            //入库到长期保存系统
            insertEarchive(offline, archiveBatch, detail);
        }
    }

    @Override
    public long deleteDetail(String id) {
        ArchiveBatchReceiveOfflineDetail detail = selectById(id);
        long result = deleteById(id);
        //删除目录,原文
        List<FormData> formDataList = formDataService.selectFormData(detail.getFormDefinitionId(), (sqlQuery, formRelationWrapper) -> {
            sqlQuery.equal(formRelationWrapper.getColumn(ArchiveEntity.ID_COLUMN_NAME), detail.getFormDataId()).equal(formRelationWrapper.getColumn(ArchiveEntity.STATUS_COLUMN_KEY), ArchiveDataManager.STATUS_RECEIVE);
        });
        if (formDataList.size() > 0) {
            dataManager.deleteFormData(detail.getCategoryId(), detail.getFormDefinitionId(), detail.getFormDataId());
        }
        //更新批次表的挂接与四性检测信息
        ArchiveBatchReceiveOffline batchReceiveOffline = archiveBatchOfflineService.selectById(detail.getBatchId());
        long total = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class, false)
                .count(ArchiveBatchReceiveOfflineDetailInfo.ID)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, detail.getBatchId()));
        long testSuccessTotal = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class, false)
                .count(ArchiveBatchReceiveOfflineDetailInfo.ID)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, detail.getBatchId())
                .equal(ArchiveBatchReceiveOfflineDetailInfo.TESTSTATUS, TestRecordService.TEST_STATUS_SUCCESS));
        long hookSuccessTotal = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class, false)
                .count(ArchiveBatchReceiveOfflineDetailInfo.ID)
                .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, detail.getBatchId())
                .equal(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, StatusEntity.STATUS_ENABLE_STR));
        batchReceiveOffline.setHookSuccessNum(hookSuccessTotal);
        batchReceiveOffline.setDetailNum(total);
        if (total == hookSuccessTotal) {
            batchReceiveOffline.setHookStatus(StatusEntity.STATUS_ENABLE_STR);
        }
        if (total == testSuccessTotal) {
            batchReceiveOffline.setTestStatus(TestRecordService.TEST_STATUS_SUCCESS);
        }
        archiveBatchOfflineService.updateById(batchReceiveOffline);
        return result;
    }


    /**
     * 根据移交批次创建入库批次
     *
     * @param offline
     * @return
     */
    private EArchiveBatch insertEarchiveBatch(ArchiveBatchReceiveOffline offline) {
        EArchiveBatch batch = new EArchiveBatch();
        batch.setBatchName(offline.getBatchName() + "自动入库");
        batch.setBatchType(EArchiveBatchService.BATCH_TYPE_IN);
        batch.setStartDate(System.currentTimeMillis());
        batch.setDetailNum(offline.getDetailNum());
        batch.setBeizhu("归档入库");
        batch.setOrgId(offline.getOrgId());

        eArchiveBatchService.insert(batch);
        return batch;
    }

    /**
     * //TODO，追加文件
     * 追加档案到长期保存库
     *
     * @param offline
     * @param archiveBatch
     * @param off
     */
    private void insertEarchive(ArchiveBatchReceiveOffline offline, EArchiveBatch archiveBatch, ArchiveBatchReceiveOfflineDetail off) {
        //判断是那个分类id

        //添加电子文件长期保存
        EArchive eArchive = new EArchive();
        eArchive.setId(UUIDUtils.getUUID());
        eArchive.setFondCode(off.getFondCode());
        eArchive.setCategoryCode(off.getCategoryCode());
        eArchive.setFormDataId(off.getFormDataId());
        eArchive.setFormDefinitionId(off.getFormDefinitionId());
        eArchive.setOrgCode(off.getOrgCode());
        eArchive.setArchiveCode(off.getArchiveCode());
        eArchive.setTitle(off.getTitle());
        eArchive.setKeyWords(off.getKeyWords());
        eArchive.setNote(off.getNote());
        eArchive.setYear(off.getYear());
        eArchive.setInDate(System.currentTimeMillis());
        eArchive.setInBatchId(archiveBatch.getId());
        eArchive.setInBatchNo(archiveBatch.getBatchName());
        eArchive.setOutCount(0);


        //添加入库详情记录
        EArchiveBatchDetail inDetail = new EArchiveBatchDetail();
        off.cloneArchive(inDetail);
        inDetail.setBatchId(archiveBatch.getId());
        inDetail.setBatchName(archiveBatch.getBatchName());
        inDetail.setArchiveId(eArchive.getId());
        inDetail.setLastTestRecordId(off.getLastTestRecordId());
        CommonService.bindCreateInfo(inDetail);
        commonMapper.insert(inDetail);

        //TODO 原文备份到长期保存库,存放规则根据配置
        putLongTermFile(off, eArchive);
        //TODO 更详细的绑定信息
        eArchiveService.insert(eArchive);
    }

    /*
     * 长期保存入库,原文复制到指定路径下*/
    private void putLongTermFile(ArchiveBatchReceiveOfflineDetail off, EArchive eArchive) {
        //查长期保存配置的保存路径
        String path = "";
        //根据全宗号和门类号查分类表数据
        List<CqbcClassification> cqbcClassifications = commonMapper.selectByQuery(SqlQuery.from(CqbcClassification.class)
                .equal(CqbcClassificationInfo.FONDNO, off.getFondCode())
                .equal(CqbcClassificationInfo.CLASSCODE, off.getCategoryCode()));
        //根据全宗号和门类号没有找到分类数据 则取默认分类数据
        if (cqbcClassifications.size() == 0) {
            cqbcClassifications = commonMapper.selectByQuery(SqlQuery.from(CqbcClassification.class)
                    .equal(CqbcClassificationInfo.ISDEFAULT, "true"));
        }
        Assert.isTrue(cqbcClassifications.size() > 0, "无默认存储空间分类管理!");
        //根据分类信息取存储空间
        ESaveSpaces saveSpaces = eSaveSpacesService.selectById(cqbcClassifications.get(0).getSpacesId());
        //TODO 暂时写死
        //全宗-门类-年度-保管期限-（机构问题）-档号.zip
        Assert.isTrue(saveSpaces != null, "无长期保存库存储空间!");
        path = saveSpaces.getCatalogue() + File.separator + off.getFondCode() +
                File.separator + off.getCategoryCode() +
                File.separator + off.getYear() +
                File.separator + off.getArchiveCode();
        //将原文复制到指定目录下
        List<FileInfo> fileInfoList = commonFileService.list(off.getFormDataId(), "archive", "default");
        for (FileInfo fileInfo : fileInfoList) {
            String filepath = path + File.separator + fileInfo.getName();
            //判断是否有该路径 ,无则建

            try {
                File file = new File(filepath);

                if (file.isDirectory() && !file.exists()) {
                    file.getParentFile().mkdirs();
                }
                defaultFileHandler.copyTo(fileInfo, filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //管理过程元数据
        List<ArchiveMetadataRecord> archiveMetadataRecordList = archiveMetadataRecordService.selectList(SqlQuery.from(ArchiveMetadataRecord.class).equal(ArchiveMetadataRecordInfo.FORMDATAID, off.getFormDataId()));
        //将List转为xml文件
        String mdPath = path + File.separator + "管理过程元数据.xml";

        try (OutputStream fileOutputStream = Files.newOutputStream(Paths.get(mdPath))) {
            JavaType javaType = PacketDataParserUtil.objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ArchiveMetadataRecord.class);
            PacketDataParserUtil.objectMapper.writerWithDefaultPrettyPrinter().forType(javaType).writeValue(fileOutputStream, archiveMetadataRecordList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //打成zip
        try {
            ZipUtil.toZip(path, path + ".zip", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File zipFile = new File(path + ".zip");
        //长期保存文件信息
        EFileInfo efileInfo = new EFileInfo();
        efileInfo.setFileName(off.getArchiveCode() + ".zip");
        efileInfo.setFilePath(path + ".zip");
        efileInfo.setFormDataId(off.getFormDataId());
        efileInfo.setFormDefinitionId(off.getFormDefinitionId());
        efileInfo.setArchiveId(eArchive.getId());
        efileInfo.setFileSize(zipFile.length());
        efileInfo.setSuffix("zip");

        eArchive.setSpaceId(saveSpaces.getId());
        eArchive.setClassificationId(cqbcClassifications.get(0).getId());
        //文件摘要,取文件hash sha512
        try {
            String fileHash = this.fileInfoHandler.fileHash(new FileInputStream(zipFile));
            Assert.isTrue(!StringUtils.isEmpty(fileHash), "文件hash值不能为空！");
            efileInfo.setDigest(fileHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eFileInfoService.insert(efileInfo);
    }

    /**
     * 接收成功，需要入库文件
     *
     * @param offline
     * @param details
     * @param organise
     */
    private void insertManageInfo(ArchiveBatchReceiveOffline offline, List<ArchiveBatchReceiveOfflineDetail> details, Organise organise) {
        //根据模板生成交接单据
        List<com.dr.archive.manage.template.entity.CompilationTemplate> compilationTemplateList = compilationTemplateService.getCompilationTemplateByCode(ManagementService.SUB_TYPE_CODE_DASJL_DALYHDALSZYGCSMCL);
        if (compilationTemplateList.size() > 0 && compilationTemplateList.get(0).getCompilationContent() != null) {
            //Assert.isTrue(compilationTemplateList.size() > 0, "未配置【" + ManagementService.SUB_TYPE_CODE_DASJL_DALYHDALSZYGCSMCL + "】类型的模板！");
            CompilationTemplate compilationTemplate = compilationTemplateList.get(0);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("offline", offline);
            dataMap.put("organise", organise);
            dataMap.put("details", details);
            String html = compilationTemplateService.getHtml(dataMap, compilationTemplate.getCompilationContent());
            Person person = SecurityHolder.get().currentPerson();
            //推送全宗卷数据
            Management management = new Management(
                    UUID.randomUUID().toString(),
                    details.get(0).getFondCode(),
                    ManagementService.SUB_TYPE_CODE_DASJL_DALYHDALSZYGCSMCL,
                    "归档清单" + offline.getBatchName(),
                    html,
                    person.getUserName(),
                    DateFormatUtils.format(offline.getCreateDate(), "yyyyMMdd"),
                    DateFormatUtils.format(offline.getCreateDate(), "yyyy"),
                    DateFormatUtils.format(offline.getCreateDate(), "yyyy")
            );
            //TODO 交接单据xml或者excel作为附件
            managementService.insert(management);
        }
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        xmlObjectMapper = converter.getObjectMapper();
    }
}
