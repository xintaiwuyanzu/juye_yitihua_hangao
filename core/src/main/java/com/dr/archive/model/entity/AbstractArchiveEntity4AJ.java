package com.dr.archive.model.entity;

import com.dr.framework.core.orm.annotations.Column;


/**
 * 案卷基本表
 *
 * @author jjl
 */
public class AbstractArchiveEntity4AJ extends AbstractArchiveEntity {
    @Column(name = COLUMN_AJH, comment = "案卷号")
    private String AJH;
    @Column(name = COLUMN_YS, comment = "页数")
    private String YS;
    @Column(name = COLUMN_WJFS, comment = "件数")
    private String WJFS;
    @Column(name = COLUMN_TIME, comment = "时间（卷内文件所属的起止年月）")
    private String TIME;
    @Column(name = COLUMN_LJR, comment = "立卷人")
    private String LJR;
    @Column(name = COLUMN_JCR, comment = "检查人")
    private String JCR;
    @Column(name = COLUMN_LJSJ, comment = "立卷时间")
    private String LJSJ;

    public String getYS() {
        return YS;
    }

    public void setYS(String YS) {
        this.YS = YS;
    }

    public String getAJH() {
        return AJH;
    }

    public void setAJH(String AJH) {
        this.AJH = AJH;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getWJFS() {
        return WJFS;
    }

    public void setWJFS(String WJFS) {
        this.WJFS = WJFS;
    }

    public String getLJR() {
        return LJR;
    }

    public void setLJR(String LJR) {
        this.LJR = LJR;
    }

    public String getJCR() {
        return JCR;
    }

    public void setJCR(String JCR) {
        this.JCR = JCR;
    }

    public String getLJSJ() {
        return LJSJ;
    }

    public void setLJSJ(String LJSJ) {
        this.LJSJ = LJSJ;
    }
}
