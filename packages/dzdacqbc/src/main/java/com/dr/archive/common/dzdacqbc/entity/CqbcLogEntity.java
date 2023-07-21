package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存日志信息表实体类
 *
 * @author hyj
 */

@Table(name = Constants.DZDNCQBC + "log", module = Constants.MODULE_NAME, comment = "电子档案长期保存日志信息表")
public class CqbcLogEntity extends BaseStatusEntity<String> {

    @Column(comment = "任务信息Id", length = 200)
    private String taskId;

    @Column(comment = "数据包Id", length = 200)
    private String archiveId;

    @Column(name = "LogMethod", comment = "方法名称", length = 200)
    private String method;

    @Column(comment = "操作类型", length = 200)
    private String operateType;

    @Column(comment = "操作内容", length = 1000)
    private String content;

    @Column(comment = "操作时间", length = 200)
    private String operateDate;

    @Column(comment = "操作人", length = 200)
    private String operatePerson;

    @Column(comment = "ip地址", length = 100)
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
