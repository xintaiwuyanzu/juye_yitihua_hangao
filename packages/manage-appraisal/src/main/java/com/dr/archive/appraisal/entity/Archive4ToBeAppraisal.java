package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_arvhie_4tobe_appraisal", module = Constants.MODULE_NAME, comment = "待鉴定档案库")
public class Archive4ToBeAppraisal extends BaseStatusEntity<String> {

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

    @Column(comment = "目录号")
    private String catalogueCode;

    @Column(comment = "卷号")
    private String ajh;

    @Column(comment = "单位名称")
    private String orgName;

    @Column(comment = "年份")
    private String vintages;

    @Column(comment = "文件时间")
    private String filetime;

    @Column(comment = "档案到期时间")
    private String expirationTime;

    @Column(comment = "档案所属馆")
    private String organiseId;

    @Column(comment = "密级敏感词")
    private String s_level;

    @Column(comment = "鉴定类型")
    private String appraisalType;

    @Column(comment = "表单ID")
    private String formDefinitionId;

    @Column(comment = "表单数据ID")
    private String formDataId;

    @Column(comment = "匹配规则")
    private String matchingRules;

    @Column(comment = "辅助鉴定结果")
    private String auxiliaryResult;

    @Column(comment = "匹配专题")
    private String matchingSpecial;

    @Column(comment = "开放范围")
    private String openRange;

    @Column(comment = "鉴定的任务id")
    private String taskId;

    @Column(comment = "鉴定的批次id")
    private String batchId;

    @Column(comment = "延期年限")
    private String expiresYear;
    @Column(comment = "文号")
    private String fileCode;

    @Column(comment = "责任者")
    private String dutyPerson;

    @Column(comment = "应保管期限")
    private String saveTerm;

    @Column(comment = "上次鉴定档案时间")
    private String lastAppraisalDate;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
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

    public String getOpenRange() {
        return openRange;
    }

    public void setOpenRange(String openRange) {
        this.openRange = openRange;
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

    public String getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(String appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String getMatchingRules() {
        return matchingRules;
    }

    public void setMatchingRules(String matchingRules) {
        this.matchingRules = matchingRules;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getCatalogueCode() {
        return catalogueCode;
    }

    public void setCatalogueCode(String catalogueCode) {
        this.catalogueCode = catalogueCode;
    }

    public String getAjh() {
        return ajh;
    }

    public void setAjh(String ajh) {
        this.ajh = ajh;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getExpiresYear() {
        return expiresYear;
    }

    public void setExpiresYear(String expiresYear) {
        this.expiresYear = expiresYear;
    }

    public String getS_level() {
        return s_level;
    }

    public void setS_level(String s_level) {
        this.s_level = s_level;
    }

    public String getLastAppraisalDate() {
        return lastAppraisalDate;
    }

    public void setLastAppraisalDate(String lastAppraisalDate) {
        this.lastAppraisalDate = lastAppraisalDate;
    }

    public String getMatchingSpecial() {
        return matchingSpecial;
    }

    public void setMatchingSpecial(String matchingSpecial) {
        this.matchingSpecial = matchingSpecial;
    }
}
