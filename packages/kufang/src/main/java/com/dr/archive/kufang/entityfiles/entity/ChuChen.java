package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX+"CHUCHEN",module = Constants.MODULE_NAME, comment = "档案除尘")
public class ChuChen extends BaseStatusEntity<String> {
    @Column(comment = "除尘情况", length = 50)
    private String chuChen;
    @Column(comment = "清理人", length = 50)
    private String cleaner;
    @Column(comment = "记录人", length = 50)
    private String personName;
    @Column(comment = "库房名称", length = 200)
    private String kuFangMingCheng;
    @Column(comment = "采取措施", length = 50)
    private String mark;

    public String getChuChen() {
        return chuChen;
    }

    public void setChuChen(String chuChen) {
        this.chuChen = chuChen;
    }

    public String getCleaner() {
        return cleaner;
    }

    public void setCleaner(String cleaner) {
        this.cleaner = cleaner;
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
