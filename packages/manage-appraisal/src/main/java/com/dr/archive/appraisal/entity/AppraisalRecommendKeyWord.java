package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_recommend_keyword", module = Constants.MODULE_NAME, comment = "档案鉴定匹配过滤词")
public class AppraisalRecommendKeyWord extends BaseStatusEntity<String> {


    @Column(comment = "关键词")
    private String keyWord;
    @Column(comment = "辅助鉴定结果")
    private String auxiliaryResult;
    @Column(comment = "辅助鉴定结果")
    private String openRange;
    @Column(comment = "备注")
    private String remarks;
    @Column(comment = "表单数据Id")
    private String formDataId;
    @Column(comment = "表单Id")
    private String formDefinitionId;
    @Column(comment = "鉴定任务ID")
    private String ruleId;
    @Column(comment = "鉴定任务ID")
    private String batchId;
    @Column(comment = "创建人姓名")
    private String createPersonName;
    @Column(comment = "数据来源")
    private String sourceType;


    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getOpenRange() {
        return openRange;
    }

    public void setOpenRange(String openRange) {
        this.openRange = openRange;
    }

    public String getAuxiliaryResult() {
        return auxiliaryResult;
    }

    public void setAuxiliaryResult(String auxiliaryResult) {
        this.auxiliaryResult = auxiliaryResult;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
