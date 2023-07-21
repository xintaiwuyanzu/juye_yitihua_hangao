package com.dr.archive.model.entity;

import com.dr.framework.core.orm.annotations.Column;

/**
 * 文件基本信息表
 *
 * @author jjl
 */
public class AbstractArchiveEntity4WJ extends AbstractArchiveEntity {
    @Column(name = COLUMN_AJH, comment = "案卷号")
    private String AJH;

    @Column(name = COLUMN_AJDH, comment = "案卷档号")
    private String AJDH;

    @Column(name = COLUMN_YH, comment = "页号")
    private String YH;

    @Column(name = COLUMN_FILECODE, comment = "文号")
    private String FILECODE;

    public String getFILECODE() {
        return FILECODE;
    }

    public void setFILECODE(String FILECODE) {
        this.FILECODE = FILECODE;
    }

    public String getAJH() {
        return AJH;
    }

    public void setAJH(String AJH) {
        this.AJH = AJH;
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


}
