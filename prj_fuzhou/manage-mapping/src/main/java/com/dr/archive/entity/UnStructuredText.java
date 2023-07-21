package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author Mr.Zhu
 * @date 2022/6/9 - 9:46
 */
@Table(name = Constants.TABLE_PREFIX + "Mapping_UnStructuredText", module = Constants.MODULE_NAME, comment = "非结构化原文")
public class UnStructuredText extends BaseStatusEntity<String> {
    @Column(comment = "档号", length = 100)
    private String archiveCode;
    @Column(comment = "全宗号", length = 100)
    private String fondCode;
    @Column(comment = "全宗Id")
    private String fondId;
    @Column(comment = "全宗名称", length = 100)
    private String fondName;
    @Column(comment = "档案门类代码", length = 100)
    private String categoryCode;
    @Column(comment = "门类Id")
    private String categoryId;
    @Column(comment = "门类名称", length = 100)
    private String categoryName;
    @Column(comment = "机构id", length = 50)
    private String orgId;
    //补充福建省要求的管理数据类别及要求
    @Column(comment = "立档单位名称", length = 100)
    private String LDDWMC;
    @Column(comment = "年度", length = 100)
    private String ND;
    @Column(comment = "三元组数", length = 100)
    private String RelationNum;
    @Column(comment = "题名")
    private String TIMING;
    @Column(comment = "表单id", length = 100)
    private String formDefinitionId;
    @Column(comment = "数据id", length = 100)
    private String formDataId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLDDWMC() {
        return LDDWMC;
    }

    public void setLDDWMC(String LDDWMC) {
        this.LDDWMC = LDDWMC;
    }

    public String getND() {
        return ND;
    }

    public void setND(String ND) {
        this.ND = ND;
    }

    public String getRelationNum() {
        return RelationNum;
    }

    public void setRelationNum(String relationNum) {
        RelationNum = relationNum;
    }

    public String getTIMING() {
        return TIMING;
    }

    public void setTIMING(String TIMING) {
        this.TIMING = TIMING;
    }
}
