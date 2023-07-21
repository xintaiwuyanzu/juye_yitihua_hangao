package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存 恢复记录表
 */

@Table(name = Constants.DZDNCQBC + "restore_record", module = Constants.MODULE_NAME, comment = "电子档案长期保存恢复记录表")
public class ESaveRestoreRecord extends BaseStatusEntity<String> {

    @Column(comment = "备份批次id", length = 100)
    private String batchId;

    @Column(comment = "恢复记录名称", length = 100)
    private String recordName;

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

    @Column(comment = "存储空间id")
    private String spaceId;

    @Column(comment = "备份地址")
    private String backPath;

    @Column(comment = "恢复地址")
    private String restorePath;

    @Column(comment = "恢复人id")
    private String personId;
    @Column(comment = "恢复人名称")
    private String personName;

    @Column(comment = "备份类型")
    private String reType;

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

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    public String getRestorePath() {
        return restorePath;
    }

    public void setRestorePath(String restorePath) {
        this.restorePath = restorePath;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getReType() {
        return reType;
    }

    public void setReType(String reType) {
        this.reType = reType;
    }
}
