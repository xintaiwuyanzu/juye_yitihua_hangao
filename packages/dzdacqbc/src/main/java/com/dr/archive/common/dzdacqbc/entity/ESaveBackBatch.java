package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存 备份批次
 */

@Table(name = Constants.DZDNCQBC + "backBatch", module = Constants.MODULE_NAME, comment = "电子档案长期保存备份批次表")
public class ESaveBackBatch extends BaseStatusEntity<String> {

    @Column(comment = "备份批次编号", length = 200)
    private String batchCode;

    @Column(comment = "备份批次名称", length = 200)
    private String batchName;

    @Column(comment = "备份数量")
    private int backCount;

    @Column(comment = "备份策略id")
    private String strategyId;
    //jx:近线备份  lx:离线备份
    @Column(comment = "备份类型")
    private String backType;

    @Column(comment = "操作人")
    private String personId;

    //1：已过期  0：未过期
    @Column(comment = "是否过期")
    private String isExpire;

    @Column(comment = "机构id")
    private String orgId;


    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getBackCount() {
        return backCount;
    }

    public void setBackCount(int backCount) {
        this.backCount = backCount;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(String isExpire) {
        this.isExpire = isExpire;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
