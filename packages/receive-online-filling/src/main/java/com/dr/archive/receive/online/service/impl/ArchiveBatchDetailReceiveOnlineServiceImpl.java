package com.dr.archive.receive.online.service.impl;

import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailServiceImpl;
import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.EArchiveBatchService;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.common.dzdacqbc.service.EFileInfoService;
import com.dr.archive.common.dzdacqbc.service.ESaveSpacesService;
import com.dr.archive.common.packet.util.PacketDataParserUtil;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.ImpBatch;
import com.dr.archive.kufang.entityfiles.service.EntityFilesService;
import com.dr.archive.kufang.entityfiles.service.ImpBatchService;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecordInfo;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.template.entity.CompilationTemplate;
import com.dr.archive.manage.template.service.CompilationTemplateService;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnlineInfo;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.service.ArchiveBatchDetailReceiveOnlineService;
import com.dr.archive.receive.online.service.ArchiveOnlineReceiveService;
import com.dr.archive.util.Constants;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.util.ZipUtil;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.file.service.impl.DefaultFileHandler;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.fasterxml.jackson.databind.JavaType;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 导入
 *
 * @author: dr
 * @date: 2021/08/28 1:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveBatchDetailReceiveOnlineServiceImpl extends AbstractArchiveBatchDetailServiceImpl<ArchiveBatchDetailReceiveOnline> implements ArchiveBatchDetailReceiveOnlineService {
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
    /*
     * 实体档案*/
    @Autowired
    EntityFilesService entityFilesService;
    @Autowired
    ImpBatchService impBatchService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    ArchiveOnlineReceiveService archiveOnlineReceiveService;
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


    @Override
    public String getType() {
        return BaseArchiveBatchDetailService.BATCH_TYPE_PRE_ARCHIVE;
    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * 在线接收入库操作
     *
     * @param online
     */
    @Async
    @Override
    public void filling(ArchiveBatchReceiveOnline online) {
        List<ArchiveBatchDetailReceiveOnline> details = selectByBatchId(online.getId());
        Organise organise = SecurityHolder.get().currentOrganise();
        String organiseType = organise.getOrganiseType();
        //推送生成全宗卷信息
        insertManageInfo(online, details, organise);
        //追加入库批次
        EArchiveBatch archiveBatch = insertEarchiveBatch(online);
        //如果是数字化工具进来的数据 加到实体档案库
        ImpBatch impBatch = new ImpBatch();
        if ("INSPUR-DZZW-MACHINING".equals(online.getSystemNum())) {
            impBatch = insertImpBatch(online);
        }

        for (ArchiveBatchDetailReceiveOnline detail : details) {
            dataManager.updateStatus(detail.getFormDataId(), ArchiveDataManager.STATUS_MANAGE, detail.getFormDefinitionId());
            //  添加接收行为的管理过程元数据记录  档案馆 的入库成为移交操作
            if (Constants.ORG_TYPE_DAG.equals(organiseType)) {
                archiveMetadataRecordService.insert(new ArchiveMetadataRecord(detail.getFormDefinitionId(), detail.getFormDataId(), detail.getFondCode(), detail.getCategoryCode(), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_ONLINE_YIJIAO, "在线移交", "在线移交", "在线移交", "", "", "", "", null, online.getCreateDate()));
            } else {
                archiveMetadataRecordService.insert(new ArchiveMetadataRecord(detail.getFormDefinitionId(), detail.getFormDataId(), detail.getFondCode(), detail.getCategoryCode(), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_ONLINE_RECEIVE, "在线归档", "在线归档", "在线归档", "", "", "", "", null, online.getCreateDate()));
            }
            // 添加入库行为的管理过程元数据记录
            archiveMetadataRecordService.insert(new ArchiveMetadataRecord(detail.getFormDefinitionId(), detail.getFormDataId(), detail.getFondCode(), detail.getCategoryCode(), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_INGL, "入库", "入库", "入库", "", "", "", "", null));
            //入库到长期保存系统
            insertEarchive(online, archiveBatch, detail);
            //如果是数字化工具进来的数据 加到实体档案库
            if ("INSPUR-DZZW-MACHINING".equals(online.getSystemNum())) {
                insertEntityFiles(impBatch, detail);
            }
        }
        //TODO 生成交接单据.xml 按批次
    }

    @Override
    public long deleteDetail(String id) {
        ArchiveBatchDetailReceiveOnline detailReceiveOnline = selectById(id);
        long result = deleteById(id);
        //删除目录,原文
        archiveDataManager.deleteFormData(detailReceiveOnline.getCategoryId(), detailReceiveOnline.getFormDefinitionId(), detailReceiveOnline.getFormDataId());
        //更新批次表的归档数据与四性检测信息
        ArchiveBatchReceiveOnline batchReceiveOnline = archiveOnlineReceiveService.selectById(detailReceiveOnline.getBatchId());
        long total = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class, false)
                .count(ArchiveBatchDetailReceiveOnlineInfo.ID)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, detailReceiveOnline.getBatchId()));
        long testSuccessTotal = commonMapper.countByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class, false)
                .count(ArchiveBatchDetailReceiveOnlineInfo.ID)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, detailReceiveOnline.getBatchId())
                .equal(ArchiveBatchDetailReceiveOnlineInfo.TESTSTATUS, TestRecordService.TEST_STATUS_SUCCESS));
        batchReceiveOnline.setDetailNum(total);
        if (total == testSuccessTotal) {
            batchReceiveOnline.setTestStatus(TestRecordService.TEST_STATUS_SUCCESS);
        }
        archiveOnlineReceiveService.updateById(batchReceiveOnline);
        return result;
    }

    /*
     * 根据移交批次创建实体档案导入记录*/
    private ImpBatch insertImpBatch(ArchiveBatchReceiveOnline online) {
        ImpBatch impBatch = new ImpBatch();
        impBatch.setRecordName(online.getBatchName() + "自动入库");
        impBatch.setQuantity(String.valueOf(online.getDetailNum()));
        impBatch.setRemarks("在线接收数字化加工系统");
        impBatch.setId(UUID.randomUUID().toString());
        impBatch.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        impBatchService.insert(impBatch);
        return impBatch;
    }

    /*加到实体档案中*/
    private void insertEntityFiles(ImpBatch impBatch, ArchiveBatchDetailReceiveOnline on) {
        EntityFiles entityFiles = new EntityFiles();
        entityFiles.setArchiveCode(on.getArchiveCode());
        entityFiles.setBatchId(impBatch.getId());
        entityFiles.setTitle(on.getTitle());
        entityFiles.setOrganiseId(SecurityHolder.get() == null ? "" : SecurityHolder.get().currentOrganise().getId());
        Fond fond = fondService.findFondByCode(on.getFondCode());
        entityFiles.setFondId(fond == null ? "" : fond.getId());
        entityFiles.setFondCode(on.getFondCode());
        entityFiles.setFondName(on.getFondName());
        entityFiles.setClassId(on.getCategoryId());
        entityFiles.setClassCode(on.getCategoryCode());
        entityFiles.setClassName(on.getCategoryName());
        entityFilesService.insert(entityFiles);
    }


    /**
     * 根据移交批次创建入库批次
     *
     * @param offline
     * @return
     */
    private EArchiveBatch insertEarchiveBatch(ArchiveBatchReceiveOnline offline) {
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
     * @param batch
     * @param archiveBatch
     * @param on
     */
    private void insertEarchive(ArchiveBatchReceiveOnline batch, EArchiveBatch archiveBatch, ArchiveBatchDetailReceiveOnline on) {

        //添加电子文件长期保存
        EArchive eArchive = new EArchive();
        eArchive.setId(UUIDUtils.getUUID());
        eArchive.setFondCode(on.getFondCode());
        eArchive.setCategoryCode(on.getCategoryCode());
        eArchive.setFormDataId(on.getFormDataId());
        eArchive.setFormDefinitionId(on.getFormDefinitionId());
        eArchive.setOrgCode(on.getOrgCode());
        eArchive.setArchiveCode(on.getArchiveCode());
        eArchive.setTitle(on.getTitle());
        eArchive.setKeyWords(on.getKeyWords());
        eArchive.setNote(on.getNote());
        eArchive.setYear(on.getYear());
        eArchive.setInDate(System.currentTimeMillis());
        eArchive.setInBatchId(archiveBatch.getId());
        eArchive.setInBatchNo(archiveBatch.getBatchName());
        eArchive.setOutCount(0);
        //TODO 更详细的绑定信息
        eArchiveService.insert(eArchive);

        //添加入库详情记录
        EArchiveBatchDetail inDetail = new EArchiveBatchDetail();
        on.cloneArchive(inDetail);
        inDetail.setBatchId(archiveBatch.getId());
        inDetail.setBatchName(archiveBatch.getBatchName());
        inDetail.setArchiveId(eArchive.getId());
        inDetail.setLastTestRecordId(on.getLastTestRecordId());
        CommonService.bindCreateInfo(inDetail);
        commonMapper.insert(inDetail);
        //TODO 原文备份到长期保存库,存放规则根据配置
        putLongTermFile(on, eArchive);


    }

    /*
     * 长期保存入库,原文复制到指定路径下*/
    private void putLongTermFile(ArchiveBatchDetailReceiveOnline on, EArchive eArchive) {
        //查长期保存配置的保存路径
        String path = "";
        SqlQuery query = SqlQuery.from(ESaveSpaces.class)
                .like(ESaveSpacesInfo.SPACENAME, "长期保存库")
                .orderByDesc(ESaveSpacesInfo.CREATEDATE);
        List<ESaveSpaces> eSaveSpacesList = eSaveSpacesService.selectList(query);
        //TODO 暂时写死
        //全宗-门类-年度-保管期限-（机构问题）-档号.zip
        Assert.isTrue(eSaveSpacesList.size() > 0, "无长期保存库存储空间!");
        path = eSaveSpacesList.get(0).getCatalogue() + File.separator + on.getFondCode() +
                File.separator + on.getCategoryCode() +
                File.separator + on.getYear() +
                File.separator + on.getArchiveCode();
        //将原文复制到指定目录下
        List<FileInfo> fileInfoList = commonFileService.list(on.getFormDataId(), "archive", "default");
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
        List<ArchiveMetadataRecord> archiveMetadataRecordList = archiveMetadataRecordService.selectList(SqlQuery.from(ArchiveMetadataRecord.class).equal(ArchiveMetadataRecordInfo.FORMDATAID, on.getFormDataId()));
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
        efileInfo.setFileName(on.getArchiveCode() + ".zip");
        efileInfo.setFilePath(path + ".zip");
        efileInfo.setFormDataId(on.getFormDataId());
        efileInfo.setFormDefinitionId(on.getFormDefinitionId());
        efileInfo.setArchiveId(eArchive.getId());
        efileInfo.setFileSize(zipFile.length());
        efileInfo.setSuffix("zip");
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
     * @param online
     * @param details
     * @param organise
     */
    private void insertManageInfo(ArchiveBatchReceiveOnline online, List<ArchiveBatchDetailReceiveOnline> details, Organise organise) {
        //根据模板生成交接单据
        List<com.dr.archive.manage.template.entity.CompilationTemplate> compilationTemplateList = compilationTemplateService.getCompilationTemplateByCode(ManagementService.SUB_TYPE_CODE_DASJL_DALYHDALSZYGCSMCL);
        if (compilationTemplateList.size() > 0 && compilationTemplateList.get(0).getCompilationContent() != null) {
            //Assert.isTrue(compilationTemplateList.size() > 0, "未配置【" + ManagementService.SUB_TYPE_CODE_DASJL_DALYHDALSZYGCSMCL + "】类型的模板！");
            CompilationTemplate compilationTemplate = compilationTemplateList.get(0);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("offline", online);
            dataMap.put("organise", organise);
            dataMap.put("details", details);
            String html = compilationTemplateService.getHtml(dataMap, compilationTemplate.getCompilationContent());
            Person person = SecurityHolder.get().currentPerson();
            //推送全宗卷数据
            Management management = new Management(
                    UUID.randomUUID().toString(),
                    details.get(0).getFondCode(),
                    ManagementService.SUB_TYPE_CODE_DASJL_DALYHDALSZYGCSMCL,
                    "归档清单" + online.getBatchName(),
                    html,
                    person.getUserName(),
                    DateFormatUtils.format(online.getCreateDate(), "yyyyMMdd"),
                    DateFormatUtils.format(online.getCreateDate(), "yyyy"),
                    DateFormatUtils.format(online.getCreateDate(), "yyyy")
            );
            //TODO 交接单据xml或者excel作为附件
            managementService.insert(management);
        }

    }
}
