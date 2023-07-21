package com.dr.archive.appraisal.entity;


import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseOrderedEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_MatchingKeyWord", module = Constants.MODULE_NAME, comment = "档案鉴定匹配到的关键词")
public class MatchingKeyWord extends BaseOrderedEntity {

    @Column(comment = "匹配到的词库ID")
    private String matchIngWordGroupId;

    @Column(comment = "词库鉴定结果参考")
    private String wordGroupResult;

    @Column(comment = "匹配到的关键词")
    private String keywords;

    @Column(comment = "鉴定档案Id")
    private String appraisalId;

    @Column(comment = "优先级")
    private String priority;

    @Column(comment = "表单Id")
    private String formDefinitionId;

    @Column(comment = "表单数据Id")
    private String formDataId;

    @Column(comment = "匹配字段")
    private String filed;

    @Column(comment = "出现次数")
    private String frequency;

    @Column(comment = "档案鉴定过滤词Id")
    private String appraisalKeyWordId;

    @Column(comment = "档案鉴定过滤词")
    private String appraisalKeyWord;

    @Column(comment = "依据ID")
    private String basisId;

    @Column(comment = "规则ID")
    private String rulesId;


    public String getMatchIngWordGroupId() {
        return matchIngWordGroupId;
    }

    public void setMatchIngWordGroupId(String matchIngWordGroupId) {
        this.matchIngWordGroupId = matchIngWordGroupId;
    }

    public String getWordGroupResult() {
        return wordGroupResult;
    }

    public void setWordGroupResult(String wordGroupResult) {
        this.wordGroupResult = wordGroupResult;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAppraisalId() {
        return appraisalId;
    }

    public void setAppraisalId(String appraisalId) {
        this.appraisalId = appraisalId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    public String getAppraisalKeyWordId() {
        return appraisalKeyWordId;
    }

    public void setAppraisalKeyWordId(String appraisalKeyWordId) {
        this.appraisalKeyWordId = appraisalKeyWordId;
    }

    public String getAppraisalKeyWord() {
        return appraisalKeyWord;
    }

    public void setAppraisalKeyWord(String appraisalKeyWord) {
        this.appraisalKeyWord = appraisalKeyWord;
    }

    public String getBasisId() {
        return basisId;
    }

    public void setBasisId(String basisId) {
        this.basisId = basisId;
    }

    public String getRulesId() {
        return rulesId;
    }

    public void setRulesId(String rulesId) {
        this.rulesId = rulesId;
    }
}
