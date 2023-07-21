package com.dr.archive.entity.task;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-06-17 15:27
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_Task", module = Constants.MODULE_NAME, comment = "定时任务")
public class Task extends BaseStatusEntity<String> {
    @Column(comment = "元数据id")
    private String dataResourceId;//（dataMark表示标注数据，relationMark表示梳理关系）
    @Column(comment = "任务类型")
    private String taskType;//（dataMark表示标注数据，relationMark表示梳理关系）
    @Column(comment = "参数", type = ColumnType.CLOB)
    private String params;
    @Column(comment = "分页数据当前页")
    private int pageIndex;
    @Column(comment = "分页数据大小")
    private long pageSize;
    @Column(comment = "需要处理的总数")
    private long totalSize;
    @Column(comment = "完成时间")
    private long endDate;
    @Column(comment = "是否启用")
    private int isOpen;

    public Task() {
    }

    public Task(String dataResourceId, String taskType, String params, int pageIndex, long pageSize, long totalSize, long endDate, int isOpen) {
        this.dataResourceId = dataResourceId;
        this.taskType = taskType;
        this.params = params;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.endDate = endDate;
        this.isOpen = isOpen;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getDataResourceId() {
        return dataResourceId;
    }

    public void setDataResourceId(String dataResourceId) {
        this.dataResourceId = dataResourceId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}
