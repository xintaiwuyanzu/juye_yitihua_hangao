package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 长期保存检测记录详情
 *
 * @author dr
 */
@Table(name = Constants.DZDNCQBC + "task_detail", comment = "检测详情", module = Constants.MODULE_NAME)
public class EArchiveDetectTaskDetail extends BaseStatusEntity<String> {
    @Column(comment = "检测任务")
    private String taskId;

    @Column(comment = "电子档案数据Id")
    private String archiveId;
    @Column(comment = "电子档案题名")
    private String archiveTitle;
    @Column(comment = "电子档案档号")
    private String archiveCode;
    @Column(comment = "检测结果描述")
    private String description;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getArchiveTitle() {
        return archiveTitle;
    }

    public void setArchiveTitle(String archiveTitle) {
        this.archiveTitle = archiveTitle;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
