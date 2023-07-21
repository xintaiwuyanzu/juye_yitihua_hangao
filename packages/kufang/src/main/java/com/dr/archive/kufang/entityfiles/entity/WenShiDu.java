package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "WENSHIDU", module = Constants.MODULE_NAME, comment = "温湿度管理")
public class WenShiDu extends BaseStatusEntity<String> {
    @Column(comment = "最低温度", length = 50)
    private String zuiDiWenDu;
    @Column(comment = "最高温度", length = 50)
    private String zuiGaoWenDu;
    @Column(comment = "最低湿度", length = 50)
    private String zuiDiShiDu;
    @Column(comment = "最高湿度", length = 50)
    private String zuiGaoShiDu;
    @Column(comment = "记录人姓名", length = 50)
    private String personName;
    @Column(comment = "相对湿度", length = 50)
    private String shiDu;
    @Column(comment = "温度", length = 50)
    private String wenDu;
    @Column(comment = "备注", length = 50)
    private String mark;
    @Column(comment = "库房名称", length = 200)
    private String kuFangMingCheng;
    @Column(comment = "库房号", length = 50)
    private String kuFangHao;
    @Column(comment = "设备名称", length = 200)
    private String sheBeiMingCheng;
    @Column(comment = "设备型号", length = 50)
    private String sheBeiXingHao;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getShiDu() {
        return shiDu;
    }

    public void setShiDu(String shiDu) {
        this.shiDu = shiDu;
    }

    public String getWenDu() {
        return wenDu;
    }

    public void setWenDu(String wenDu) {
        this.wenDu = wenDu;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getZuiDiWenDu() {
        return zuiDiWenDu;
    }

    public void setZuiDiWenDu(String zuiDiWenDu) {
        this.zuiDiWenDu = zuiDiWenDu;
    }

    public String getZuiGaoWenDu() {
        return zuiGaoWenDu;
    }

    public void setZuiGaoWenDu(String zuiGaoWenDu) {
        this.zuiGaoWenDu = zuiGaoWenDu;
    }

    public String getZuiDiShiDu() {
        return zuiDiShiDu;
    }

    public void setZuiDiShiDu(String zuiDiShiDu) {
        this.zuiDiShiDu = zuiDiShiDu;
    }

    public String getZuiGaoShiDu() {
        return zuiGaoShiDu;
    }

    public void setZuiGaoShiDu(String zuiGaoShiDu) {
        this.zuiGaoShiDu = zuiGaoShiDu;
    }

    public String getKuFangMingCheng() {
        return kuFangMingCheng;
    }

    public void setKuFangMingCheng(String kuFangMingCheng) {
        this.kuFangMingCheng = kuFangMingCheng;
    }

    public String getKuFangHao() {
        return kuFangHao;
    }

    public void setKuFangHao(String kuFangHao) {
        this.kuFangHao = kuFangHao;
    }

    public String getSheBeiMingCheng() {
        return sheBeiMingCheng;
    }

    public void setSheBeiMingCheng(String sheBeiMingCheng) {
        this.sheBeiMingCheng = sheBeiMingCheng;
    }

    public String getSheBeiXingHao() {
        return sheBeiXingHao;
    }

    public void setSheBeiXingHao(String sheBeiXingHao) {
        this.sheBeiXingHao = sheBeiXingHao;
    }
}
