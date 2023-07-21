package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存报警信息表实体类
 *
 * @author hyj
 */
@Table(name = Constants.DZDNCQBC + "alarm", module = Constants.MODULE_NAME, comment = "电子档案长期保存报警信息表")
public class CqbcAlarm extends BaseStatusEntity<String> {

    @Column(comment = "报警类型", length = 200)
    private String alarmType;

    @Column(comment = "报警内容", length = 500)
    private String alarmContent;

    @Column(comment = "报警文件id", length = 100)
    private String fileId;

    @Column(comment = "档案id", length = 100)
    private String archiveId;

    @Column(comment = "报警时间", length = 200)
    private long alarmDate;

    @Column(comment = "任务名称", length = 200)
    private String taskName;

    @Column(comment = "分类信息id", length = 200)
    private String classificationId;

    @Column(comment = "处理人id", length = 200)
    private String processUserId;

    @Column(comment = "处理状态",length = 100)
    private String processState;

    @Column(comment = "处理人名称", length = 200)
    private String processUserName;

    @Column(comment = "处理时间", length = 200)
    private long processDate;

    @Column(comment = "处理结果", length = 200)
    private String processResult;

    @Column(comment = "机构id", length = 200)
    private String orgId;
    @Column(comment = "全宗编码", length = 200)
    private String fondCode;

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProcessUserName() {
        return processUserName;
    }

    public void setProcessUserName(String processUserName) {
        this.processUserName = processUserName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public long getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(long alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(String processUserId) {
        this.processUserId = processUserId;
    }

    public long getProcessDate() {
        return processDate;
    }

    public void setProcessDate(long processDate) {
        this.processDate = processDate;
    }
}
