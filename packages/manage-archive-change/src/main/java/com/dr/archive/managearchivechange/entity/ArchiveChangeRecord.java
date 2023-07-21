package com.dr.archive.managearchivechange.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "change_record", module = Constants.MODULE_NAME, comment = "档案变更记录表")
public class ArchiveChangeRecord extends BaseStatusEntity<String> {
    @Column(comment = "纠错id", length = 50)
    private String errorCorrectionId;
    @Column(comment = "form表单id", length = 50)
    private String formDefinitionId;
    @Column(comment = "档案数据id", length = 50)
    private String formDataId;
    @Column(comment = "全宗id")
    private String fondId;
    @Column(comment = "门类id")
    private String categoriesId;
    @Column(comment = "需要修改的属性代码")
    private String fieldsCode;
    @Column(comment = "需要修改的属性目标值")
    private String newValues;
    @Column(comment = "备注(变更说明)", length = 1000)
    private String remarks;
    @Column(comment = "档号", length = 100)
    private String archiveCode;
    @Column(comment = "题名", length = 100)
    private String title;
    @Column(comment = "需要修改的属性原始值", type = ColumnType.CLOB)
    private String oldValues;
    @Column(comment = "需要修改的属性名称", type = ColumnType.CLOB)
    private String fieldNames;

    public String getErrorCorrectionId() {
        return errorCorrectionId;
    }

    public void setErrorCorrectionId(String errorCorrectionId) {
        this.errorCorrectionId = errorCorrectionId;
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

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(String categoriesId) {
        this.categoriesId = categoriesId;
    }

    public String getFieldsCode() {
        return fieldsCode;
    }

    public void setFieldsCode(String fieldsCode) {
        this.fieldsCode = fieldsCode;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOldValues() {
        return oldValues;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }

    public String getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
