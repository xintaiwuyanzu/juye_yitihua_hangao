package com.dr.archive.fuzhou.vo;

public class InformaintionVO {

    //档案主键
    public String archivesId;

    //档案题名
    private String archivesTitle;

    //归档时间
    private String archiveTime;

    //表单数据id
    private String formId;

    public String getFormDataId() {
        return formId;
    }

    public void setFormDataId(String formDataId) {
        this.formId = formDataId;
    }

    public String getArchivesId() {
        return archivesId;
    }

    public void setArchivesId(String archivesId) {
        this.archivesId = archivesId;
    }

    public String getArchivesTitle() {
        return archivesTitle;
    }

    public void setArchivesTitle(String archivesTitle) {
        this.archivesTitle = archivesTitle;
    }

    public String getArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(String archiveTime) {
        this.archiveTime = archiveTime;
    }
}
