package com.dr.archive.receive.offline.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "Code_Log_Sheet", module = Constants.MODULE_NAME, comment = "离线导入档号出错记录表")
public class CodeLogSheet extends BaseStatusEntity<String> {

    @Column(comment = "批次id", length = 100, order = 10)
    private String batchId;

    @Column(comment = "报错原因",length = 100,order = 1)
    private String error_reason;

    public CodeLogSheet(String batchId,String error_reason){
        setBatchId(batchId);
        setError_reason(error_reason);
    }
    public CodeLogSheet(){};

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getError_reason() {
        return error_reason;
    }

    public void setError_reason(String error_reason) {
        this.error_reason = error_reason;
    }
}
