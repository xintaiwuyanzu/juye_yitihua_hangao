package com.dr.archive.model.entity;

import com.dr.framework.core.orm.annotations.Column;

/**
 * 档案表抽象父类，用来统一规定各种类型的档案基本字段
 * <p>
 * 业务表可以继承这个类，然后添加自己的字段即可
 * <p>
 *
 * <strong>
 * 这个类可以用来做业务逻辑判断，增删改查不能用该类
 * <strong/>
 *
 * @author dr
 */
public class AbstractArchiveEntity extends AbstractArchiveEntityOther {

    @Column(name = COLUMN_TITLE, comment = "题名", length = 800)
    private String title;
    @Column(name = COLUMN_ARCHIVE_CODE, comment = "档号")
    private String archiveCode;
    @Column(name = COLUMN_ORG_CODE, comment = "机构或问题")
    private String orgCode;
    @Column(name = COLUMN_YEAR, comment = "年度", length = 100)
    private String year;
    @Column(name = COLUMN_FILETIME, comment = "文件形成日期", length = 100)
    private String fileTime;
    @Column(name = COLUMN_SAVE_TERM, comment = "保管期限", length = 100)
    private String saveTerm;
    @Column(name = COLUMN_SECURITY_LEVEL, comment = "密级", length = 100)
    private String securityLevel;
    @Column(name = COLUMN_DUTY_PERSON, comment = "责任者", length = 1000)
    private String dutyPerson;
    @Column(name = COLUMN_NOTE, comment = "备注", length = 1000)
    private String note;

    @Column(name = COLUMN_WJLX, comment = "文件类型", length = 200)
    private String WJLX;


    //    @Column(name = COLUMN_BQ, comment = "标签", length = 1000)
//    private String TAG;
//
//    public String getTAG() {
//        return TAG;
//    }
//
//    public void setTAG(String TAG) {
//        this.TAG = TAG;
//    }

    public String getWJLX() {
        return WJLX;
    }

    public void setWJLX(String WJLX) {
        this.WJLX = WJLX;
    }


    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

}
