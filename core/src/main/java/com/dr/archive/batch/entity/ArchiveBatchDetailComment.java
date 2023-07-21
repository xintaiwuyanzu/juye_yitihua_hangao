package com.dr.archive.batch.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 档案详情意见表
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAIL_COMMENT", comment = "档案详情意见表", module = Constants.MODULE_NAME)
public class ArchiveBatchDetailComment extends AbstractBatchItemDetailItem {
    @Column(comment = "创建人名称", length = 100)
    private String createPersonName;
    @Column(length = 100, comment = "环节实例Id")
    private String taskInstanceId;
    @Column(length = 500, name = "iComment", comment = "审核意见内容")
    private String comment;
    @Column(length = 200, comment = "审核意见类型")
    private String commentType;
    @Column(length = 200, comment = "能否查看原文")
    private boolean useFile;
    @Column(length = 500, comment = "扩展字段一")
    private String param1;
    @Column(length = 500, comment = "扩展字段二")
    private String param2;
    @Column(length = 500, comment = "扩展字段三")
    private String param3;
    @Column(length = 500, comment = "扩展字段四")
    private String param4;
    @Column(length = 1000, comment = "扩展字段五")
    private String param5;

    public boolean isUseFile() {
        return useFile;
    }

    public void setUseFile(boolean useFile) {
        this.useFile = useFile;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getTaskInstanceId() {
        return taskInstanceId;
    }

    public void setTaskInstanceId(String taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }
}
