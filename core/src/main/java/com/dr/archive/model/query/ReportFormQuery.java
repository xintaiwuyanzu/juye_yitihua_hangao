package com.dr.archive.model.query;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/19 16:58
 */
public class ReportFormQuery {
    /**
     * 创建时间
     */
    private long createDate;
    /**
     * 起始时间
     */
    private long startTime;
    private long endTime;
    private String storagePeriod;
    private String title;
    private String annual;
    private String fondCode;
    private String fondName;
    private String company;
    /**
     * 部门
     */
    private String organizationId;
    /**
     * 案卷id
     */
    private String archiveId;
    /**
     * 开放等级
     */
    private String openClass;
    /**
     * 保管期限
     */
    private String retention;
    /**
     * 文件格式
     */
    private String fileFormat;
    /**
     * 格式文件数量
     */
    private String fileFormatSum;
    /**
     * 附件容量
     */
    private long fileCapacity;

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getStoragePeriod() {
        return storagePeriod;
    }

    public void setStoragePeriod(String storagePeriod) {
        this.storagePeriod = storagePeriod;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnual() {
        return annual;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getOpenClass() {
        return openClass;
    }

    public void setOpenClass(String openClass) {
        this.openClass = openClass;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileFormatSum() {
        return fileFormatSum;
    }

    public void setFileFormatSum(String fileFormatSum) {
        this.fileFormatSum = fileFormatSum;
    }

    public long getFileCapacity() {
        return fileCapacity;
    }

    public void setFileCapacity(long fileCapacity) {
        this.fileCapacity = fileCapacity;
    }
}
