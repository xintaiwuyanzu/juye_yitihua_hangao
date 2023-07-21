package com.dr.archive.manage.log.entity;

import com.dr.framework.common.entity.BaseOrderedEntity;
import com.dr.framework.common.form.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author caor
 * @date 2020/7/31 10:26
 */
@Table(name = "PUB_LOG", module = Constants.MODULE_NAME, comment = "系统日志表")
public class SysLogEntity extends BaseOrderedEntity {

    @Column(comment = "方法名", name = "LOGMETHOD", length = 300, order = 1)
    private String method;
    @Column(comment = "操作", name = "LOGOPERATION", length = 2000, order = 2)
    private String operation;
    @Column(comment = "参数", name = "LOGPARAMS", length = 300, order = 3)
    private String params;
    @Column(comment = "ip地址", name = "LOGIP", length = 50, order = 4)
    private String ip;
    @Column(comment = "操作审计" ,name = "OPERATIONAUDIT", length = 100,order = 5)
    private String operationAudit;
    @Column(comment = "操作审计", name = "AUDITDESCRIPTION", length = 100, order = 6)
    private String auditDescription;
    @Column(comment = "机构", name = "ORGANISEID", length = 100, order = 6)
    private String organiseId;

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getAuditDescription() {
        return auditDescription;
    }

    public void setAuditDescription(String auditDescription) {
        this.auditDescription = auditDescription;
    }

    public String getOperationAudit() {
        return operationAudit;
    }

    public void setOperationAudit(String operationAudit) {
        this.operationAudit = operationAudit;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
