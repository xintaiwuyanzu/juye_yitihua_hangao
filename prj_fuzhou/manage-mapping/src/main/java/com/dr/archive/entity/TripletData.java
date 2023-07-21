package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-06-09 11:22
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_TripletData", module = Constants.MODULE_NAME, comment = "三元组数据")
public class TripletData extends BaseStatusEntity<String> {
    @Column(comment = "元数据表id")
    private String baseFormId;
    @Column(comment = "源对象表单id")
    private String sourceFormId;
    @Column(comment = "源对象表单名称")
    private String sourceFormName;
    @Column(comment = "源数据id")
    private String sourceId;
    @Column(comment = "源对象值")
    private String sourceName;
    @Column(comment = "目标对象表单id")
    private String targetFormId;
    @Column(comment = "目标对象表单名称")
    private String targetFormName;
    @Column(comment = "目标数据id")
    private String targetId;
    @Column(comment = "目标对象值")
    private String targetName;
    @Column(comment = "关系id")
    private String relationId;
    @Column(comment = "关系名称")
    private String relationName;
    @Column(comment = "源对象字段")
    private String sourceField;
    @Column(comment = "目标对象字段")
    private String targetField;

    public TripletData(String baseFormId, String sourceFormId, String sourceFormName, String sourceId, String sourceName, String targetFormId, String targetFormName, String targetId, String targetName, String relationId, String relationName, String sourceField, String targetField) {
        this.baseFormId = baseFormId;
        this.sourceFormId = sourceFormId;
        this.sourceFormName = sourceFormName;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.targetFormId = targetFormId;
        this.targetFormName = targetFormName;
        this.targetId = targetId;
        this.targetName = targetName;
        this.relationId = relationId;
        this.relationName = relationName;
        this.sourceField = sourceField;
        this.targetField = targetField;
    }

    public String getSourceFormName() {
        return sourceFormName;
    }

    public void setSourceFormName(String sourceFormName) {
        this.sourceFormName = sourceFormName;
    }

    public String getTargetFormName() {
        return targetFormName;
    }

    public void setTargetFormName(String targetFormName) {
        this.targetFormName = targetFormName;
    }

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getBaseFormId() {
        return baseFormId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getSourceFormId() {
        return sourceFormId;
    }

    public void setSourceFormId(String sourceFormId) {
        this.sourceFormId = sourceFormId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetFormId() {
        return targetFormId;
    }

    public void setTargetFormId(String targetFormId) {
        this.targetFormId = targetFormId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public TripletData() {
    }

    public void setBaseFormId(String baseFormId) {
        this.baseFormId = baseFormId;
    }
}
