package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.*;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.transfer.bo.EArchiveBaseInfo;
import com.dr.archive.util.ZipUtil;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ESaveBackBatchDetailServiceImpl extends DefaultBaseService<ESaveBackBatchDetail> implements ESaveBackBatchDetailService, InitializingBean {
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    ESaveSpacesService eSaveSpacesService;
    @Autowired
    ESaveMediumService saveMediumService;
    @Autowired
    ESaveOffLineService eSaveOffLineService;

    @Autowired
    MappingJackson2XmlHttpMessageConverter converter;
    protected ObjectMapper xmlObjectMapper;

    @Autowired
    protected ArchiveDataManager dataManager;
    @Autowired
    EFileInfoService eFileInfoService;
    @Autowired
    protected FileInfoHandler fileInfoHandler;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    protected ArchiveDataManager archiveDataManager;
    @Autowired
    ESaveRestoreRecordService eSaveRestoreRecordService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected FondService fondService;

    /**
     * 添加
     *
     * @param batchId
     * @param earchiveId
     */
    @Override
    public void addDetail(String batchId, String earchiveId, String spaceId, String newPath) {
        ESaveBackBatchDetail detail = new ESaveBackBatchDetail();
        detail.setBatchId(batchId);
        detail = common(earchiveId, detail);
        ESaveSpaces spaces = eSaveSpacesService.selectById(spaceId);
        detail.setSpaceId(spaces.getId());
        detail.setBackPath(newPath);
        detail.setBackType("jx");
        detail.setIsExpire("0");//未过期
        detail.setBackCount(0);
        detail.setOrgId(SecurityHolder.get().currentOrganise().getId());
        insert(detail);
    }

    public ESaveBackBatchDetail common(String earchiveId, ESaveBackBatchDetail detail) {
        EArchive eArchive = eArchiveService.selectById(earchiveId);
        if (eArchive != null) {
            detail.setArchiveCode(eArchive.getArchiveCode());
            detail.setArchiveId(eArchive.getFormDataId());
            detail.setCategoryCode(eArchive.getCategoryCode());
            Fond fond = fondService.findFondByCode(eArchive.getFondCode());
            Category category = categoryService.findCategoryByCode(eArchive.getCategoryCode(), fond.getId());
            detail.setArchiveType(category.getArchiveType());
            detail.setFond_code(eArchive.getFondCode());
            detail.setNd(eArchive.getYear());
            detail.setFormDefinitionId(eArchive.getFormDefinitionId());
            detail.setTiming(eArchive.getTitle());
        }
        return detail;
    }

    /**
     * 离线备份详情
     */
    @Override
    public void addDetailLX(String offLineId) {
        ESaveOffLine offLine = eSaveOffLineService.selectById(offLineId);

        SqlQuery<EArchive> sqlQuery = SqlQuery.from(EArchive.class).equal(EArchiveInfo.FONDCODE, offLine.getFond_code())
                .equal(EArchiveInfo.CATEGORYCODE, offLine.getClassCode()).equal(EArchiveInfo.YEAR, offLine.getNd());
        List<EArchive> list = commonMapper.selectByQuery(sqlQuery);
        for (EArchive eArchive : list) {
            ESaveBackBatchDetail detail = new ESaveBackBatchDetail();
            detail.setBatchId(offLineId);
            detail = common(eArchive.getId(), detail);
            ESaveMedium medium = saveMediumService.selectById(offLine.getMediumId());
            detail.setBackPath(medium.getMediumName());
            detail.setMediumId(offLine.getMediumId());
            detail.setBackType("lx");
            detail.setOrgId(SecurityHolder.get().currentOrganise().getId());
            insert(detail);
        }
    }

    /**
     * 备份数据恢复
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dataRecovery(String detailId, String type) {
        //1、找到备份的zip包 2、解压zip  3、解析元数据xml  4、更新长期保存数据   5、更新正式库数据
        //6、添加恢复记录  7、处理备份详情的数据
        ESaveBackBatchDetail detail = selectById(detailId);
        String zipPath = detail.getBackPath();
        String outZipPath = zipPath.substring(0, zipPath.lastIndexOf(".zip"));
        try {
            int end = outZipPath.lastIndexOf(File.separator);
//            ZipUtil.zipContraMultiFile(zipPath, outZipPath.substring(0, end));
            ZipUtil.unZipFiles(zipPath, outZipPath.substring(0, end));
            //元数据.xml的位置
            String xmlPath = outZipPath + File.separator + "基本信息元数据.xml";
            File localFile = new File(xmlPath);
            if (localFile.exists()) {
                EArchiveBaseInfo eArchiveBaseInfo = xmlObjectMapper.readValue(localFile, EArchiveBaseInfo.class);
                FormData data = archiveDataManager.selectOneFormData(detail.getFormDefinitionId(), detail.getArchiveId());
                //基础信息
                data.put(AbstractArchiveEntity.COLUMN_SAVE_TERM, eArchiveBaseInfo.getSave_term());
                data.put(AbstractArchiveEntity.COLUMN_TITLE, eArchiveBaseInfo.getTitle());
                data.put(AbstractArchiveEntity.COLUMN_ORGANISEID, eArchiveBaseInfo.getOrganiseid());
                data.put(AbstractArchiveEntity.COLUMN_FOND_CODE, eArchiveBaseInfo.getFond_code());
                data.put(AbstractArchiveEntity.COLUMN_CATEGORY_CODE, eArchiveBaseInfo.getCate_gory_code());
                data.put(AbstractArchiveEntity.COLUMN_SOURCE_TYPE, eArchiveBaseInfo.getSource_type());
                data.put(AbstractArchiveEntity.COLUMN_FILETIME, eArchiveBaseInfo.getFiletime());
                data.put(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE, eArchiveBaseInfo.getArchive_code());
                data.put(AbstractArchiveEntity.COLUMN_YEAR, eArchiveBaseInfo.getVintages());
                data.put(AbstractArchiveEntity.COLUMN_STATUS, eArchiveBaseInfo.getStatus_info());
                data.put(ArchiveEntity.ID_COLUMN_NAME,detail.getArchiveId());
                String fondCode = eArchiveBaseInfo.getFond_code();
                Fond fond = fondService.findFondByCode(fondCode);
                String cateGoryCode = eArchiveBaseInfo.getCate_gory_code();
                Category category = categoryService.findCategoryByCode(cateGoryCode, fond.getId());
                //更新表单数据
                dataManager.updateFormData(data, fond.getId(), category.getId());
                //查询是否有原文，没有则进行挂接原文
                List<FileInfo> fileInfoList = commonFileService.list(data.getId(), "archive", "default");
                if (fileInfoList.size() == 0) {
                    List<EArchiveBaseInfo.File> files = eArchiveBaseInfo.getFiles();
                    for (EArchiveBaseInfo.File file : files) {
                        String filePath = outZipPath + File.separator + "原文" + File.separator + file.getFile_actual_name();
                        doHook(data.getId(), filePath);
                    }
                }

                //更新长期保存数据 更新zip包
                EArchive archive = eArchiveService.selectOne(SqlQuery.from(EArchive.class).equal(EArchiveInfo.FORMDATAID, detail.getArchiveId()));
                eArchiveService.dealEArchive(archive, data);//处理长期保存库中档案数据
                eArchiveService.updateById(archive);
                EFileInfo fileInfo = eFileInfoService.selectOne(SqlQuery.from(EFileInfo.class).equal(EFileInfoInfo.ARCHIVEID, archive.getId()));
                File zipFile = new File(zipPath);
                File eFile = new File(fileInfo.getFilePath());
                //复制备份的压缩包到长期保存存储路径下，并更新efileinfo表的数据
                if (eFile.exists()) {
                    eFile.delete();
                    copyFile(zipFile, fileInfo.getFilePath());
                    String fileHash = this.fileInfoHandler.fileHash(new FileInputStream(zipFile));
                    Assert.isTrue(!StringUtils.isEmpty(fileHash), "文件hash值不能为空！");
                    fileInfo.setDigest(fileHash);
                    fileInfo.setFileSize(zipFile.length());
                    eFileInfoService.updateById(fileInfo);
                }
                //删除临时文件夹
                FileUtils.deleteDirectory(new File(outZipPath));

                detail.setBackCount(detail.getBackCount() + 1);
                detail.setUpdateDate(System.currentTimeMillis());
                updateById(detail);
                //添加恢复记录
                eSaveRestoreRecordService.addRecord(detail.getBatchId(), detail.getId(), type);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void copyFile(File source, String toPath) throws IOException {
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

    /**
     * 挂接原文
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

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        xmlObjectMapper = converter.getObjectMapper();
    }
}
