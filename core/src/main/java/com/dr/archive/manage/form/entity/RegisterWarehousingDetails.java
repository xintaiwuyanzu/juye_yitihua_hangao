package com.dr.archive.manage.form.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "Register_Warehousing_Details", module = Constants.MODULE_NAME, comment = "登记出入库记录详情表")
public class RegisterWarehousingDetails extends BaseStatusEntity<String> {

    @Column(comment = "记录id", length = 100,order = 10)
    private String bId;

    @Column(comment = "题名", length = 100,order = 11)
    private String TIMING;

    @Column(comment = "档号", length = 100,order = 12)
    private String archiveCode;

    @Column(comment = "全宗号", length = 100,order = 13)
    private String fondCode;

    @Column(comment = "全宗Id",order = 14)
    private String fondId;

    @Column(comment = "全宗名称", length = 100,order = 15)
    private String fondName;

    @Column(comment = "年度", length = 100,order = 16)
    private String ND;
    @Column(comment = "保管期限", length = 100,order = 17)
    private String BGQX;

    @Column(comment = "文件形成日期", length = 100,order = 18)
    private String WJXCRQ;

    @Column(comment = "档案类型", length = 100)
    private String archiveType;

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    public String getAJDH() {
        return AJDH;
    }

    public void setAJDH(String AJDH) {
        this.AJDH = AJDH;
    }

    /**
     * 卷内文件存放的
     */
    @Column(comment = "案卷档案", length = 100)
    private String AJDH;

    @Column(comment = "备注", length = 100,order = 18)
    private String BZ;

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getTIMING() {
        return TIMING;
    }

    public void setTIMING(String TIMING) {
        this.TIMING = TIMING;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getND() {
        return ND;
    }

    public void setND(String ND) {
        this.ND = ND;
    }

    public String getBGQX() {
        return BGQX;
    }

    public void setBGQX(String BGQX) {
        this.BGQX = BGQX;
    }

    public String getWJXCRQ() {
        return WJXCRQ;
    }

    public void setWJXCRQ(String WJXCRQ) {
        this.WJXCRQ = WJXCRQ;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }
}
