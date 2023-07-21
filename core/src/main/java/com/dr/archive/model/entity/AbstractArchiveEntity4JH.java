package com.dr.archive.model.entity;

import com.dr.framework.core.orm.annotations.Column;

/**
 * 件盒基本信息表
 *
 * @author jjl
 */
public class AbstractArchiveEntity4JH extends AbstractArchiveEntity {
   //济阳档案馆用，要求把件号改成室编件号
    @Column(name = COLUMN_JH, comment = "室编件号")
    private String JH;

    @Column(name = COLUMN_HH, comment = "盒号")
    private String HH;

    @Column(name = COLUMN_AJDH, comment = "案卷档号")
    private String AJDH;

    @Column(name = COLUMN_YH, comment = "页号")
    private String YH;

    @Column(name = COLUMN_FILECODE, comment = "文件编号")
    private String FILECODE;

    @Column(name = COLUMN_YS, comment = "页数", length = 100)
    private String YS;

    @Column(name = COLUMN_WZ, comment = "位置", length = 100)
    private String position;

    @Column(name = COLUMN_RM, comment = "人名", length = 100)
    private String personName;

    @Column(name = COLUMN_AJH, comment = "案卷号", length = 100)
    private String AJH;

    public String getFILECODE() {
        return FILECODE;
    }

    public void setFILECODE(String FILECODE) {
        this.FILECODE = FILECODE;
    }

    public String getAJDH() {
        return AJDH;
    }

    public void setAJDH(String AJDH) {
        this.AJDH = AJDH;
    }

    public String getYH() {
        return YH;
    }

    public void setYH(String YH) {
        this.YH = YH;
    }

    public String getJH() {
        return JH;
    }

    public void setJH(String JH) {
        this.JH = JH;
    }

    public String getHH() {
        return HH;
    }

    public void setHH(String HH) {
        this.HH = HH;
    }

    public String getYS() {
        return YS;
    }

    public void setYS(String YS) {
        this.YS = YS;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAJH() {
        return AJH;
    }

    public void setAJH(String AJH) {
        this.AJH = AJH;
    }
}
