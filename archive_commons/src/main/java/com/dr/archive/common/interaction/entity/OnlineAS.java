package com.dr.archive.common.interaction.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

@Table(name = Constants.COMMON_TABLE_PREFIX + "ONLINEAS", module = Constants.COMMON_MODULE_NAME, comment = "在线年检")
public class OnlineAS extends BaseStatusEntity<String> {

    @Column(comment = "组织机构代码ID", length = 200)
    private String organiseId;
    @Column(comment = "组织机构代码", length = 200)
    private String organiseCode;
    @Column(comment = "组织机构名称", length = 200)
    private String organiseName;

    @Column(comment = "年检名称", length = 200)
    private String nianjianName;
    @Column(comment = "年度", length = 200)
    private String nian;
    @Column(comment = "单位名称", length = 200)
    private String unitName;
    @Column(comment = "单位类别代码", length = 200)
    private String unitCode;
    @Column(comment = "单位负责人", length = 200)
    private String unitPerson;
    @Column(comment = "电话", length = 200)
    private String phone;
    @Column(comment = "详细地址", length = 200)
    private String address;

    @Column(comment = "档案室主任姓名", length = 200)
    private String archiveDirectorName;

    @Column(comment = "档案员姓名", length = 200)
    private String archivePersonName;

    @Column(comment = "文书文件级数据", length = 200)
    private String wenshuwenJianji;
    @Column(comment = "文书全文数据库", length = 200)
    private String wenshuQuanwen;

    @Column(comment = "业务条目数据库", length = 200)
    private String yewuTiaomu;
    @Column(comment = "业务全文数据", length = 200)
    private String yewuQuanwen;

    @Column(comment = "文书档案卷", length = 200)
    private String wemshuJuan;
    @Column(comment = "文书档案件", length = 200)
    private String wemshuJian;

    @Column(comment = "科技档案卷", length = 200)
    private String kejiJuan;
    @Column(comment = "科技档案件", length = 200)
    private String kejiJian;
    @Column(comment = "会计档案卷", length = 200)
    private String kuaijiJuan;
    @Column(comment = "会计档案件", length = 200)
    private String kuaijiJian;
    @Column(comment = "其他档案卷", length = 200)
    private String qitaJuan;
    @Column(comment = "其他档案件", length = 200)
    private String qitaJian;


    @Column(comment = "库房面积", length = 200)
    private String baoguanKufang;
    @Column(comment = "空调", length = 200)
    private String baoguanKongtiao;
    @Column(comment = "灭火器", length = 200)
    private String baoguanMiehuoqi;
    @Column(comment = "档案箱", length = 200)
    private String baoguanXiang;

    @Column(comment = "本单位自检情况", type = ColumnType.CLOB)
    private String zijian;

    @Column(comment = "年检结果", type = ColumnType.CLOB)
    private String nianjian;

    public String getNian() {
        return nian;
    }

    public void setNian(String nian) {
        this.nian = nian;
    }

    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }

    public String getNianjianName() {
        return nianjianName;
    }

    public void setNianjianName(String nianjianName) {
        this.nianjianName = nianjianName;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getOrganiseCode() {
        return organiseCode;
    }

    public void setOrganiseCode(String organiseCode) {
        this.organiseCode = organiseCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitPerson() {
        return unitPerson;
    }

    public void setUnitPerson(String unitPerson) {
        this.unitPerson = unitPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArchiveDirectorName() {
        return archiveDirectorName;
    }

    public void setArchiveDirectorName(String archiveDirectorName) {
        this.archiveDirectorName = archiveDirectorName;
    }

    public String getArchivePersonName() {
        return archivePersonName;
    }

    public void setArchivePersonName(String archivePersonName) {
        this.archivePersonName = archivePersonName;
    }

    public String getWenshuwenJianji() {
        return wenshuwenJianji;
    }

    public void setWenshuwenJianji(String wenshuwenJianji) {
        this.wenshuwenJianji = wenshuwenJianji;
    }

    public String getWenshuQuanwen() {
        return wenshuQuanwen;
    }

    public void setWenshuQuanwen(String wenshuQuanwen) {
        this.wenshuQuanwen = wenshuQuanwen;
    }

    public String getYewuTiaomu() {
        return yewuTiaomu;
    }

    public void setYewuTiaomu(String yewuTiaomu) {
        this.yewuTiaomu = yewuTiaomu;
    }

    public String getYewuQuanwen() {
        return yewuQuanwen;
    }

    public void setYewuQuanwen(String yewuQuanwen) {
        this.yewuQuanwen = yewuQuanwen;
    }

    public String getWemshuJuan() {
        return wemshuJuan;
    }

    public void setWemshuJuan(String wemshuJuan) {
        this.wemshuJuan = wemshuJuan;
    }

    public String getWemshuJian() {
        return wemshuJian;
    }

    public void setWemshuJian(String wemshuJian) {
        this.wemshuJian = wemshuJian;
    }

    public String getKejiJuan() {
        return kejiJuan;
    }

    public void setKejiJuan(String kejiJuan) {
        this.kejiJuan = kejiJuan;
    }

    public String getKejiJian() {
        return kejiJian;
    }

    public void setKejiJian(String kejiJian) {
        this.kejiJian = kejiJian;
    }

    public String getKuaijiJuan() {
        return kuaijiJuan;
    }

    public void setKuaijiJuan(String kuaijiJuan) {
        this.kuaijiJuan = kuaijiJuan;
    }

    public String getKuaijiJian() {
        return kuaijiJian;
    }

    public void setKuaijiJian(String kuaijiJian) {
        this.kuaijiJian = kuaijiJian;
    }

    public String getQitaJuan() {
        return qitaJuan;
    }

    public void setQitaJuan(String qitaJuan) {
        this.qitaJuan = qitaJuan;
    }

    public String getQitaJian() {
        return qitaJian;
    }

    public void setQitaJian(String qitaJian) {
        this.qitaJian = qitaJian;
    }

    public String getBaoguanKufang() {
        return baoguanKufang;
    }

    public void setBaoguanKufang(String baoguanKufang) {
        this.baoguanKufang = baoguanKufang;
    }

    public String getBaoguanKongtiao() {
        return baoguanKongtiao;
    }

    public void setBaoguanKongtiao(String baoguanKongtiao) {
        this.baoguanKongtiao = baoguanKongtiao;
    }

    public String getBaoguanMiehuoqi() {
        return baoguanMiehuoqi;
    }

    public void setBaoguanMiehuoqi(String baoguanMiehuoqi) {
        this.baoguanMiehuoqi = baoguanMiehuoqi;
    }

    public String getBaoguanXiang() {
        return baoguanXiang;
    }

    public void setBaoguanXiang(String baoguanXiang) {
        this.baoguanXiang = baoguanXiang;
    }

    public String getZijian() {
        return zijian;
    }

    public void setZijian(String zijian) {
        this.zijian = zijian;
    }

    public String getNianjian() {
        return nianjian;
    }

    public void setNianjian(String nianjian) {
        this.nianjian = nianjian;
    }
}
