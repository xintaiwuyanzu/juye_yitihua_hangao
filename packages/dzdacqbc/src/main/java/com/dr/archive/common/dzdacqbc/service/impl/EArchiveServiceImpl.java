package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.bo.TransferInfo;
import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.*;
import com.dr.archive.common.dzdacqbc.utils.DataToXml;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataContext;
import com.dr.archive.manage.form.service.ArchiveDataPlugin;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.util.ZipUtil;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.file.service.impl.DefaultFileHandler;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.jdbc.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 电子档案service
 *
 * @author dr
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EArchiveServiceImpl extends DefaultBaseService<EArchive> implements EArchiveService, ArchiveDataPlugin {
    @Autowired
    ESaveSpacesService eSaveSpacesService;
    @Autowired
    MappingJackson2XmlHttpMessageConverter converter;
    ObjectMapper objectMapper;
    Map<String, TransferInfo> batchMap = Collections.synchronizedMap(new LinkedHashMap<String, TransferInfo>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, TransferInfo> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    });

    @Autowired
    FormDataService formDataService;
    @Autowired
    ArchiveMetadataRecordService archiveMetadataRecordService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    DefaultFileHandler defaultFileHandler;
    @Autowired
    EFileInfoService eFileInfoService;
    @Autowired
    protected FileInfoHandler fileInfoHandler;
    @Autowired
    ArchiveFormDefinitionService archiveFormService;
    @Autowired
    CommonFileConfig commonFileConfig;

    //    @Autowired
//    TestRecordService testRecordService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;



    @Override
    public void addEArchive(ArchiveDataQuery query) {
        List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
        //添加入库批次
        EArchiveBatch archiveBatch = insertEArchiveBatch(formDataList.size());
        for (FormData formData : formDataList) {
            // 添加入库行为的管理过程元数据记录
            archiveMetadataRecordService.insert(new ArchiveMetadataRecord(formData.getFormDefinitionId(), formData.getId(), formData.get(ArchiveEntity.COLUMN_FOND_CODE),
                    formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_INGL, "入库", "入库", "入库",
                    "", "", "", "", null));
            //入库到长期保存系统
            insertEarchiveAll(archiveBatch, formData, "", "");
        }
    }

    @Override
    public void addEArchiveOne(FormData formData, String modelType, String ajh) {
        EArchive eArchive = selectOne(SqlQuery.from(EArchive.class).equal(EArchiveInfo.FORMDATAID, formData.getId()));
        if(eArchive == null){
            //添加入库批次
            EArchiveBatch archiveBatch = insertEArchiveBatch(1);
            // 添加入库行为的管理过程元数据记录
            archiveMetadataRecordService.insert(new ArchiveMetadataRecord(formData.getFormDefinitionId(), formData.getId(), formData.get(ArchiveEntity.COLUMN_FOND_CODE),
                    formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_INGL, "入库", "入库", "入库",
                    "", "", "", "", null));
            //入库到长期保存系统
            insertEarchiveAll(archiveBatch, formData, modelType, ajh);
        }
    }


    public EArchiveBatch insertEArchiveBatch(long num) {
        EArchiveBatch batch = new EArchiveBatch();
        batch.setId(UUIDUtils.getUUID());
        batch.setCreateDate(System.currentTimeMillis());
        batch.setBatchName("自动入库");
        batch.setBatchType(EArchiveBatchService.BATCH_TYPE_IN);
        batch.setStartDate(System.currentTimeMillis());
        batch.setDetailNum(num);
        batch.setBeizhu("归档入库");
//        batch.setOrgId();
        commonMapper.insertIgnoreNull(batch);
        return batch;
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void insertEarchiveAll(EArchiveBatch archiveBatch, FormData formData, String modelType, String ajdh) {
        //添加电子文件长期保存
        EArchive eArchive = new EArchive();
        eArchive.setId(UUIDUtils.getUUID());
        eArchive = dealEArchive(eArchive, formData);
        eArchive.setInDate(System.currentTimeMillis());
        eArchive.setInBatchId(archiveBatch.getId());
        eArchive.setInBatchNo(archiveBatch.getBatchName());
        eArchive.setOutCount(0);

        eArchive.setModelType(modelType);//档案类型
        eArchive.setAjdh(ajdh);
        if(!StringUtils.isEmpty(ajdh)){
            eArchive.setIsJNWJ("1");
        }

        addEArchiveBatchDetail(archiveBatch, eArchive);

        // 原文备份到长期保存库,存放规则根据配置
        putLongTermFile1(eArchive, formData);
        //TODO 更详细的绑定信息
        commonMapper.insert(eArchive);
    }

    /**
     * 统一的插入数据
     */
    @Override
    public EArchive dealEArchive(EArchive eArchive, FormData formData) {
        eArchive.setArchiveId(formData.getId());
        eArchive.setFondCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        eArchive.setCategoryCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE));
        eArchive.setFormDataId(formData.getId());
        eArchive.setORGANISEID(formData.get(ArchiveEntity.COLUMN_ORGANISEID));
        eArchive.setFormDefinitionId(formData.getFormDefinitionId());
//        eArchive.setOrgCode();
        eArchive.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        eArchive.setTitle(formData.get(ArchiveEntity.COLUMN_TITLE));
        eArchive.setKeyWords(formData.get(ArchiveEntity.COLUMN_KEY_WORDS));
        eArchive.setNote(formData.get(ArchiveEntity.COLUMN_NOTE));
        eArchive.setYear(formData.get(ArchiveEntity.COLUMN_YEAR));
        eArchive.setFileTime(formData.get(ArchiveEntity.COLUMN_FILETIME));
        return eArchive;
    }


    private void addEArchiveBatchDetail(EArchiveBatch archiveBatch, EArchive eArchive) {
        //添加入库详情记录
        EArchiveBatchDetail inDetail = new EArchiveBatchDetail();
        inDetail.setBatchId(archiveBatch.getId());
        inDetail.setBatchName(archiveBatch.getBatchName());
        inDetail.setArchiveId(eArchive.getId());
//        inDetail.setLastTestRecordId();
        CommonService.bindCreateInfo(inDetail);

        inDetail.setFondCode(eArchive.getFondCode());
        inDetail.setCategoryCode(eArchive.getCatalogueCode());
        inDetail.setFormDataId(eArchive.getFormDataId());
        inDetail.setFormDefinitionId(eArchive.getFormDefinitionId());
        inDetail.setArchiveCode(eArchive.getArchiveCode());
        inDetail.setTitle(eArchive.getTitle());
        inDetail.setKeyWords(eArchive.getKeyWords());
        inDetail.setNote(eArchive.getNote());
        inDetail.setYear(eArchive.getYear());

        commonMapper.insert(inDetail);
    }

    /***
     * 长期保存入库,原文复制到指定路径下
     */
    private void putLongTermFile1(EArchive eArchive, FormData formData) {
        //查长期保存配置的保存路径
        String path = "";
        //根据全宗号和门类号查分类表数据
        List<CqbcClassification> cqbcClassifications = commonMapper.selectByQuery(SqlQuery.from(CqbcClassification.class)
                .equal(CqbcClassificationInfo.FONDNO, eArchive.getFondCode())
                .equal(CqbcClassificationInfo.CLASSCODE, eArchive.getCategoryCode()));
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
        path = saveSpaces.getCatalogue() + File.separator + eArchive.getFondCode() +
                File.separator + eArchive.getCategoryCode() +
                File.separator + eArchive.getYear() +
                File.separator + eArchive.getArchiveCode();

        //先创建文件夹
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //1、再将原文复制到指定目录下
        List<FileInfo> fileInfoList = commonFileService.list(eArchive.getFormDataId(), "archive", "default");
        //获取临时文件夹路径
        String tempFilePath = saveSpaces.getCatalogue() + File.separator + "temp" + File.separator + eArchive.getArchiveCode();
        for (FileInfo fileInfo : fileInfoList) {
            String filepath = tempFilePath + File.separator + fileInfo.getName();
            try {
                defaultFileHandler.copyTo(fileInfo, filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //2、先添加管理过程元数据
        List<FormField> formFieldList = archiveFormService.findFieldList(eArchive.getFormDefinitionId());
        DataToXml.packetXml(formData, formFieldList, tempFilePath, path + File.separator + "原文", path);

        try {
            //3、删除临时数据
            FileUtils.deleteDirectory(new File(tempFilePath));
            //4、最后打成zip
            ZipUtil.toZip(path, path + ".zip", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File zipFile = new File(path + ".zip");
        //长期保存文件信息
        EFileInfo efileInfo = new EFileInfo();
        efileInfo.setFileName(eArchive.getArchiveCode() + ".zip");
        efileInfo.setFilePath(path + ".zip");
        efileInfo.setFormDataId(eArchive.getFormDataId());
        efileInfo.setFormDefinitionId(eArchive.getFormDefinitionId());
        efileInfo.setArchiveId(eArchive.getId());
        efileInfo.setFileSize(zipFile.length());
        efileInfo.setSuffix("zip");

        eArchive.setSpaceId(saveSpaces.getId());
        eArchive.setClassificationId(cqbcClassifications.get(0).getId());
        eArchive.setLastBackupsDate(System.currentTimeMillis());
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

    private static void copyFile(File source, String toPath) throws IOException {
        try {
            File file = new File(toPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            Files.copy(source.toPath(), Paths.get(toPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFile1(File source, String toPath) throws IOException {
        try {
            File file = new File(toPath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            Files.copy(source.toPath(), Paths.get(toPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public long countArchive() {
        return commonMapper.countByQuery(
                SqlQuery.from(EArchive.class));
    }

    @Override
    public File buildArchiveDir(EArchive eArchive) {

        return null;
    }

    @Override
    public EArchiveDetectTaskDetail detectEArchive(EArchive eArchive) {
        EArchiveDetectTaskDetail detail = new EArchiveDetectTaskDetail();
        detail.setArchiveCode(eArchive.getArchiveCode());
        detail.setArchiveId(eArchive.getId());
        detail.setArchiveTitle(eArchive.getTitle());
        //根目录
        File rootDir = eSaveSpacesService.buildRootDir(eArchive.getSpaceId());
        //移交目录
        //TransferInfo.TransferDirectory directory = selectDirectory(rootDir, eArchive);
        //数据包文件
        CqbcClassification cqbcClassification = commonMapper.selectById(CqbcClassification.class, eArchive.getClassificationId());
        File saveFile = new File(String.join(File.separator, rootDir.getPath(), eArchive.getFondCode(), eArchive.getCategoryCode(), eArchive.getYear(), eArchive.getArchiveCode(), eArchive.getArchiveCode() + ".zip"));

        List<EFileInfo> eFileInfos = commonMapper.selectByQuery(SqlQuery.from(EFileInfo.class).equal(EFileInfoInfo.EARCHIVEID, eArchive.getId()));
        eArchive.setArchiveStatus("0");
        String name = eArchive.getArchiveCode() + "电子文件被篡改：数据包文件不存在";
        for (EFileInfo eFileInfo : eFileInfos) {
            File eFile = new File(eFileInfo.getFilePath());
            if (!eFile.exists()) {
                detail.setStatus(EArchiveDetectTaskService.DETAIL_STATUS_FAIL);
                eArchive.setArchiveStatus("1");
                name = name + "(" + eFileInfo.getFileName() + ")";
            }
//        File saveFile = new File(String.join(File.separator, rootDir.getPath(), eArchive.getFondCode(), eArchive.getCategoryCode(), eArchive.getYear(), eArchive.getArchiveCode(), eArchive.getArchiveCode() + ".zip"));
//        if (saveFile.exists()) {
//            //TODO 需要计算文件大小和摘要
//            detail.setDescription("检测成功");
//            detail.setStatus(EArchiveDetectTaskService.DETAIL_STATUS_SUCCESS);
//        } else {
//            detail.setDescription(eArchive.getArchiveCode() + "电子文件被篡改：数据包文件不存在");
//            detail.setStatus(EArchiveDetectTaskService.DETAIL_STATUS_FAIL);
//            eArchive.setArchiveStatus("1");
//            cqbcClassification.setProblemNum(cqbcClassification.getProblemNum()+1);
//        }
        }
        if ("0".equals(eArchive.getArchiveStatus())) {
            detail.setDescription("检测成功");
        } else {
            detail.setDescription(name);
            cqbcClassification.setProblemNum(cqbcClassification.getProblemNum() + 1);
        }
        eArchive.setTestingNum(eArchive.getTestingNum() + 1);
        eArchive.setLastTestDate(System.currentTimeMillis());
        commonMapper.updateIgnoreNullById(cqbcClassification);
        commonMapper.updateIgnoreNullById(eArchive);
        return detail;
    }

    /**
     * 根据电子文件和目录查询电子文件交接单据中的交接信息
     *
     * @param rootDir
     * @param eArchive
     * @return
     */
    private TransferInfo.TransferDirectory selectDirectory(File rootDir, EArchive eArchive) {
        TransferInfo transferInfo = batchMap.get(eArchive.getInBatchId());
        if (transferInfo == null) {
            synchronized (batchMap) {
                File batchDir = new File(rootDir, BATCH_DIR_NAME);
                //交接文件路径
                File batchFile = new File(batchDir, eArchive.getInBatchNo() + ".xml");
                if (batchFile.exists()) {
                    try {
                        transferInfo = objectMapper.readValue(batchFile, TransferInfo.class);
                        batchMap.put(eArchive.getInBatchId(), transferInfo);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if (transferInfo != null) {
            for (TransferInfo.TransferDirectory directory : transferInfo.getDirectories()) {
                if (directory.getDocumentNumber().equals(eArchive.getArchiveCode())) {
                    return directory;
                }
            }
        }
        return null;
    }

    @Override
    public long countByClass(String classId) {
        return count(SqlQuery.from(EArchive.class)
                .equal(EArchiveInfo.CLASSIFICATIONID, classId));
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        objectMapper = converter.getObjectMapper();
    }

    @Override
    public Long afterDelete(String archiveIds, ArchiveDataContext context) {
        String[] ids = archiveIds.split(",");
        for (String id : ids) {
            //删除长期保存库中的数据
            delete(SqlQuery.from(EArchive.class).equal(EArchiveInfo.ARCHIVEID, id));
        }
        return ArchiveDataPlugin.super.afterDelete(archiveIds, context);
    }

    /**
     * 档案退回至临时库
     */
    @Override
    public void backArchives(ArchiveDataQuery query) {
        List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
        //添加出库批次
        EArchiveBatch archiveBatch = insertBackEArchiveBatch(formDataList.size());
        for (FormData formData : formDataList) {
            // 添加出库行为的管理过程元数据记录
            archiveMetadataRecordService.insert(new ArchiveMetadataRecord(formData.getFormDefinitionId(), formData.getId(), formData.get(ArchiveEntity.COLUMN_FOND_CODE),
                    formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_OUTGL, "出库", "出库", "出库",
                    "", "", "", "", null));
            //修改长期保存库数据
            changeEarchive(formData);
        }
    }

    /**
     * 获得卷内文件
     * @param formData
     * @return
     */
    public List<FormData> getWJS(FormData formData){
        Fond fond = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
        if("1".equals(category.getArchiveType())){//案卷类型
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

    public EArchiveBatch insertBackEArchiveBatch(long num) {
        EArchiveBatch batch = new EArchiveBatch();
        batch.setId(UUIDUtils.getUUID());
        batch.setCreateDate(System.currentTimeMillis());
        batch.setBatchName("出库");
        batch.setBatchType(EArchiveBatchService.BATCH_TYPE_OUT);
        batch.setStartDate(System.currentTimeMillis());
        batch.setDetailNum(num);
        batch.setBeizhu("退回临时库");
        commonMapper.insertIgnoreNull(batch);
        return batch;
    }

    public void changeEarchive(FormData formData) {
        //删除长期保存库中的数据
        SqlQuery<EArchive> sqlQuery = SqlQuery.from(EArchive.class).equal(EArchiveInfo.FORMDATAID, formData.getId())
                .equal(EArchiveInfo.FORMDEFINITIONID, formData.getFormDefinitionId());
        List<EArchive> list = selectList(sqlQuery);
        for (EArchive eArchive : list) {

            SqlQuery<EFileInfo> fileSql = SqlQuery.from(EFileInfo.class).equal(EFileInfoInfo.ARCHIVEID, eArchive.getId());
            List<EFileInfo> files = eFileInfoService.selectList(fileSql);
            for (EFileInfo file : files) {
                //删除打包的数据
                File eFile = new File(file.getFilePath());
                if (eFile.exists()) {
                    eFile.delete();
                }
                //删除efileInfo中的数据
                eFileInfoService.deleteById(file.getId());
            }
            //删除长期保存库数据
            deleteById(eArchive.getId());
        }
    }


}
