package com.dr.archive.manage.task.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAILS", comment = "鉴定详情表", module = Constants.MODULE_NAME)
public class BatchDetails extends BaseStatusEntity<String> {

    @Column(comment = "鉴定ID", length = 500)
    private String appraisalid;

    @Column(comment = "批次ID", length = 200)
    private  String batchid;

    @Column(comment = "任务ID", length = 200)
    private String taskid;

    @Column(comment = "字段", length = 200)
    private String sourceCode;

    @Column(comment = "字段名", length = 200)
    private String sourceName;

    @Column(comment = "目标值", length = 200)
    private String targetValue;

    @Column(comment = "原始值", length = 200)
    private String sourceValue;

    @Column(comment = "审核意见", length = 200)
    private String advice;

    @Column(comment = "是否第一次审核", length = 50)
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAppraisalid() {
        return appraisalid;
    }

    public void setAppraisalid(String appraisalid) {
        this.appraisalid = appraisalid;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
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

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
