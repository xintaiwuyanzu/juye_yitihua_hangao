package com.dr.archive.managefondsdescriptivefile.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-02-23 20:43
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "MANAGEMENT_HISTORY", module = Constants.MODULE_NAME, comment = "全宗卷历史表")
public class ManagementHistory extends Management {
    @Column(comment = "全宗卷Id", length = 50)
    private String managementId;
    @Column(comment = "流程实例Id", length = 50)
    private String processInstanceId;
    @Column(comment = "环节实例Id", length = 50)
    private String taskInstanceId;
    /**
     * 1：添加
     * 2：修改
     * 3：删除
     */
    @Column(comment = "类型", length = 50)
    private String historyType;
    /**
     * 1通过 0不通过
     */
    @Column(comment = "是否通过", length = 10)
    private String isPass;
    @Column(comment = "审核意见", type = ColumnType.CLOB)
    private String examinationOpinion;
    @Column(comment = "添加人姓名", length = 200)
    private String createPersonName;

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskInstanceId() {
        return taskInstanceId;
    }

    public void setTaskInstanceId(String taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getExaminationOpinion() {
        return examinationOpinion;
    }

    public void setExaminationOpinion(String examinationOpinion) {
        this.examinationOpinion = examinationOpinion;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }
}
