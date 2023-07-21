package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_destoryHistory", module = Constants.MODULE_NAME, comment = "档案待鉴定库刷新历史")
public class DestoryHistory extends BaseCreateInfoEntity {

    @Column(comment = "批次Id")
    private String batchId;

    @Column(comment = "刷新数据量")
    private String destoryQuantity;

    @Column(comment = "操作人员姓名")
    private String createPersonName;

    @Column(comment = "组织机构")
    private String orgId;


    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getDestoryQuantity() {
        return destoryQuantity;
    }

    public void setDestoryQuantity(String destoryQuantity) {
        this.destoryQuantity = destoryQuantity;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
