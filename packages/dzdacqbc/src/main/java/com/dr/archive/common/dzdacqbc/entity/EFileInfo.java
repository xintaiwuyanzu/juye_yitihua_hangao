package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * 长期保存文件信息表
 */
@Table(name = Constants.DZDNCQBC + "efile_info", module = Constants.MODULE_NAME, comment = "长期保存文件信息表")
public class EFileInfo extends BaseStatusEntity<String> {
    @Column(comment = "文件名称")
    private String fileName;
    @Column(comment = "文件路径")
    private String filePath;
    @Column(comment = "文件大小")
    private long fileSize;
    @Column(comment = "文件类型")
    private String suffix;
    @Column(comment = "摘要")
    private String digest;
    @Column(comment = "长期保存档案信息表id")
    private String archiveId;
    @Column(comment = "表单id", length = 100)
    private String formDefinitionId;
    @Column(comment = "数据id", length = 100)
    private String formDataId;
    @Column(comment = "档案id")
    private String eArchiveId;

    public String geteArchiveId() {
        return eArchiveId;
    }

    public void seteArchiveId(String eArchiveId) {
        this.eArchiveId = eArchiveId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }
}
