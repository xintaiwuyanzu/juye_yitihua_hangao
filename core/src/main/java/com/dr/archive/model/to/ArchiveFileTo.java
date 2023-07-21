package com.dr.archive.model.to;

import com.dr.archive.enums.CategoryType;
import com.dr.archive.enums.FileCreateType;

/**
 * 附件对象，附件需要排序，是个双链表
 *
 * @author dr
 */
public class ArchiveFileTo extends BaseOrderTo {
    /**
     * ===========================================
     * 文件基本信息
     * ===========================================
     */
    /**
     * 文件后缀
     */
    private String suffix;
    /**
     * 文件类型
     */
    private String mime;
    /**
     * 文件大小
     */
    private long fileSize;
    /**
     * 文件创建时间
     */
    private long createTime;
    /**
     * 文件描述
     */
    private String description;

    /**
     * ===========================================
     * 业务关联信息
     * ===========================================
     */
    /**
     * 原始文件Id
     */
    private String srcId;
    /**
     * 下一个文件Id
     */
    private String nextId;
    /**
     * 上一个文件Id
     */
    private String preId;
    /**
     * 业务外键
     * 具体一条数据的Id
     */
    private String businessId;
    /**
     * 哪张表
     */
    private String formId;
    /**
     * 门类id
     */
    private String categoryId;
    /**
     * 存储方案Id
     */
    private String storageSchemeId;
    /**
     * ===========================================
     * 分类类型信息
     * ===========================================
     */

    /**
     * 档案类型
     */
    private CategoryType categoryType;
    /**
     * 文件创建类型
     */
    private FileCreateType createType;
    /**
     * 文件分组编码
     */
    private String createGroupCode;
    /**
     * 业务类型分组
     */
    private String businessGroupCode;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStorageSchemeId() {
        return storageSchemeId;
    }

    public void setStorageSchemeId(String storageSchemeId) {
        this.storageSchemeId = storageSchemeId;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public FileCreateType getCreateType() {
        return createType;
    }

    public void setCreateType(FileCreateType createType) {
        this.createType = createType;
    }

    public String getCreateGroupCode() {
        return createGroupCode;
    }

    public void setCreateGroupCode(String createGroupCode) {
        this.createGroupCode = createGroupCode;
    }

    public String getBusinessGroupCode() {
        return businessGroupCode;
    }

    public void setBusinessGroupCode(String businessGroupCode) {
        this.businessGroupCode = businessGroupCode;
    }
}
