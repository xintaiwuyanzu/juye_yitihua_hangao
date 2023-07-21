package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseDescriptionEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author lych
 * @date 2023/2/1 上午 11:22
 * @mood happy
 */
@Table(name = Constants.TABLE_PREFIX + "Borrowing_Registration", comment = "电子借阅登记表", module = Constants.MODULE_NAME)

public class BorrowingRegistration extends BaseDescriptionEntity<String> {

    /**
     * 借阅申请  { 状态：status }字典项
     * 0：暂存（申请人只有在0时修改申请条件，其他状态可以将编辑按钮去掉，为0时候可以考虑有删除按钮）
     * 1:  已提交
     * 2：办理中（借阅人员给申请人员查询档案时）
     * 3：办结（借阅人员查询完后，点击办结，办结后申请人可以查看档案）
     * 4：不通过（失败选项）
     */
    @Column(comment = "申请借阅编号",length = 100,order = 19)
    private String borrowingCode;

    @Column(comment = "申请人",length = 100,order = 20)
    private String applicant;

    @Column(comment = "申请人Id",length = 100,order = 21)
    private String applicantId;

    @Column(comment = "机构id",length = 100,order = 22)
    private String organizeId;

    @Column(comment = "机构名称",length = 100,order = 23)
    private String organizeName;

    @Column(comment = "借阅时间",length = 100,order = 24)
    private Long borrowingTime;

    @Column(comment = "查询目的",length = 800,order = 25)
    private String purpose;

    @Column(comment = "查询范围",length = 800,order = 26)
    private String reviewScope;

    @Column(comment = "关键字",length = 100,order = 27)
    private String keywordString;

    @Column(comment = "办结时间",length = 100,order = 28)
    private Long FinishTime;

    @Column(comment = "借阅规则id",length = 100,order = 29)
    private String strategyId;

    //用户信息
    @Column(comment = "用户电话",length = 100,order = 30)
    private String phone;

    /**
     * 0：否  1：是
     */

    @Column(comment = "是否打印",length = 100)
    private String printState;

    /**
     * 0：否  1：是
     */
    @Column(comment = "是否下载",length = 100)
    private String downloadState;

    /**
     * 0：否  1：是
     */
    @Column(comment = "是否带水印",length = 100)
    private String watermarkState;

    @Column(comment = "借阅档案信息",length = 500)
    private String archiveContent;

    @Column(comment = "身份证",length = 100,order = 31)
    private String idNo;

    @Column(comment = "邮箱",length = 100,order = 31)
    private String email;

    @Column(comment = "手机号",length = 100,order = 31)
    private String mobile;

    @Column(comment = "职务",length = 100,order = 31)
    private String duty;

    public String getPrintState() {
        return printState;
    }

    public void setPrintState(String printState) {
        this.printState = printState;
    }

    public String getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(String downloadState) {
        this.downloadState = downloadState;
    }

    public String getArchiveContent() {
        return archiveContent;
    }

    public void setArchiveContent(String archiveContent) {
        this.archiveContent = archiveContent;
    }

    public String getWatermarkState() {
        return watermarkState;
    }

    public void setWatermarkState(String watermarkState) {
        this.watermarkState = watermarkState;
    }
    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getBorrowingCode() {
        return borrowingCode;
    }

    public void setBorrowingCode(String borrowingCode) {
        this.borrowingCode = borrowingCode;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(String organizeId) {
        this.organizeId = organizeId;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public Long getBorrowingTime() {
        return borrowingTime;
    }

    public void setBorrowingTime(Long borrowingTime) {
        this.borrowingTime = borrowingTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getReviewScope() {
        return reviewScope;
    }

    public void setReviewScope(String reviewScope) {
        this.reviewScope = reviewScope;
    }

    public String getKeywordString() {
        return keywordString;
    }

    public void setKeywordString(String keywordString) {
        this.keywordString = keywordString;
    }

    public Long getFinishTime() {
        return FinishTime;
    }

    public void setFinishTime(Long finishTime) {
        FinishTime = finishTime;
    }
}
