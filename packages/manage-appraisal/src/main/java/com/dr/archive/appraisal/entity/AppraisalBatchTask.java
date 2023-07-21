package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_batch_task", module = Constants.MODULE_NAME, comment = "档案鉴定任务")
public class AppraisalBatchTask extends BaseStatusEntity<String> {

    @Column(comment = "鉴定任务编号")
    private String batchTaskCode;

    @Column(comment = "鉴定任务数量")
    private String batchTaskQuantity;

    @Column(comment = "完成鉴定任务数量")
    private String finishQuantity;

    @Column(comment = "任务匹配词库")
    private String taskWordGroup;

    @Column(comment = "机器鉴定结果")
    private String auxiliaryResult;

    @Column(comment = "鉴定批次ID")
    private String batchId;

    @Column(comment = "鉴定类型")
    private String appraisalType;

    @Column(comment = "当前操作人员")
    private String currentPerson;

    @Column(comment = "当前操作人员姓名")
    private String currentPersonName;

    @Column(comment = "领取任务姓名")
    private String receiveTaskName;

    public String getBatchTaskCode() {
        return batchTaskCode;
    }

    public void setBatchTaskCode(String batchTaskCode) {
        this.batchTaskCode = batchTaskCode;
    }

    public String getBatchTaskQuantity() {
        return batchTaskQuantity;
    }

    public void setBatchTaskQuantity(String batchTaskQuantity) {
        this.batchTaskQuantity = batchTaskQuantity;
    }

    public String getFinishQuantity() {
        return finishQuantity;
    }

    public void setFinishQuantity(String finishQuantity) {
        this.finishQuantity = finishQuantity;
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

    public String getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(String currentPerson) {
        this.currentPerson = currentPerson;
    }

    public String getTaskWordGroup() {
        return taskWordGroup;
    }

    public void setTaskWordGroup(String taskWordGroup) {
        this.taskWordGroup = taskWordGroup;
    }

    public String getAuxiliaryResult() {
        return auxiliaryResult;
    }

    public void setAuxiliaryResult(String auxiliaryResult) {
        this.auxiliaryResult = auxiliaryResult;
    }

    public String getCurrentPersonName() {
        return currentPersonName;
    }

    public void setCurrentPersonName(String currentPersonName) {
        this.currentPersonName = currentPersonName;
    }

    public String getReceiveTaskName() {
        return receiveTaskName;
    }

    public void setReceiveTaskName(String receiveTaskName) {
        this.receiveTaskName = receiveTaskName;
    }
}
