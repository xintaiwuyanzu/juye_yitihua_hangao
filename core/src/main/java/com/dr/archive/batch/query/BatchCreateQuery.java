package com.dr.archive.batch.query;

import com.dr.archive.model.query.ArchiveDataQuery;

/**
 * 任务查询条件
 *
 * @author dr
 */
public class BatchCreateQuery extends ArchiveDataQuery {
    /**
     * 导入的时候，用这个字段当作状态参数
     */
    private String name;
    private String fileLocation;
    private String fileName;
    private String mineType;
    private String type;
    private Long startDate;
    private Long endDate;
    private String sendPersonId;
    private String targetPersonId;
    private boolean isTask;
    private String categoryCode;
    private String fondCode;
    /**
     * 导入的时候，用这个做数据来源
     */
    private String sourceCode;
    private String sourceName;
    private String sourceValue;
    private String targetValue;
    private String taskId;
    private String appraisalType;
    private String transferingUnit;
    private String transferingUnitPerson;
    /**
     * 导入导出方案Id
     */
    private String impSchemaId;

    /**
     * 四性检测
     */
    private String fourDetection;

    /*
     * 导入方式 1:包结构 2:目录+原文*/
    private String impType;

    public String getFourDetection() {
        return fourDetection;
    }

    public void setFourDetection(String fourDetection) {
        this.fourDetection = fourDetection;
    }

    public String getImpType() {
        return impType;
    }

    public void setImpType(String impType) {
        this.impType = impType;
    }

    /**
     * 纠错记录
     */
    private String errorInfo;
    /**
     * 原文挂接路径
     */
    private String souceFilesPath;
    private String status;
    private String archiveCode;
    private String batchName;
    private String beizhu;

    /**
     * 业务指导
     *
     * @return
     */
    //业务指导名称
    private String businessName;

    /**
     * 年度检查
     *
     * @return
     */
    //年度检查名称
    private String checkName;

    private String toSendOrgId;

    private String toSendOrgName;

    public String getToSendOrgName() {
        return toSendOrgName;
    }

    public void setToSendOrgName(String toSendOrgName) {
        this.toSendOrgName = toSendOrgName;
    }

    //年度检查内容
    private String checkContent;


    /**
     * 执法检查
     *
     * @return
     */
    //执法检查名称
    private String lawName;

    //单位抽取规则
    private String toSendOrgFormId;

    //人员抽取规则
    private String toSendPerFormId;

    //销毁原因
    private String destoryOpinion;

    /**
     * 业务系统归档数据接收退回参数
     *
     * @return
     */
    //批次ID
    private String batchId;

    //all退回全部，select退回选中
    private String sendType;

    //退回原因
    private String returnReason;

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getDestoryOpinion() {
        return destoryOpinion;
    }

    public void setDestoryOpinion(String destoryOpinion) {
        this.destoryOpinion = destoryOpinion;
    }

    public String getLawName() {
        return lawName;
    }

    public void setLawName(String lawName) {
        this.lawName = lawName;
    }

    public String getToSendOrgFormId() {
        return toSendOrgFormId;
    }

    public void setToSendOrgFormId(String toSendOrgFormId) {
        this.toSendOrgFormId = toSendOrgFormId;
    }

    public String getToSendPerFormId() {
        return toSendPerFormId;
    }

    public void setToSendPerFormId(String toSendPerFormId) {
        this.toSendPerFormId = toSendPerFormId;
    }

    public String getToSendOrgId() {
        return toSendOrgId;
    }

    public void setToSendOrgId(String toSendOrgId) {
        this.toSendOrgId = toSendOrgId;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getSendPersonId() {
        return sendPersonId;
    }

    public void setSendPersonId(String sendPersonId) {
        this.sendPersonId = sendPersonId;
    }

    public String getTargetPersonId() {
        return targetPersonId;
    }

    public void setTargetPersonId(String targetPersonId) {
        this.targetPersonId = targetPersonId;
    }

    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean task) {
        isTask = task;
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

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getImpSchemaId() {
        return impSchemaId;
    }

    public void setImpSchemaId(String impSchemaId) {
        this.impSchemaId = impSchemaId;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(String appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSouceFilesPath() {
        return souceFilesPath;
    }

    public void setSouceFilesPath(String souceFilesPath) {
        this.souceFilesPath = souceFilesPath;
    }

    public String getTransferingUnit() {
        return transferingUnit;
    }

    public void setTransferingUnit(String transferingUnit) {
        this.transferingUnit = transferingUnit;
    }

    public String getTransferingUnitPerson() {
        return transferingUnitPerson;
    }

    public void setTransferingUnitPerson(String transferingUnitPerson) {
        this.transferingUnitPerson = transferingUnitPerson;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }
}
