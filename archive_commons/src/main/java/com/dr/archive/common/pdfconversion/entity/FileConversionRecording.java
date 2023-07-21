package com.dr.archive.common.pdfconversion.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

@Table(name = Constants.COMMON_TABLE_PREFIX + "FILE_CONVERSION_RECORDING", comment = "文件转换记录表", module = Constants.COMMON_MODULE_NAME)
public class FileConversionRecording extends BaseStatusEntity<String> {
    @Column(comment = "文件名称")
    private String fileName;
    @Column(comment = "原文件类型")
    private String fileSuffix;
    @Column(comment = "新文件类型")
    private String afterFileSuffix;
    @Column(comment = "原文件路径")
    private String fileUrl;
    @Column(comment = "新文件路径")
    private String afterFileUrl;
    @Column(comment = "原文件大小")
    private Long fileSize;
    @Column(comment = "新文件大小")
    private Long afterFileSize;
    @Column(comment = "新文件大小")
    private String fileStatus;
    @Column(comment = "内容")
    private String statusContent;
    @Column(comment = "表单id", length = 100)
    private String formDefinitionId;
    @Column(comment = "档案id")
    private String eArchiveId;

    public String getStatusContent() {
        return statusContent;
    }

    public void setStatusContent(String statusContent) {
        this.statusContent = statusContent;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getAfterFileSuffix() {
        return afterFileSuffix;
    }

    public void setAfterFileSuffix(String afterFileSuffix) {
        this.afterFileSuffix = afterFileSuffix;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAfterFileUrl() {
        return afterFileUrl;
    }

    public void setAfterFileUrl(String afterFileUrl) {
        this.afterFileUrl = afterFileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getAfterFileSize() {
        return afterFileSize;
    }

    public void setAfterFileSize(Long afterFileSize) {
        this.afterFileSize = afterFileSize;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String geteArchiveId() {
        return eArchiveId;
    }

    public void seteArchiveId(String eArchiveId) {
        this.eArchiveId = eArchiveId;
    }

}
