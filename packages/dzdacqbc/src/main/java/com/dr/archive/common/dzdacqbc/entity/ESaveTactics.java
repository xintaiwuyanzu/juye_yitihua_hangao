package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存全局策略表实体类
 *
 * @author hyj
 */

@Table(name = Constants.DZDNCQBC + "tactics", module = Constants.MODULE_NAME, comment = "电子档案长期保存全局策略表")
public class ESaveTactics extends BaseStatusEntity<String> {

    @Column(comment = "全局检测周期", length = 200)
    private String globalDetection;

    @Column(comment = "抽样检测周期", length = 200)
    private String sampleDetection;

    @Column(comment = "抽样检测比例", length = 200)
    private String sampleProportion;

    @Column(comment = "提醒离线备份周期", length = 200)
    private String remindBackups;

    @Column(comment = "存储报警阈值", length = 200)
    private String alarmValue;

    @Column(comment = "策略名称", length = 50)
    private String tacticsName;

    @Column(comment = "是否为默认 1：是；0：否", length = 10)
    private String isDefault;

    @Column(comment = "是否已启用 1：是；0：否", length = 10)
    private String isStart;

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getTacticsName() {
        return tacticsName;
    }

    public void setTacticsName(String tacticsName) {
        this.tacticsName = tacticsName;
    }

    public String getGlobalDetection() {
        return globalDetection;
    }

    public void setGlobalDetection(String globalDetection) {
        this.globalDetection = globalDetection;
    }

    public String getSampleDetection() {
        return sampleDetection;
    }

    public void setSampleDetection(String sampleDetection) {
        this.sampleDetection = sampleDetection;
    }

    public String getSampleProportion() {
        return sampleProportion;
    }

    public void setSampleProportion(String sampleProportion) {
        this.sampleProportion = sampleProportion;
    }

    public String getRemindBackups() {
        return remindBackups;
    }

    public void setRemindBackups(String remindBackups) {
        this.remindBackups = remindBackups;
    }

    public String getAlarmValue() {
        return alarmValue;
    }

    public void setAlarmValue(String alarmValue) {
        this.alarmValue = alarmValue;
    }
}
