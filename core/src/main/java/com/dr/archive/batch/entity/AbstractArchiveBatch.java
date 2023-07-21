package com.dr.archive.batch.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 档案批次信息表
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH", comment = "档案批次信息表", module = Constants.MODULE_NAME)
public class AbstractArchiveBatch extends BaseStatusEntity<String> {
    @Column(comment = "批次名称", length = 500)
    private String batchName;
    @Column(comment = "类型", length = 50)
    private String batchType;
    @Column(comment = "开始时间", type = ColumnType.DATE)
    private Long startDate;
    @Column(comment = "结束时间", type = ColumnType.DATE)
    private Long endDate;
    @Column(comment = "详情总数")
    private long detailNum;
    @Column(comment = "文件存储位置", length = 500)
    private String fileLocation;
    @Column(comment = "文件名称", length = 500)
    private String fileName;
    @Column(comment = "文件类型", length = 100)
    private String mineType;
    @Column(comment = "备注")
    private String beizhu;
    @Column(comment = "表单id")
    private String formDefinitionId;

    @Column(comment = "机构Id")
    private String orgId;

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
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

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
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

    public long getDetailNum() {
        return detailNum;
    }

    public void setDetailNum(long detailNum) {
        this.detailNum = detailNum;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
