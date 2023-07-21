package com.dr.archive.common.pdfconversion.impl;

import com.dr.archive.common.pdfconversion.entity.FileConversionRecording;
import com.dr.archive.common.pdfconversion.service.FileConversionRecordingService;
import com.dr.archive.common.pdfconversion.service.FormatConversionService;
import com.dr.archive.common.pdfconversion.service.PdfConversionNew;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.file.BaseFile;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;


/**
 * @author caor
 * @Date 2020-11-02 16:42
 */
@Service
public class FormatConversionServiceImpl implements FormatConversionService {
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    protected OrganisePersonService organisePersonService;
    @Autowired
    protected CommonFileConfig fileConfig;
    @Autowired
    List<PdfConversionNew> supportMediaTypes;
    @Autowired
    FileConversionRecordingService fileConversionService;

    @Override
//    @Async
    @Transactional(rollbackFor = Exception.class)
    public String fileToPdf(String fileId) {
        //TODO 新增时有可能获取不到人员
        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
        SecurityHolder.set(securityHolder);
        //获取文件基本信息
        FileInfo fileInfo = commonFileService.fileInfo(fileId);
        FileConversionRecording fileConversionRecording = new FileConversionRecording();
        //获取原文件名称
        String fileName = fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf("."));
        fileConversionRecording.setFileName(fileName);
        fileConversionRecording.setFileSuffix(fileInfo.getSuffix());
        fileConversionRecording.setAfterFileSuffix("pdf");
        fileConversionRecording.setFileSize(fileInfo.getFileSize());
        fileConversionRecording.setFileUrl(buildFilePath(fileInfo));
        fileConversionRecording.setStatus("0");
        fileConversionService.insert(fileConversionRecording);
        //获取临时文件夹路径
        String tempFilePath = commonFileConfig.getFullDirPathWithCurrentDate("temp", fileInfo.getMimeType());
        //创建临时文件名称
        File tempFile = new File(tempFilePath, fileId + ".pdf");

        try (InputStream inputStream = commonFileService.fileStream(fileId);
             OutputStream fop = new FileOutputStream(tempFile)) {
            MediaType mediaType = MediaType.parseMediaType(fileInfo.getMimeType());
            for (PdfConversionNew conversion : supportMediaTypes) {
                if (conversion.accept(mediaType)) {
                    conversion.conversion(inputStream, fop);
                    break;
                }
            }
            fop.close();
            //如果转换成功
            if (tempFile.exists()) {
                FileInfo fileInfo1 = commonFileService.addFile(new FileSystemFileResource(tempFile) {
                    @Override
                    public String getName() {
                        return fileName + ".pdf";
                    }
                }, fileInfo.getRefId(), fileInfo.getRefType(), fileInfo.getGroupCode());
                //修改原文件信息，做到删除的效果
                commonFileService.addFile(fileInfo.getFileHash(), fileInfo.getRefId(), fileInfo.getRefType() + "_bak");
                //删除原文件
                commonFileService.removeFile(fileInfo.getId());
                //添加成功之后，文件会被commonfile管理，可以删除临时文件
                tempFile.delete();
                fileConversionRecording.setAfterFileSuffix(fileInfo1.getSuffix());
                fileConversionRecording.setAfterFileUrl(buildFilePath(fileInfo1));
                fileConversionRecording.setAfterFileSize(fileInfo1.getFileSize());
                fileConversionRecording.setStatus("1");
                fileConversionService.updateById(fileConversionRecording);
                return "正在执行转换操作，请稍后刷新原文列表！";
            } else {
                fileConversionRecording.setStatusContent("当前格式暂不支持转换！");
                fileConversionRecording.setStatus("0");
                fileConversionService.updateById(fileConversionRecording);
                return "当前格式暂不支持转换！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileConversionRecording.setStatusContent("转换失败！");
            fileConversionRecording.setStatus("0");
            fileConversionService.updateById(fileConversionRecording);
            return "转换失败";
        }
    }


    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public String batchFileToPdf(String fileIds) {
        Assert.isTrue(StringUtils.hasText(fileIds),"fileIds不能为空");
        String[] split = fileIds.split(",");
        String message ="";
        for (String s : split) {
            message+=fileToPdf(s);
        }
        return message;
    }

    protected String  buildFilePath(BaseFile fileInfo) {
        Date date = new Date(fileInfo.getSaveDate());
        String mineType = StringUtils.isEmpty(fileInfo.getSuffix()) ? Constants.DEFAULT : fileInfo.getSuffix();
        return String.join(
                File.separator,
                fileConfig.getFullDirPath(null, mineType, date),
                fileInfo.getBaseFileId() + "." + fileInfo.getSuffix()
        );
    }

}
