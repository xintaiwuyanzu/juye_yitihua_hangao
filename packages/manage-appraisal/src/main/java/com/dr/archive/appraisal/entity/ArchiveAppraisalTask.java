package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_arvhie_appraisal_task", module = Constants.MODULE_NAME, comment = "鉴定任务中的档案信息")
public class ArchiveAppraisalTask extends BaseStatusEntity<String> {

    @Column(comment = "全宗ID")
    private String fondId;

    @Column(comment = "全宗编码")
    private String fondCode;

    @Column(comment = "门类ID")
    private String categoryId;

    @Column(comment = "门类编码")
    private String categoryCode;

    @Column(comment = "门类名称")
    private String categoryName;

    @Column(comment = "档号")
    private String archiveCode;

    @Column(comment = "题名")
    private String title;

    @Column(comment = "年份")
    private String vintages;

    @Column(comment = "文件时间")
    private String filetime;

    @Column(comment = "档案所属馆")
    private String organiseId;

    @Column(comment = "表单ID")
    private String formDefinitionId;

    @Column(comment = "表单数据ID")
    private String formDataId;

    @Column(comment = "辅助鉴定结果")
    private String auxiliaryResult;

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

    @Column(comment = "是否查看过鉴定历史")
    private String isSeeHisTory;

    @Column(comment = "延期年限")
    private String delayYear;

    @Column(comment = "匹配词库")
    private String matchingWordGroup;

    @Column(comment = "标记")
    private String sign;

    @Column(comment = "下个环节是否标记")
    private String nextSign;

    @Column(comment = "是否销毁")
    private String isDel;

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVintages() {
        return vintages;
    }

    public void setVintages(String vintages) {
        this.vintages = vintages;
    }

    public String getFiletime() {
        return filetime;
    }

    public void setFiletime(String filetime) {
        this.filetime = filetime;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
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

    public String getAuxiliaryResult() {
        return auxiliaryResult;
    }

    public void setAuxiliaryResult(String auxiliaryResult) {
        this.auxiliaryResult = auxiliaryResult;
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

    public String getIsSeeHisTory() {
        return isSeeHisTory;
    }

    public void setIsSeeHisTory(String isSeeHisTory) {
        this.isSeeHisTory = isSeeHisTory;
    }

    public String getDelayYear() {
        return delayYear;
    }

    public void setDelayYear(String delayYear) {
        this.delayYear = delayYear;
    }

    public String getMatchingWordGroup() {
        return matchingWordGroup;
    }

    public void setMatchingWordGroup(String matchingWordGroup) {
        this.matchingWordGroup = matchingWordGroup;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNextSign() {
        return nextSign;
    }

    public void setNextSign(String nextSign) {
        this.nextSign = nextSign;
    }
}
