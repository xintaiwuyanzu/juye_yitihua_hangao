package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存 备份批次详情表
 */

@Table(name = Constants.DZDNCQBC + "backBatch_detail", module = Constants.MODULE_NAME, comment = "电子档案长期保存备份批次详情表")
public class ESaveBackBatchDetail extends BaseStatusEntity<String> {

    @Column(comment = "近线批次id或离线备份批次id", length = 200)
    private String batchId;

    @Column(comment = "档案id", length = 100)
    private String archiveId;

    @Column(comment = "表单id", length = 100)
    private String formDefinitionId;

    @Column(comment = "档号", length = 100)
    private String archiveCode;

    @Column(comment = "题名")
    private String timing;


    @Column(comment = "全宗号")
    private String fond_code;

    @Column(comment = "档案门类代码", length = 100)
    private String categoryCode;

    @Column(comment = "年度", length = 100)
    private String nd;

    @Column(comment = "存储空间id")
    private String spaceId;
    @Column(comment = "备份地址")
    private String backPath;

    @Column(comment = "介质id")
    private String mediumId;

    @Column(comment = "备份类型")
    private String backType;

    //1：已过期  0：未过期
    @Column(comment = "是否过期")
    private String isExpire;

    @Column(comment = "已恢复次数")
    private int backCount;

    @Column(comment = "机构id")
    private String orgId;

    @Column(comment = "档案类型")
    private String archiveType;


    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }


    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }



    public String getFond_code() {
        return fond_code;
    }

    public void setFond_code(String fond_code) {
        this.fond_code = fond_code;
    }


    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getMediumId() {
        return mediumId;
    }

    public void setMediumId(String mediumId) {
        this.mediumId = mediumId;
    }

    public String getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(String isExpire) {
        this.isExpire = isExpire;
    }

    public int getBackCount() {
        return backCount;
    }

    public void setBackCount(int backCount) {
        this.backCount = backCount;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }
}
