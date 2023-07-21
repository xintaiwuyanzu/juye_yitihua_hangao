package com.dr.archive.manage.task.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAIL_APPRAISAL", comment = "鉴定批次详情信息", module = Constants.MODULE_NAME)
public class AppraisalBatchDetail extends AbstractBatchDetailEntity {
    @Column(comment = "原字段code", length = 50)
    private String sourceCode;
    @Column(comment = "原字段名称", length = 200)
    private String sourceName;
    @Column(comment = "原始值", length = 500)
    private String sourceValue;
    @Column(comment = "目标值", length = 500)
    private String targetValue;
    @Column(comment = "鉴定类型", length = 50)
    private String appraisalType;
    @Column(comment = "鉴定人员", length = 50)
    private String appraisalPerson;
    @Column(comment = "鉴定时间", length = 50)
    private Long appraisalDate;
    @Column(comment = "表单类型")
    private String formType;
    @Column(comment = "鉴定意见")
    private String opinion;
    /**
     * 0初审
     * 1复审
     * 2终审
     */
    @Column(comment = "鉴定环节")
    private String auditStatus;

    @Column(comment = "初审状态")
    private String firstTrial;

    @Column(comment = "初审人ID")
    private String firstPersonId;

    @Column(comment = "初审人名称")
    private String firstPersonName;

    @Column(comment = "初审结果")
    private String firstResult;

    @Column(comment = "初审意见")
    private String firstTrialOpinion;

    @Column(comment = "初审延期年限")
    private String firstDelayTime;

    @Column(comment = "复审状态")
    private String recheckStatus;

    @Column(comment = "复审人ID")
    private String recheckPersonId;

    @Column(comment = "复审人名称")
    private String recheckPersonName;

    @Column(comment = "复审结果")
    private String recheckResult;

    @Column(comment = "复审意见")
    private String recheckOpinion;

    @Column(comment = "复审意见")
    private String recheckDelayTime;

    @Column(comment = "终审状态")
    private String finalStatus;

    @Column(comment = "终审人ID")
    private String finalPersonId;

    @Column(comment = "终审人名称")
    private String finalPersonName;

    @Column(comment = "终审结果")
    private String finalResult;

    @Column(comment = "终审意见")
    private String finalOpinion;

    @Column(comment = "上一条详情id")
    private String preDetailId;

    public String getRecheckDelayTime() {
        return recheckDelayTime;
    }

    public void setRecheckDelayTime(String recheckDelayTime) {
        this.recheckDelayTime = recheckDelayTime;
    }

    public String getFirstDelayTime() {
        return firstDelayTime;
    }

    public void setFirstDelayTime(String firstDelayTime) {
        this.firstDelayTime = firstDelayTime;
    }

    public String getFirstPersonId() {
        return firstPersonId;
    }

    public void setFirstPersonId(String firstPersonId) {
        this.firstPersonId = firstPersonId;
    }

    public String getFirstPersonName() {
        return firstPersonName;
    }

    public void setFirstPersonName(String firstPersonName) {
        this.firstPersonName = firstPersonName;
    }

    public String getRecheckPersonId() {
        return recheckPersonId;
    }

    public void setRecheckPersonId(String recheckPersonId) {
        this.recheckPersonId = recheckPersonId;
    }

    public String getRecheckPersonName() {
        return recheckPersonName;
    }

    public void setRecheckPersonName(String recheckPersonName) {
        this.recheckPersonName = recheckPersonName;
    }

    public String getFinalPersonId() {
        return finalPersonId;
    }

    public void setFinalPersonId(String finalPersonId) {
        this.finalPersonId = finalPersonId;
    }

    public String getFinalPersonName() {
        return finalPersonName;
    }

    public void setFinalPersonName(String finalPersonName) {
        this.finalPersonName = finalPersonName;
    }

    public String getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(String firstResult) {
        this.firstResult = firstResult;
    }

    public String getRecheckResult() {
        return recheckResult;
    }

    public void setRecheckResult(String recheckResult) {
        this.recheckResult = recheckResult;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public String getPreDetailId() {
        return preDetailId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getFirstTrial() {
        return firstTrial;
    }

    public void setFirstTrial(String firstTrial) {
        this.firstTrial = firstTrial;
    }

    public String getFirstTrialOpinion() {
        return firstTrialOpinion;
    }

    public void setFirstTrialOpinion(String firstTrialOpinion) {
        this.firstTrialOpinion = firstTrialOpinion;
    }

    public String getRecheckStatus() {
        return recheckStatus;
    }

    public void setRecheckStatus(String recheckStatus) {
        this.recheckStatus = recheckStatus;
    }

    public String getRecheckOpinion() {
        return recheckOpinion;
    }

    public void setRecheckOpinion(String recheckOpinion) {
        this.recheckOpinion = recheckOpinion;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getFinalOpinion() {
        return finalOpinion;
    }

    public void setFinalOpinion(String finalOpinion) {
        this.finalOpinion = finalOpinion;
    }

    public void setPreDetailId(String preDetailId) {
        this.preDetailId = preDetailId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(String appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String getAppraisalPerson() {
        return appraisalPerson;
    }

    public void setAppraisalPerson(String appraisalPerson) {
        this.appraisalPerson = appraisalPerson;
    }

    public Long getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Long appraisalDate) {
        this.appraisalDate = appraisalDate;
    }
}
