package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_org_record", module = Constants.MODULE_NAME, comment = "档案扫描表单记录")
public class AppraisalOrgRecord extends BaseStatusEntity<String> {

    @Column(comment = "表单Id")
    private String orgId;

    @Column(comment = "开始时间")
    private String startTime;

    @Column(comment = "结束时间")
    private String endTime;

    @Column(comment = "更新待鉴定数据量")
    private String updateCount;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(String updateCount) {
        this.updateCount = updateCount;
    }
}
