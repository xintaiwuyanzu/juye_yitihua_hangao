package com.dr.archive.utilization.consult.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 查档利用批次信息
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_CONSULT", comment = "查档申请批次表", module = Constants.MODULE_NAME)
public class ArchiveBatchConsult extends AbstractArchiveBatch {
    /**
     * 利用相关基本信息
     */
    @Column(comment = "查档内容描述", length = 100)
    private String searchDescription;
    @Column(comment = "利用方式描述", length = 100)
    private String useWay;
    @Column(comment = "利用目的", length = 100)
    private String useFor;
    @Column(comment = "其他", length = 500)
    private String other;
    /*
     *========================================
     *用户基本信息
     *========================================
     */

    @Column(name = "userName", comment = "申请人")
    private String userName;
    @Column(comment = "用户ID")
    private String userId;
    @Column(comment = "预约时间")
    private String appointmentTime;
    @Column(name = "birthday", comment = "出生日期", type = ColumnType.DATE)
    private long birthday;
    @Column(comment = "民族")
    private String ethnic;
    @Column(comment = "性别", length = 50)
    private String sex;

    /*
     *========================================
     *联系信息
     *========================================
     */

    @Column(comment = "联系电话", length = 100)
    private String phone;
    @Column(name = "address", comment = "家庭住址", length = 50)
    private String address;

    /*
     *========================================
     *证件信息
     *========================================
     */

    @Column(comment = "有效证件号", length = 50)
    private String idNo;
    @Column(name = "valid_date", comment = "有效期限", type = ColumnType.DATE)
    private long valid;
    @Column(comment = "签发机关")
    private String agency;
    @Column(comment = "证件类型")
    private String idType;


    @Column(comment = "数据来源")
    private String sourceType;

    @Column(comment = "创建机构ID")
    private String createOrgId;
    @Column(comment = "接收机构ID")
    private String receiveOrgId;
    @Column(comment = "备注")
    private String remarks;

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public String getUseWay() {
        return useWay;
    }

    public void setUseWay(String useWay) {
        this.useWay = useWay;
    }

    public String getUseFor() {
        return useFor;
    }

    public void setUseFor(String useFor) {
        this.useFor = useFor;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public long getValid() {
        return valid;
    }

    public void setValid(long valid) {
        this.valid = valid;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getReceiveOrgId() {
        return receiveOrgId;
    }

    public void setReceiveOrgId(String receiveOrgId) {
        this.receiveOrgId = receiveOrgId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
