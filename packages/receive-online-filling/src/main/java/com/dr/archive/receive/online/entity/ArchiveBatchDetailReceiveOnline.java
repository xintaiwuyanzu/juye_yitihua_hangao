package com.dr.archive.receive.online.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 批次接收预归档详情
 * status: 1 接收归档文件成功 2 归档中  3 归档成功  4 归档失败
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAIL_RECEIPT", comment = "预归档详情", module = Constants.MODULE_NAME)
public class ArchiveBatchDetailReceiveOnline extends AbstractArchiveReceiveDetail {

    @Column(comment = "系统编号", length = 100)
    private String systemNum;
    @Column(comment = "系统名称", length = 200)
    private String systemName;
    @Column(comment = "业务主键", length = 100)
    private String businessId;
    @Column(comment = "ofd数据包名", length = 300)
    private String ofdName;
    @Column(name = "OFD_PATH", comment = "下载地址", length = 500)
    private String path;
    @Column(comment = "数字摘要", length = 300)
    private String digitaldigest;
    @Column(comment = "预归档完成时间", length = 100)
    private Long preArchiveDate;
    @Column(comment = "预归档完成人", length = 200)
    private String preArchivePerson;
    @Column(comment = "事项版本号", length = 100)
    private String taskVersion;
    @Column(comment = "事项类型", length = 100)
    private String tasktype;
    @Column(comment = "行政区划编码", length = 100)
    private String regionCode;
    @Column(comment = "政务服务事项基本编码", length = 100)
    private String taskCode;
    @Column(comment = "事项名称", length = 300)
    private String taskmname;
    @Column(comment = "统一社会信用代码", length = 100)
    private String socialCode;
    @Column(comment = "四性检测状态", length = 10)
    private String testStatus;
    @Column(comment = "具体执行了多少项检测")
    private long totalCount;
    @Column(comment = "成功通过数量")
    private long successCount;

    @Column(comment = "最后一次四性检测id")
    private String lastTestRecordId;

    public String getTaskmname() {
        return taskmname;
    }

    public void setTaskmname(String taskmname) {
        this.taskmname = taskmname;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getTaskVersion() {
        return taskVersion;
    }

    public void setTaskVersion(String taskVersion) {
        this.taskVersion = taskVersion;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getSocialCode() {
        return socialCode;
    }

    public void setSocialCode(String socialCode) {
        this.socialCode = socialCode;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOfdName() {
        return ofdName;
    }

    public void setOfdName(String ofdName) {
        this.ofdName = ofdName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDigitaldigest() {
        return digitaldigest;
    }

    public void setDigitaldigest(String digitaldigest) {
        this.digitaldigest = digitaldigest;
    }

    public Long getPreArchiveDate() {
        return preArchiveDate;
    }

    public void setPreArchiveDate(Long preArchiveDate) {
        this.preArchiveDate = preArchiveDate;
    }

    public String getPreArchivePerson() {
        return preArchivePerson;
    }

    public void setPreArchivePerson(String preArchivePerson) {
        this.preArchivePerson = preArchivePerson;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public String getLastTestRecordId() {
        return lastTestRecordId;
    }

    public void setLastTestRecordId(String lastTestRecordId) {
        this.lastTestRecordId = lastTestRecordId;
    }
}
