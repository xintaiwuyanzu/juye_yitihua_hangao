package com.dr.archive.fuzhou.ofd.bo;

/**
 * 这个参数只是用来传给ofd回调使用的，
 * 传给本类里面传给ofd的参数都会原样传回来，真正的参数使用这里面的关键Id查询
 *
 * @author dr
 */
public class WaterMarkBo {
    /**
     * 文件Id
     */
    private String fileId;
    /**
     * 水印管理Id
     */
    private String waterMarkId;
    /**
     * 测试
     */
    private String watermarkConfig;
    /**
     * 全宗Id
     */
    private String fondId;
    /**
     * 门类Id
     */
    private String categoryId;
    /**
     * 当前登录用户Id
     */
    private String userId;
    //TODO 表单Id
    /**
     * 文件下载地址
     */
    private String fileUrl;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getWaterMarkId() {
        return waterMarkId;
    }

    public void setWaterMarkId(String waterMarkId) {
        this.waterMarkId = waterMarkId;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getWatermarkConfig() {
        return watermarkConfig;
    }

    public void setWatermarkConfig(String watermarkConfig) {
        this.watermarkConfig = watermarkConfig;
    }
}
