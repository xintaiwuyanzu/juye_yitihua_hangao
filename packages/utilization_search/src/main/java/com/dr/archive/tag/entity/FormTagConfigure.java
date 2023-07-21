package com.dr.archive.tag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * @date: 2022/3/28 11:21
 * 元数据标签配置表
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "FORM_TAG_CONFIGURE", module = Constants.MODULE_NAME, comment = "元数据标签配置表")
public class FormTagConfigure extends BaseStatusEntity<String> {
    @Column(name = "formDefinitionId", comment = "表单定义Id", length = 100)
    private String formDefinitionId;
    @Column(name = "formDefinitionName", comment = "表单名称")
    private String formDefinitionName;
    @Column(name = "formDefinitionCode", comment = "表单编码")
    private String formDefinitionCode;
    @Column(name = "fieldCode", comment = "字段编码")
    private String fieldCode;
    @Column(name = "fieldLabel", comment = "字段中文名称")
    private String label;
    @Column(name = "stType", comment = "标签实体类型")
    private String stType;

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormDefinitionName() {
        return formDefinitionName;
    }

    public void setFormDefinitionName(String formDefinitionName) {
        this.formDefinitionName = formDefinitionName;
    }

    public String getFormDefinitionCode() {
        return formDefinitionCode;
    }

    public void setFormDefinitionCode(String formDefinitionCode) {
        this.formDefinitionCode = formDefinitionCode;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStType() {
        return stType;
    }

    public void setStType(String stType) {
        this.stType = stType;
    }
}
