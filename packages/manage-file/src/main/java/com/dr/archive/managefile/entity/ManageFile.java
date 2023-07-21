package com.dr.archive.managefile.entity;

import com.dr.archive.model.entity.BaseBusinessIdEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-04-07 17:16
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "MANAGE_FILE", module = Constants.MODULE_NAME, comment = "资料库关联表")
public class ManageFile extends BaseBusinessIdEntity {
    @Column(comment = "文件id")
    private String fileInfoId;
    @Column(comment = "文件名称")
    private String fileName;
    @Column(comment = "文件类型")
    private String suffix;
    @Column(comment = "上传日期")
    private long saveDate;
    @Column(comment = "备注")
    private String fileDescription;
    @Column(comment = "表单id")
    private String formDefinitionId;
    @Column(comment = "模板Id")
    private String templateId;
    /**
     * 批次id，直接查询相关附件，目前在编研模块用到
     */
    @Column(comment = "batchId")
    private String batchId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(String fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public long getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(long saveDate) {
        this.saveDate = saveDate;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }
}
