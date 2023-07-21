package com.dr.archive.utilization.compilation.service.impl;

import com.dr.archive.managefile.entity.ManageFile;
import com.dr.archive.managefile.entity.ManageFileInfo;
import com.dr.archive.managefile.service.ManageFileService;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.utilization.compilation.entity.CompilationTaskDetail;
import com.dr.archive.utilization.compilation.entity.CompilationTaskDetailInfo;
import com.dr.archive.utilization.compilation.service.CompilationTaskDetailService;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.dr.archive.utilization.compilation.entity.CompilationTaskDetail.*;
import static com.dr.archive.utilization.compilation.service.CompilationTemplateConstants.TYPE_COMPILATION_TEMPLATE_PICTURE;
import static com.dr.archive.utilization.compilation.service.impl.CompilationTaskServiceImpl.imgType;

/**
 * @Author: caor
 * @Date: 2022-08-20 16:54
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CompilationTaskDetailServiceImpl extends DefaultBaseService<CompilationTaskDetail> implements CompilationTaskDetailService {
    @Autowired
    ManageFileService manageFileService;
    @Autowired
    CommonFileService commonFileService;

    //重复性校验
    @Override
    public long insert(CompilationTaskDetail entity) {
        String id = UUIDUtils.getUUID();
        entity.setId(id);
        if (commonMapper.countByQuery(SqlQuery.from(CompilationTaskDetail.class).equal(CompilationTaskDetailInfo.BATCHID, entity.getBatchId()).equal(CompilationTaskDetailInfo.FORMDATAID, entity.getFormDataId())) > 0) {
            return 0;
        }
        //往管理附件表中插入数据,todo 图片类型会插入到个人网盘中，在大事记编研时可能有问题
        if (BUSINESS_TYPE_ARCHIVE.equals(entity.getSourceType())) {
            List<FileInfo> fileInfos = commonFileService.list(entity.getFormDataId(), "archive");
            for (FileInfo fileInfo : fileInfos) {
                if (!TYPE_COMPILATION_TEMPLATE_PICTURE.equals(selectById(entity.getBatchId())) || (TYPE_COMPILATION_TEMPLATE_PICTURE.equals(selectById(entity.getBatchId())) && ArrayUtils.contains(imgType, fileInfo.getSuffix().toUpperCase()))) {
                    ManageFile manageFile = new ManageFile();
                    manageFile.setBatchId(entity.getBatchId());
                    manageFile.setBusinessId(entity.getId());
                    manageFile.setType(entity.getBusinessType());
                    manageFile.setFileInfoId(fileInfo.getId());
                    manageFile.setFileName(fileInfo.getName());
                    manageFile.setFileDescription(fileInfo.getDescription());
                    manageFile.setSuffix(fileInfo.getSuffix());
                    manageFile.setSaveDate(fileInfo.getSaveDate());
                    manageFile.setFormDefinitionId(entity.getFormDefinitionId());
                    manageFileService.insert(manageFile);
                }
            }
        } else if (BUSINESS_TYPE_FONDFILE.equals(entity.getSourceType())) {
            if (StringUtils.hasText(entity.getFormDataId())) {
                List<ManageFile> fondManageFiles = manageFileService.selectList(SqlQuery.from(ManageFile.class).equal(ManageFileInfo.BUSINESSID, entity.getFormDataId()));
                for (ManageFile fondManageFile : fondManageFiles) {
                    insertCompilationTaskManageFile(entity, fondManageFile);
                }
            }
        } else if (BUSINESS_TYPE_PERSONFILE.equals(entity.getSourceType())) {
            if (StringUtils.hasText(entity.getFormDataId())) {
                ManageFile old = new ManageFile();
                FileInfo fileInfo = commonFileService.fileInfo(entity.getFormDataId());
                old.setFileInfoId(fileInfo.getId());
                old.setFileName(fileInfo.getName());
                old.setFileDescription(fileInfo.getDescription());
                old.setSuffix(fileInfo.getSuffix());
                old.setSaveDate(fileInfo.getSaveDate());
                insertCompilationTaskManageFile(entity, old);
            }
        }
        return super.insert(entity);
    }

    void insertCompilationTaskManageFile(CompilationTaskDetail entity, ManageFile personManageFile) {
        ManageFile manageFile = new ManageFile();
        manageFile.setId(UUIDUtils.getUUID());
        manageFile.setBatchId(entity.getBatchId());
        manageFile.setBusinessId(entity.getId());
        manageFile.setType(entity.getBusinessType());
        manageFile.setFileInfoId(personManageFile.getFileInfoId());
        manageFile.setFileName(personManageFile.getFileName());
        manageFile.setFormDefinitionId(entity.getFormDefinitionId());
        manageFile.setFileDescription(personManageFile.getFileDescription());
        manageFile.setSuffix(personManageFile.getSuffix());
        manageFile.setSaveDate(System.currentTimeMillis());
        manageFile.setUpdateDate(System.currentTimeMillis());
        Person person = SecurityHolder.get().currentPerson();
        manageFile.setCreatePerson(person.getId());
        manageFile.setUpdatePerson(person.getId());
        commonMapper.insert(manageFile);
    }
}
