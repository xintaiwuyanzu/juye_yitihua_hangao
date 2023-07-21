package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_batch", module = Constants.MODULE_NAME, comment = "档案鉴定批次")
public class AppraisalBatch extends BaseStatusEntity<String> {

    @Column(comment = "鉴定任务名称")
    private String batchName;

    @Column(comment = "鉴定任务类型")
    private String appraisalType;

    @Column(comment = "鉴定任务描述")
    private String batchRemarks;

    @Column(comment = "开始时间")
    private String startTime;

    @Column(comment = "结束时间")
    private String endTime;

    @Column(comment = "鉴定全宗编码")
    private String fondCodes;

    @Column(comment = "鉴定全宗名称")
    private String fondNames;

    @Column(comment = "开始年度")
    private String startVintages;

    @Column(comment = "结束年度")
    private String endVintages;

    @Column(comment = "档案数量")
    private String archiveQuantity;

    @Column(comment = "创建用户名称")
    private String createPersonName;

    @Column(comment = "是否已经销毁档案")
    private String isDestory;

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(String appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String getBatchRemarks() {
        return batchRemarks;
    }

    public void setBatchRemarks(String batchRemarks) {
        this.batchRemarks = batchRemarks;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFondCodes() {
        return fondCodes;
    }

    public void setFondCodes(String fondCodes) {
        this.fondCodes = fondCodes;
    }

    public String getFondNames() {
        return fondNames;
    }

    public void setFondNames(String fondNames) {
        this.fondNames = fondNames;
    }

    public String getStartVintages() {
        return startVintages;
    }

    public void setStartVintages(String startVintages) {
        this.startVintages = startVintages;
    }

    public String getEndVintages() {
        return endVintages;
    }

    public void setEndVintages(String endVintages) {
        this.endVintages = endVintages;
    }

    public String getArchiveQuantity() {
        return archiveQuantity;
    }

    public void setArchiveQuantity(String archiveQuantity) {
        this.archiveQuantity = archiveQuantity;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getIsDestory() {
        return isDestory;
    }

    public void setIsDestory(String isDestory) {
        this.isDestory = isDestory;
    }
}
