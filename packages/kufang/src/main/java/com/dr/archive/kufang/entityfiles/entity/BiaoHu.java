package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX+"BIAOHU",module = Constants.MODULE_NAME, comment = "档案裱糊")
public class BiaoHu extends BaseStatusEntity<String> {
    @Column(comment = "数量", length = 50)
    private String num;
    @Column(comment = "档号", length = 50)
    private String archiveCode;
    @Column(comment = "记录人", length = 50)
    private String personName;
    @Column(comment = "交卷人", length = 200)
    private String jiaoJuan ;
    @Column(comment = "收卷人", length = 200)
    private String shouJuan;
    @Column(comment = "库房名称", length = 200)
    private String kuFangMingCheng;
    @Column(comment = "备注", length = 50)
    private String mark;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getJiaoJuan() {
        return jiaoJuan;
    }

    public void setJiaoJuan(String jiaoJuan) {
        this.jiaoJuan = jiaoJuan;
    }

    public String getShouJuan() {
        return shouJuan;
    }

    public void setShouJuan(String shouJuan) {
        this.shouJuan = shouJuan;
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
