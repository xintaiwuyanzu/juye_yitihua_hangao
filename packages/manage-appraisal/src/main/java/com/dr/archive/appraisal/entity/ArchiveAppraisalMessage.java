package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_arvhie_appraisal_message", module = Constants.MODULE_NAME, comment = "档案鉴定记录")
public class ArchiveAppraisalMessage extends BaseStatusEntity<String> {

    @Column(comment = "表单ID")
    private String formDefinitionId;

    @Column(comment = "表单数据ID")
    private String formDataId;

    @Column(comment = "鉴定的任务id")
    private String taskId;

    @Column(comment = "鉴定的批次id")
    private String batchId;

    @Column(comment = "鉴定类型")
    private String appraisalType;

    @Column(comment = "人工鉴定结果")
    private String personResult;

    @Column(comment = "人工鉴定依据")
    private String personAppraisalBasis;

    @Column(comment = "人工鉴定关键字")
    private String personAppraisalKeyWord;

    @Column(comment = "备注")
    private String remarks;

    @Column(comment = "人工鉴定结果与机器结果是否一致")
    private String isEqual;

    @Column(comment = "操作人员姓名")
    private String appraisalPersonName;

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getPersonResult() {
        return personResult;
    }

    public void setPersonResult(String personResult) {
        this.personResult = personResult;
    }

    public String getPersonAppraisalBasis() {
        return personAppraisalBasis;
    }

    public void setPersonAppraisalBasis(String personAppraisalBasis) {
        this.personAppraisalBasis = personAppraisalBasis;
    }

    public String getPersonAppraisalKeyWord() {
        return personAppraisalKeyWord;
    }

    public void setPersonAppraisalKeyWord(String personAppraisalKeyWord) {
        this.personAppraisalKeyWord = personAppraisalKeyWord;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(String appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String getIsEqual() {
        return isEqual;
    }

    public void setIsEqual(String isEqual) {
        this.isEqual = isEqual;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getAppraisalPersonName() {

        return appraisalPersonName;
    }

    public void setAppraisalPersonName(String appraisalPersonName) {
        this.appraisalPersonName = appraisalPersonName;
    }
}
