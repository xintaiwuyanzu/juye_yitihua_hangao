package com.dr.archive.common.dzdacqbc.eventListener;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.AlarmService;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.common.dzdacqbc.service.EFileInfoService;
import com.dr.archive.common.dzdacqbc.utils.DataToXml;
import com.dr.archive.event.ArchiveDataAddEvent;
import com.dr.archive.event.ArchiveDataDeleteEvent;
import com.dr.archive.event.ArchiveDataEditEvent;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.util.ZipUtil;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.file.service.impl.DefaultFileHandler;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 监听档案数据修改
 */
@Component
public class ArchiveChangeDataEventListener implements ArchiveChangeDataListener {

    Logger logger = LoggerFactory.getLogger(ArchiveChangeDataEventListener.class);
    @Autowired
    ArchiveFormDefinitionService archiveFormService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    DefaultFileHandler defaultFileHandler;
    @Autowired
    EFileInfoService eFileInfoService;
    @Autowired
    protected FileInfoHandler fileInfoHandler;

    @Async
    @Override
    @EventListener(value = ArchiveDataEditEvent.class)
    public void ArchiveDataEdit(ArchiveDataEditEvent archiveDataEditEvent) {
        try {
            FormData formData = archiveDataEditEvent.getData();
            String status = formData.get(ArchiveEntity.STATUS_COLUMN_KEY);
            String status1 = formData.get(ArchiveEntity.COLUMN_STATUS);
            if ("MANAGE".equals(status.toUpperCase()) || "MANAGE".equals(status1.toUpperCase())) {//只修改正式库的数据
                //重新修改长期保存库的元数据，并重新打包
                EArchive eArchive = commonMapper.selectOneByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.ARCHIVEID, formData.getId()));
                if (eArchive != null) {
                    //处理长期保存库中的档案数据
                    eArchive = eArchiveService.dealEArchive(eArchive, formData);
                    commonMapper.updateIgnoreNullById(eArchive);
                    //处理压缩包
                    changeArchive(eArchive, formData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changeArchive(EArchive eArchive, FormData formData) {
        //档案发生调整时，先判断是否已经有打好的包，有的话删除，没有的话重新建

        //调整管理过程元数据
        String path = "";
        EFileInfo eFileInfo = commonMapper.selectOneByQuery(SqlQuery.from(EFileInfo.class).equal(EFileInfoInfo.ARCHIVEID, eArchive.getId()));
        if (eFileInfo != null) {
            try {
                File file = new File(eFileInfo.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
                //创建一个新的文件夹
                int len = eFileInfo.getFilePath().lastIndexOf(".zip");
                path = eFileInfo.getFilePath().substring(0, len);
                File fileNew = new File(path);
                if (!fileNew.exists()) {
                    fileNew.mkdirs();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //1、再将原文复制到指定目录下
            List<FileInfo> fileInfoList = commonFileService.list(eArchive.getFormDataId(), "archive", "default");
            //获取临时文件夹路径
            ESaveSpaces spaces = commonMapper.selectOneByQuery(SqlQuery.from(ESaveSpaces.class).equal(ESaveSpacesInfo.ID, eArchive.getSpaceId()));
            String tempFilePath = spaces.getCatalogue() + File.separator + "temp" + File.separator + eArchive.getArchiveCode();
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
                //3、删除临时文件夹中数据
                FileUtils.deleteDirectory(new File(tempFilePath));
                //4、最后打成zip
                ZipUtil.toZip(path, path + ".zip", true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            File zipFile = new File(path + ".zip");
            //长期保存文件信息
            eFileInfo.setFilePath(path + ".zip");
            eFileInfo.setFileSize(zipFile.length());
            //文件摘要,取文件hash sha512
            try {
                String fileHash = this.fileInfoHandler.fileHash(new FileInputStream(zipFile));
                Assert.isTrue(!StringUtils.isEmpty(fileHash), "文件hash值不能为空！");
                eFileInfo.setDigest(fileHash);
            } catch (Exception e) {
                e.printStackTrace();
            }
            eFileInfoService.updateById(eFileInfo);
            //添加报警信息
            addAlarm(eFileInfo, eArchive);
        }

    }

    public void addAlarm(EFileInfo efile, EArchive eArchive) {
        CqbcAlarm cqbcAlarm = new CqbcAlarm();
        cqbcAlarm.setId(UUIDUtils.getUUID());
        cqbcAlarm.setCreateDate(System.currentTimeMillis());
        cqbcAlarm.setAlarmType(AlarmService.ALARM_TYPE_ADJUST);
        cqbcAlarm.setAlarmContent(efile.getFileName() + "发生调整");
        cqbcAlarm.setFileId(efile.getId());
        cqbcAlarm.setArchiveId(efile.getArchiveId());
        cqbcAlarm.setAlarmDate(System.currentTimeMillis());
        cqbcAlarm.setProcessState("0");
        cqbcAlarm.setFondCode(eArchive.getFondCode());
        commonMapper.insertIgnoreNull(cqbcAlarm);
    }


    @Async
    @Override
    @EventListener(value = ArchiveDataDeleteEvent.class)
    public void removeFormData(ArchiveDataDeleteEvent event) {
        try {

        } catch (Exception e) {
            logger.warn("删除索引数据失败", e);
        }
    }

    /**
     * 表单数据添加
     *
     * @param event
     */
    @Async
    @Override
    @EventListener
    public void addFormData(ArchiveDataAddEvent event) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
