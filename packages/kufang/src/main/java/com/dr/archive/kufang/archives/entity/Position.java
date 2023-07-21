package com.dr.archive.kufang.archives.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;


/**
 * 密集架位置信息管理类
 *
 * @author dr
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "POSITION", module = Constants.COMMON_MODULE_NAME, comment = "密集架位置信息表")
public class Position extends BaseStatusEntity<String> {

    @Column(comment = "库房id", length = 50)
    private String locId;
    @Column(name = "areaId", comment = "区域id", length = 50)
    private String areaId;
    @Column(name = "orgId", comment = "机构id", length = 50)
    private String orgId;

    @Column(comment = "密集架编号", length = 50)
    private String bookCaseNo;
    @Column(comment = "密集架名称", length = 50)
    private String caseName;
    /**
     * 密集架id（这个应该是相当于rfid的东西，可以扫描出来）
     */
    @Column(comment = "密集架id（书架的磁条）", length = 50)
    private String bookCaseId;
    @Column(comment = "密集架EPC", length = 50)
    private String epcOrder;

    @Column(comment = "密集架层数", length = 30)
    private String layer;
    @Column(comment = "密集架面数(AB面)", length = 10)
    private String columnNum;
    @Column(comment = "密集架列数", length = 10)
    private String columnNo;
    @Column(comment = "最后更新时间", type = ColumnType.DATE, length = 30)
    private long lastUpdate;

    @Column(comment = "位置描述", length = 150)
    private String caseNoTrans;
    @Column(comment = "备注", length = 300)
    private String remarks;
    @Column(comment = "状态")
    private String positionStatus;

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getBookCaseNo() {
        return bookCaseNo;
    }

    public void setBookCaseNo(String bookCaseNo) {
        this.bookCaseNo = bookCaseNo;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getBookCaseId() {
        return bookCaseId;
    }

    public void setBookCaseId(String bookCaseId) {
        this.bookCaseId = bookCaseId;
    }

    public String getEpcOrder() {
        return epcOrder;
    }

    public void setEpcOrder(String epcOrder) {
        this.epcOrder = epcOrder;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(String columnNum) {
        this.columnNum = columnNum;
    }

    public String getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(String columnNo) {
        this.columnNo = columnNo;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCaseNoTrans() {
        return caseNoTrans;
    }

    public void setCaseNoTrans(String caseNoTrans) {
        this.caseNoTrans = caseNoTrans;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
