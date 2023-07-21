package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_basis", module = Constants.MODULE_NAME, comment = "依据管理表")
public class AppraisalBasis extends BaseStatusEntity<String> {
    @Column(comment = "编码")
    private String code;

    @Column(comment = "依据")
    private String basis;

    /**
     * one:依据下匹配到一个规则就不匹配其他的；all：匹配依据下所有的规则
     */
    @Column(comment = "规则匹配类型")
    private String matchingType;

    @Column(comment = "描述")
    private String description;

    @Column(comment = "机构ID")
    private String orgId;

    private Object rulesList;

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getMatchingType() {
        return matchingType;
    }

    public void setMatchingType(String matchingType) {
        this.matchingType = matchingType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getRulesList() {
        return rulesList;
    }

    public void setRulesList(Object rulesList) {
        this.rulesList = rulesList;
    }
}
