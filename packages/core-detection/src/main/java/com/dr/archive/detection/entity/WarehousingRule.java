package com.dr.archive.detection.entity;

import com.dr.archive.model.entity.BaseYearEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 入库规则检测项设置
 */

@Table(name = Constants.TABLE_PREFIX + "WAREHOUSING_RULE", module = Constants.MODULE_NAME, comment = "入库规则检测项设置表")
public class WarehousingRule extends BaseYearEntity {

    @Column(comment = "环节性质", length = 10)
    private String linkNature;

    @Column(comment = "检测对象", length = 20)
    private String testObject;

    @Column(comment = "规则性质", length = 10)
    private String ruleNature;

    @Column(comment = "编码", length = 20)
    private String testCode;

    @Column(comment = "检测目标类型", length = 10)
    private String testType;

    @Column(comment = "检测目的", length = 500)
    private String testPurpose;

    @Column(comment = "检测方法", length = 500)
    private String testMethod;

    @Column(comment = "检测项目", length = 500)
    private String project;

    @Column(comment = "排序", length = 5)
    private String sort;

    @Column(comment = "备注信息", length = 500)
    private String remark;

    public String getLinkNature() {
        return linkNature;
    }

    public void setLinkNature(String linkNature) {
        this.linkNature = linkNature;
    }

    public String getTestObject() {
        return testObject;
    }

    public void setTestObject(String testObject) {
        this.testObject = testObject;
    }

    public String getRuleNature() {
        return ruleNature;
    }

    public void setRuleNature(String ruleNature) {
        this.ruleNature = ruleNature;
    }



    public String getTestPurpose() {
        return testPurpose;
    }

    public void setTestPurpose(String testPurpose) {
        this.testPurpose = testPurpose;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
