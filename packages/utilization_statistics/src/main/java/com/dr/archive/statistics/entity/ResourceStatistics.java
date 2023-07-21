package com.dr.archive.statistics.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author Mr.Zhu
 * @date 2022/4/18 - 10:22
 */
@Table(name = Constants.TABLE_PREFIX + "RESOURECE_STATISTICS", module = Constants.MODULE_NAME, comment = "档案资源统计")
public class ResourceStatistics extends BaseStatusEntity<String> {
    @Column(comment = "全宗id")
    private String fondId;
    @Column(comment = "全宗编码")
    private String fondCode;
    @Column(comment = "全宗名称")
    private String fondName;
    @Column(comment = "分类id")
    private String categoryId;
    @Column(comment = "分类编码")
    private String categoryCode;
    @Column(comment = "分类名称")
    private String categoryName;
    @Column(comment = "案卷目录数量", length = 100)
    private long arcArchiveNum;
    @Column(comment = "文件目录数量", length = 100)
    private long fileArchiveNum;
    @Column(comment = "案卷原文数量", length = 100)
    private long arcFileNum;
    @Column(comment = "文件原文数量", length = 100)
    private long fileFileNum;
    @Column(comment = "案卷原文大小", length = 100)
    private long arcFileSize;
    @Column(comment = "文件原文大小", length = 100)
    private long fileFileSize;
    /**
     * 接收暂存库、管理库
     */
    @Column(comment = "所在库", length = 50)
    private String archiveStatus;
    @Column(comment = "原文数量", length = 100)
    private long filesNum;
    @Column(comment = "电子文件大小", length = 100)
    private long fileSize;
    @Column(comment = "档案年度", length = 50)
    private String vintages;
    @Column(comment = "文件形成日期", length = 50)
    private String fileTime;
    @Column(comment = "机构id")
    private String orgId;
    @Column(comment = "机构名称")
    private String orgName;
    @Column(comment = "保管期限")
    private String saveTerm;
    /**
     * 按卷、按件
     */
    @Column(comment = "整理方式", length = 50)
    private String collatedForm;
    /**
     * 电子文件、数字化副本
     */
    @Column(comment = "载体类型", length = 50)
    private String carrierType;

    public ResourceStatistics() {

    }

    /**
     * @param fondCode       全宗号
     * @param categoryCode   分类号
     * @param arcArchiveNum  案卷档案目录数量
     * @param arcFileNum     案卷档案原文数量
     * @param arcFileSize    案卷档案原文大小
     * @param fileArchiveNum 文件档案目录数量
     * @param fileFileNum    文件档案原文数量
     * @param fileFileSize   文件档案原文大小
     * @param orgId          机构id
     */
    public ResourceStatistics(String fondCode, String categoryCode, long arcArchiveNum, long arcFileNum, long arcFileSize, long fileArchiveNum, long fileFileNum, long fileFileSize, String vintages,String orgId) {
        this.fondCode = fondCode;
        this.categoryCode = categoryCode;
        this.arcArchiveNum = arcArchiveNum;
        this.arcFileNum = arcFileNum;
        this.arcFileSize = arcFileSize;
        this.fileArchiveNum = fileArchiveNum;
        this.fileFileNum = fileFileNum;
        this.fileFileSize = fileFileSize;
        this.vintages = vintages;
        this.orgId=orgId;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getArcArchiveNum() {
        return arcArchiveNum;
    }

    public void setArcArchiveNum(long arcArchiveNum) {
        this.arcArchiveNum = arcArchiveNum;
    }

    public long getFileArchiveNum() {
        return fileArchiveNum;
    }

    public void setFileArchiveNum(long fileArchiveNum) {
        this.fileArchiveNum = fileArchiveNum;
    }

    public long getArcFileNum() {
        return arcFileNum;
    }

    public void setArcFileNum(long arcFileNum) {
        this.arcFileNum = arcFileNum;
    }

    public long getFileFileNum() {
        return fileFileNum;
    }

    public void setFileFileNum(long fileFileNum) {
        this.fileFileNum = fileFileNum;
    }

    public long getArcFileSize() {
        return arcFileSize;
    }

    public void setArcFileSize(long arcFileSize) {
        this.arcFileSize = arcFileSize;
    }

    public long getFileFileSize() {
        return fileFileSize;
    }

    public void setFileFileSize(long fileFileSize) {
        this.fileFileSize = fileFileSize;
    }

    public String getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(String archiveStatus) {
        this.archiveStatus = archiveStatus;
    }

    public long getFilesNum() {
        return filesNum;
    }

    public void setFilesNum(long filesNum) {
        this.filesNum = filesNum;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getVintages() {
        return vintages;
    }

    public void setVintages(String vintages) {
        this.vintages = vintages;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
    }

    public String getCollatedForm() {
        return collatedForm;
    }

    public void setCollatedForm(String collatedForm) {
        this.collatedForm = collatedForm;
    }

    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }
}
