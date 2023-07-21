package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_batch_wordGroup", module = Constants.MODULE_NAME, comment = "档案鉴定批次关联词组")
public class AppraisalBatchWordGroup extends BaseEntity {

    @Column(comment = "鉴定批次ID")
    private String batchId;

    @Column(comment = "鉴定词库id")
    private String wordGroupId;

    @Column(comment = "是否可以鉴定人员领取")
    private String isApply;

    @Column(comment = "优先级")
    private String priority;

    private String groupName;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getWordGroupId() {
        return wordGroupId;
    }

    public void setWordGroupId(String wordGroupId) {
        this.wordGroupId = wordGroupId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIsApply() {
        return isApply;
    }

    public void setIsApply(String isApply) {
        this.isApply = isApply;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
