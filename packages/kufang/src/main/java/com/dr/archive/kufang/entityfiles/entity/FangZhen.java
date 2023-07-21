package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "FANGZHEN", module = Constants.MODULE_NAME, comment = "档案仿真")
public class FangZhen extends BaseStatusEntity<String> {
    @Column(comment = "任务名称", length = 50)
    private String taskName;
    @Column(comment = "申请单位", length = 50)
    private String unit;
    @Column(comment = "库房名称", length = 50)
    private String kuFangMingCheng;
    @Column(comment = "申请时间", length = 50)
    private String unitTime;
    @Column(comment = "记录人", length = 50)
    private String personName;
    @Column(comment = "题名", length = 200)
    private String title;
    @Column(comment = "档号", length = 100)
    private String archiveCode;
    @Column(comment = "复制用途", length = 50)
    private String copyPurpose;
    @Column(comment = "仿真要求", length = 50)
    private String copyRequire;
    @Column(comment = "处理情况", length = 50)
    private String situation;
    @Column(comment = "备注", length = 50)
    private String mark;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getKuFangMingCheng() {
        return kuFangMingCheng;
    }

    public void setKuFangMingCheng(String kuFangMingCheng) {
        this.kuFangMingCheng = kuFangMingCheng;
    }

    public String getUnitTime() {
        return unitTime;
    }

    public void setUnitTime(String unitTime) {
        this.unitTime = unitTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getCopyPurpose() {
        return copyPurpose;
    }

    public void setCopyPurpose(String copyPurpose) {
        this.copyPurpose = copyPurpose;
    }

    public String getCopyRequire() {
        return copyRequire;
    }

    public void setCopyRequire(String copyRequire) {
        this.copyRequire = copyRequire;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
