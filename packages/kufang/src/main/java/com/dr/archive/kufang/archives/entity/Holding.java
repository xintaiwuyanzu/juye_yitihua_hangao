package com.dr.archive.kufang.archives.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;


/**
 * 密集架位置信息管理类
 *
 * @author dr
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "HOLDING", module = Constants.COMMON_MODULE_NAME, comment = "档案信息")
public class Holding extends BaseStatusEntity<String> {

    public static final String STATUS_入藏 = "b";
    public static final String STATUS_借出 = "c";
    public static final String STATUS_租出 = "d";
    public static final String STATUS_预约 = "e";
    public static final String STATUS_丢失 = "f";
    public static final String STATUS_剔除 = "g";
    public static final String STATUS_装订 = "h";
    public static final String STATUS_交换 = "i";
    public static final String STATUS_注销 = "l";
    public static final String STATUS_污损 = "s";
    public static final String STATUS_待上架 = "w";

    @Column(comment = "题名", length = 50)
    private String cp005;

    @Column(comment = "档号", length = 50)
    private String cn084;

    @Column(comment = "分类编码", length = 50)
    private String cp002;

    @Column(comment = "保管期限", length = 50)
    private String cp006;

    @Column(comment = "全宗id", length = 50)
    private String fondid;

    @Column(comment = "全宗名称", length = 50)
    private String fondName;

    @Column(comment = "分类id", length = 50)
    private String categoryid;

    @Column(comment = "分类名称", length = 50)
    private String categoryname;

    @Column(comment = "年度", length = 50)
    private String holdingyear;

    @Column(comment = "档案id（档案的磁条）", length = 50)
    private String tid;
    @Column(comment = "EPC电子代码", length = 50)
    private String epc;

    @Column(comment = "馆藏室id", length = 50)
    private String locationId;

    @Column(comment = "馆藏室id", length = 50)
    private String locationName;

    @Column(comment = "layer表id", length = 50)
    private String bookCaseNo;
    @Column(comment = "位置描述", length = 100)
    private String positionInfo;

    @Column(comment = "状态", length = 10)
    private String holdStatus;

    @Column(comment = "保存状况", length = 10)
    private String svaing;

    @Column(comment = "备注", length = 10)
    private String remarks;

    public String getSvaing() {
        return svaing;
    }

    public void setSvaing(String svaing) {
        this.svaing = svaing;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus;
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

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getYear() {
        return holdingyear;
    }

    public void setYear(String holdingyear) {
        this.holdingyear = holdingyear;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public String getCp005() {
        return cp005;
    }

    public void setCp005(String cp005) {
        this.cp005 = cp005;
    }

    public String getCn084() {
        return cn084;
    }

    public void setCn084(String cn084) {
        this.cn084 = cn084;
    }

    public String getCp002() {
        return cp002;
    }

    public void setCp002(String cp002) {
        this.cp002 = cp002;
    }

    public String getCp006() {
        return cp006;
    }

    public void setCp006(String cp006) {
        this.cp006 = cp006;
    }

    public String getHoldingyear() {
        return holdingyear;
    }

    public void setHoldingyear(String holdingyear) {
        this.holdingyear = holdingyear;
    }
}
