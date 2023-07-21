package com.dr.archive.fuzhou.configManager.bo.itemconfig;

import java.util.List;

/**
 * 方案元数据四性检测规则
 *
 * @author: qiuyf
 */
public class MetadataRuleTest {
    private String id;
    /*
     * 检测元数据*/
    private String metadata;
    private String typeId;
    /*
     * 门类*/
    private String typeCode;
    private String creator;
    private String crTime;
    private String flag;
    private String schemaId;
    private String modifier;
    private String examineState;
    private String moTime;
    /*
     * 检测规则*/
    private List<TestRule> rules;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCrTime() {
        return crTime;
    }

    public void setCrTime(String crTime) {
        this.crTime = crTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getExamineState() {
        return examineState;
    }

    public void setExamineState(String examineState) {
        this.examineState = examineState;
    }

    public String getMoTime() {
        return moTime;
    }

    public void setMoTime(String moTime) {
        this.moTime = moTime;
    }

    public List<TestRule> getRules() {
        return rules;
    }

    public void setRules(List<TestRule> rules) {
        this.rules = rules;
    }
}
