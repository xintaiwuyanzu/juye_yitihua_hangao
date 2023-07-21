package com.dr.archive.appraisal.entity;


import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseOrderedEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_Matching", module = Constants.MODULE_NAME, comment = "档案鉴定匹配到的规则")
public class AppraisalMatching extends BaseOrderedEntity {

    @Column(comment = "鉴定结果参考")
    private String auxiliaryResult;

    @Column(comment = "匹配到的内容")
    private String content;

    @Column(comment = "匹配类型")
    private String matchingType;

    @Column(comment = "鉴定档案Id")
    private String appraisalId;

    @Column(comment = "表单Id")
    private String formDefinitionId;

    @Column(comment = "表单数据Id")
    private String formDataId;

    @Column(comment = "匹配字段")
    private String filed;

    @Column(comment = "出现次数")
    private String frequency;

    @Column(comment = "档案鉴定依据规则中的专题Id")
    private String appraisalSpecialId;

    @Column(comment = "规则ID")
    private String rulesId;

    @Column(comment = "依据ID")
    private String basisID;

    public String getAuxiliaryResult() {
        return auxiliaryResult;
    }

    public void setAuxiliaryResult(String auxiliaryResult) {
        this.auxiliaryResult = auxiliaryResult;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMatchingType() {
        return matchingType;
    }

    public void setMatchingType(String matchingType) {
        this.matchingType = matchingType;
    }

    public String getAppraisalId() {
        return appraisalId;
    }

    public void setAppraisalId(String appraisalId) {
        this.appraisalId = appraisalId;
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

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getAppraisalSpecialId() {
        return appraisalSpecialId;
    }

    public void setAppraisalSpecialId(String appraisalSpecialId) {
        this.appraisalSpecialId = appraisalSpecialId;
    }

    public String getRulesId() {
        return rulesId;
    }

    public void setRulesId(String rulesId) {
        this.rulesId = rulesId;
    }

    public String getBasisID() {
        return basisID;
    }

    public void setBasisID(String basisID) {
        this.basisID = basisID;
    }
}
