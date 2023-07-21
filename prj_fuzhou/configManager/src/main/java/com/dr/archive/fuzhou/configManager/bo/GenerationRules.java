package com.dr.archive.fuzhou.configManager.bo;

import java.util.List;

/**
 * 档号生成规则
 */
public class GenerationRules extends AbstractConfigEntity {

    private List<GenerationRule> rules;

    /**
     * 门类编码
     */
    private String typeCode;
    /**
     * 门类Id
     */
    private String typeId;

    /**
     * 规则英文示例
     */
    private String metadataRule;
    /**
     * 数字规则
     */
    private String digitRule;
    /**
     * 规则中文示例描述
     */
    private String rule;

    /**
     * 生效开始时间
     */
    private String startTime;
    /**
     * 生效结束时间
     */
    private String endTime;
    /**
     * 归档系统设置的规则字符串
     */
    private String ruleList;

    public List<GenerationRule> getRules() {
        return rules;
    }

    public void setRules(List<GenerationRule> rules) {
        this.rules = rules;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getMetadataRule() {
        return metadataRule;
    }

    public void setMetadataRule(String metadataRule) {
        this.metadataRule = metadataRule;
    }

    public String getDigitRule() {
        return digitRule;
    }

    public void setDigitRule(String digitRule) {
        this.digitRule = digitRule;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

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

    public String getRuleList() {
        return ruleList;
    }

    public void setRuleList(String ruleList) {
        this.ruleList = ruleList;
    }
}