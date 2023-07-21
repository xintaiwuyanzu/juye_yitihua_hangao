package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 出入库登记
 */
@Table(name = Constants.TABLE_PREFIX + "InAndOutRecords", module = Constants.MODULE_NAME, comment = "出入库登记")
public class InAndOutRecords extends BaseStatusEntity<String> {
    @Column(comment = "机构id", length = 50)
    private String organiseId;
    @Column(comment = "档案名称", length = 200)
    private String title;
    @Column(comment = "档案id", length = 200)
    private String archiveId;
    @Column(comment = "档号", length = 200)
    private String archiveCode;
    @Column(comment = "档案类别", length = 200)
    private String archiveType;
    @Column(comment = "记录人", length = 200)
    private String personName;
    @Column(comment = "库房", length = 50)
    private String locId;
    @Column(comment = "库房名称", length = 100)
    private String locName;

    @Column(comment = "区", length = 50)
    private String areaId;
    @Column(comment = "库房名称", length = 100)
    private String areaName;

    @Column(comment = "架号", length = 200)
    private String caseNo;
    @Column(comment = "架号id", length = 200)
    private String caseId;
    @Column(comment = "AB", length = 200)
    private String columnNum;
    @Column(comment = "列号", length = 200)
    private String columnNo;
    @Column(comment = "层号", length = 200)
    private String layer;
    @Column(comment = "出库原因",length = 200)
    private String reason;
    @Column(comment = "备注", length = 200)
    private String remarks;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(String columnNum) {
        this.columnNum = columnNum;
    }

    @Column(comment = "操作类别", length = 200)
    private String doType;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDoType() {
        return doType;
    }

    public void setDoType(String doType) {
        this.doType = doType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(String columnNo) {
        this.columnNo = columnNo;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
