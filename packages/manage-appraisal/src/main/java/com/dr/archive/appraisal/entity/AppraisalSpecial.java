package com.dr.archive.appraisal.entity;


import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_special", module = Constants.MODULE_NAME, comment = "档案鉴定依据规则中的专题")
public class AppraisalSpecial extends BaseCreateInfoEntity {

    @Column(comment = "专题名称")
    private String specialName;

    @Column(comment = "专题描述")
    private String specialRemarks;

    @Column(comment = "规则ID")
    private String rulesId;

    @Column(comment = "依据ID")
    private String basisID;

    @Column(comment = "组织机构Id")
    private String orgId;
    @Column(comment = "专题标签名")
    private String specialTagNames;
    @Column(comment = "专题标签id")
    private String specialTagIds;

    private Object specialDetailList;

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public String getSpecialRemarks() {
        return specialRemarks;
    }

    public void setSpecialRemarks(String specialRemarks) {
        this.specialRemarks = specialRemarks;
    }

    public String getBasisID() {
        return basisID;
    }

    public void setBasisID(String basisID) {
        this.basisID = basisID;
    }

    public String getRulesId() {
        return rulesId;
    }

    public void setRulesId(String rulesId) {
        this.rulesId = rulesId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSpecialTagNames() {
        return specialTagNames;
    }

    public void setSpecialTagNames(String specialTagNames) {
        this.specialTagNames = specialTagNames;
    }

    public String getSpecialTagIds() {
        return specialTagIds;
    }

    public void setSpecialTagIds(String specialTagIds) {
        this.specialTagIds = specialTagIds;
    }

    public Object getSpecialDetailList() {
        return specialDetailList;
    }

    public void setSpecialDetailList(Object specialDetailList) {
        this.specialDetailList = specialDetailList;
    }
}
