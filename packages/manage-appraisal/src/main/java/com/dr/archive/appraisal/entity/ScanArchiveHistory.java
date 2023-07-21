package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_scanArchiveHistory", module = Constants.MODULE_NAME, comment = "档案待鉴定库刷新历史")
public class ScanArchiveHistory extends BaseStatusEntity<String> {

    @Column(comment = "开始时间")
    private String startTime;

    @Column(comment = "结束时间")
    private String endTime;

    @Column(comment = "组织机构Id")
    private String orgId;

    @Column(comment = "刷新数据量")
    private String refreshQuantity;

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRefreshQuantity() {
        return refreshQuantity;
    }

    public void setRefreshQuantity(String refreshQuantity) {
        this.refreshQuantity = refreshQuantity;
    }
}
