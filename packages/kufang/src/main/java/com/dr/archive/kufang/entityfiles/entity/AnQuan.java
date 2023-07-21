package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "ANQUAN", module = Constants.MODULE_NAME, comment = "安全登记检查")
public class AnQuan extends BaseStatusEntity<String> {
    @Column(comment = "防火情况", length = 50)
    private String fangHuo;
    @Column(comment = "防盗情况", length = 50)
    private String fangDao;
    @Column(comment = "记录人", length = 50)
    private String personName;
    @Column(comment = "库房名称", length = 200)
    private String kuFangMingCheng;
    @Column(comment = "采取措施", length = 50)
    private String mark;

    public String getFangHuo() {
        return fangHuo;
    }

    public void setFangHuo(String fangHuo) {
        this.fangHuo = fangHuo;
    }

    public String getFangDao() {
        return fangDao;
    }

    public void setFangDao(String fangDao) {
        this.fangDao = fangDao;
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
