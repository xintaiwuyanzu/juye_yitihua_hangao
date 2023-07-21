package com.dr.archive.model.to;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/9/14 17:57
 */
public class ImportantFileTo {
    private String id;
    private String srcName;
    private String tarName;
    private String fileType;
    private String contentType;
    private String title;
    private String filePath;
    private String rootDir;
    private long formationTime;
    private long fileSize;
    private Integer seq;
    private String businessType;
    private String businessId;
    private String batch;

    private String page;

    private String daXiao;

    private String chiCun;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getTarName() {
        return tarName;
    }

    public void setTarName(String tarName) {
        this.tarName = tarName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public long getFormationTime() {
        return formationTime;
    }

    public void setFormationTime(long formationTime) {
        this.formationTime = formationTime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getDaXiao() {
        return daXiao;
    }

    public void setDaXiao(String daXiao) {
        this.daXiao = daXiao;
    }

    public String getChiCun() {
        return chiCun;
    }

    public void setChiCun(String chiCun) {
        this.chiCun = chiCun;
    }
}
