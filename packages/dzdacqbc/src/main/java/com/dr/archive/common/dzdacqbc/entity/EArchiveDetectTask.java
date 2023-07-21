package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 长期保存检测任务
 *
 * @author dr
 */
@Table(name = Constants.DZDNCQBC + "task", comment = "检测详情", module = Constants.MODULE_NAME)
public class EArchiveDetectTask extends BaseStatusEntity<String> {
    @Column(comment = "档案分类Id")
    private String classifiId;
    @Column(comment = "档案分类名称")
    private String classifiName;
    @Column(comment = "检测任务名称")
    private String taskName;
    @Column(comment = "检测类型")
    private String detectType;
    /**
     * 这个值需要除以100
     */
    @Column(comment = "抽样检测比例")
    private long detectProportion;
    @Column(comment = "检测开始时间", type = ColumnType.DATE)
    private long startDate;
    @Column(comment = "检测结束时间", type = ColumnType.DATE)
    private long endDate;
    @Column(comment = "检测目标数量")
    private long targetTotal;
    @Column(comment = "已检测数量")
    private long currentCount;

    public String getClassifiId() {
        return classifiId;
    }

    public void setClassifiId(String classifiId) {
        this.classifiId = classifiId;
    }

    public String getClassifiName() {
        return classifiName;
    }

    public void setClassifiName(String classifiName) {
        this.classifiName = classifiName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDetectType() {
        return detectType;
    }

    public void setDetectType(String detectType) {
        this.detectType = detectType;
    }

    public long getDetectProportion() {
        return detectProportion;
    }

    public void setDetectProportion(long detectProportion) {
        this.detectProportion = detectProportion;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getTargetTotal() {
        return targetTotal;
    }

    public void setTargetTotal(long targetTotal) {
        this.targetTotal = targetTotal;
    }

    public long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(long currentCount) {
        this.currentCount = currentCount;
    }
}
