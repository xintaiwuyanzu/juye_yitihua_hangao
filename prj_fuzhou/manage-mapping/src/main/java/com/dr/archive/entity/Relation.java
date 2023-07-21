package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-05-27 16:35
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_Relation", module = Constants.MODULE_NAME, comment = "对象关系")
public class Relation extends BaseStatusEntity<String> {
    @Column(comment = "源表表单定义id")
    private String sourceFormId;
    @Column(comment = "源表")
    private String sourceName;
    @Column(comment = "源表属性")
    private String sourceProperty;
    @Column(comment = "源表属性值")
    private String sourcePropertyValue;
    @Column(comment = "源表属性名字")
    private String sourcePropertyName;

    @Column(comment = "目标表表单定义id")
    private String targetFormId;
    @Column(comment = "目标表")
    private String targetName;
    @Column(comment = "目标表属性")
    private String targetProperty;
    @Column(comment = "目标表属性值")
    private String targetPropertyValue;
    @Column(comment = "目标表属性名字")
    private String targetPropertyName;

    @Column(comment = "是否存在逆向关系")
    private int haveReverse;
    @Column(comment = "逆向关系名字")
    private String reverseName;
    @Column(comment = "关系名称")
    private String relationName;
    @Column(comment = "关系备注")
    private String relationMark;
    @Column(comment = "关系复杂度")
    private int relationType;
    @Column(comment = "关系数据", type = ColumnType.CLOB)
    private String jsonText;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceProperty() {
        return sourceProperty;
    }

    public void setSourceProperty(String sourceProperty) {
        this.sourceProperty = sourceProperty;
    }

    public String getSourcePropertyValue() {
        return sourcePropertyValue;
    }

    public void setSourcePropertyValue(String sourcePropertyValue) {
        this.sourcePropertyValue = sourcePropertyValue;
    }

    public String getSourcePropertyName() {
        return sourcePropertyName;
    }

    public void setSourcePropertyName(String sourcePropertyName) {
        this.sourcePropertyName = sourcePropertyName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetProperty() {
        return targetProperty;
    }

    public void setTargetProperty(String targetProperty) {
        this.targetProperty = targetProperty;
    }

    public String getTargetPropertyValue() {
        return targetPropertyValue;
    }

    public void setTargetPropertyValue(String targetPropertyValue) {
        this.targetPropertyValue = targetPropertyValue;
    }

    public String getTargetPropertyName() {
        return targetPropertyName;
    }

    public void setTargetPropertyName(String targetPropertyName) {
        this.targetPropertyName = targetPropertyName;
    }

    public int getHaveReverse() {
        return haveReverse;
    }

    public void setHaveReverse(int haveReverse) {
        this.haveReverse = haveReverse;
    }

    public String getReverseName() {
        return reverseName;
    }

    public void setReverseName(String reverseName) {
        this.reverseName = reverseName;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRelationMark() {
        return relationMark;
    }

    public void setRelationMark(String relationMark) {
        this.relationMark = relationMark;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public String getJsonText() {
        return jsonText;
    }

    public void setJsonText(String jsonText) {
        this.jsonText = jsonText;
    }

    public String getTargetFormId() {
        return targetFormId;
    }

    public void setTargetFormId(String targetFormId) {
        this.targetFormId = targetFormId;
    }

    public String getSourceFormId() {
        return sourceFormId;
    }

    public void setSourceFormId(String sourceFormId) {
        this.sourceFormId = sourceFormId;
    }
}
