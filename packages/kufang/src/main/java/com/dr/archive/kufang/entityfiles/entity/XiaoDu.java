package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX+"XIAODU",module = Constants.MODULE_NAME, comment = "档案消毒")
public class XiaoDu extends BaseStatusEntity<String> {
    @Column(comment = "消毒范围", length = 50)
    private String fanWei;
    @Column(comment = "消毒方式", length = 50)
    private String way;
    @Column(comment = "记录人", length = 50)
    private String personName;
    @Column(comment = "库房名称", length = 200)
    private String kuFangMingCheng;
    @Column(comment = "备注", length = 50)
    private String mark;

    @Column(comment = "消毒员", length = 50)
    private String disinfector;

    public String getDisinfector() {
        return disinfector;
    }

    public void setDisinfector(String disinfector) {
        this.disinfector = disinfector;
    }

    public String getFanWei() {
        return fanWei;
    }

    public void setFanWei(String fanWei) {
        this.fanWei = fanWei;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getKuFangMingCheng() {
        return kuFangMingCheng;
    }

    public void setKuFangMingCheng(String kuFangMingCheng) {
        this.kuFangMingCheng = kuFangMingCheng;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
