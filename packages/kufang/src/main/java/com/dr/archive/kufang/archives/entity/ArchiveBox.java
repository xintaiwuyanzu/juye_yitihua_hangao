package com.dr.archive.kufang.archives.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;


/**
 * 档案盒信息
 *
 * @author dr
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "ARCHIVEBOX", module = Constants.COMMON_MODULE_NAME, comment = "档案盒信息")
public class ArchiveBox extends BaseStatusEntity<String> {

    @Column(comment = "档案盒号", length = 50)
    private String boxCode;

    @Column(comment = "保管期限", length = 50)
    private String CP006;

    @Column(comment = "全宗号", length = 50)
    private String fondNum;

    @Column(comment = "全宗id", length = 50)
    private String fondid;

    @Column(comment = "全宗名称", length = 50)
    private String fondName;

    @Column(comment = "分类", length = 50)
    private String boxType;

    @Column(comment = "开始", length = 50)
    private String startNum;

    @Column(comment = "结束", length = 50)
    private String endNum;

    @Column(comment = "总数", length = 50)
    private String sumFile;

    @Column(comment = "年度", length = 50)
    private String holdingyear;

    @Column(comment = "EPC电子代码", length = 50)
    private String epc;

    @Column(comment = "馆藏室名称", length = 50)
    private String locationName;

    @Column(comment = "馆藏室id", length = 50)
    private String locationId;

    @Column(comment = "layer表id", length = 50)
    private String bookCaseNo;

    @Column(comment = "位置描述", length = 100)
    private String positionInfo;

    @Column(comment = "状态", length = 10)
    private String holdStatus;
    @Column(comment = "备注信息", length = 10)
    private String remarks;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCP006() {
        return CP006;
    }

    public void setCP006(String CP006) {
        this.CP006 = CP006;
    }

    public String getFondNum() {
        return fondNum;
    }

    public void setFondNum(String fondNum) {
        this.fondNum = fondNum;
    }

    public String getFondid() {
        return fondid;
    }

    public void setFondid(String fondid) {
        this.fondid = fondid;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }

    public String getSumFile() {
        return sumFile;
    }

    public void setSumFile(String sumFile) {
        this.sumFile = sumFile;
    }

    public String getHoldingyear() {
        return holdingyear;
    }

    public void setHoldingyear(String holdingyear) {
        this.holdingyear = holdingyear;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getBookCaseNo() {
        return bookCaseNo;
    }

    public void setBookCaseNo(String bookCaseNo) {
        this.bookCaseNo = bookCaseNo;
    }

    public String getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(String positionInfo) {
        this.positionInfo = positionInfo;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }
}
