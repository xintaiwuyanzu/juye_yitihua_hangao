package com.dr.archive.managearchivechange.entity;

import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-03-25 11:52
 * @Description: 状态：0:待纠错 1：已纠错 2:纠错中 3:未通过
 * 类型：1：自动发起 2：推送
 */
@Table(name = Constants.TABLE_PREFIX + "ERROR_CORRECTION", module = Constants.MODULE_NAME, comment = "档案待纠错库")
public class ErrorCorrection extends AbstractArchiveEntity {
    @Column(comment = "表单ID", length = 50)
    private String formDefinitionId;
    @Column(comment = "数据ID", length = 50)
    private String formDataId;
    @Column(comment = "全宗id")
    private String fondId;
    @Column(comment = "门类id")
    private String categoriesId;
    @Column(comment = "来源")
    private String errorSource;
    @Column(comment = "类型", length = 10)
    private String errorType;
    @Column(comment = "机构id", length = 50)
    private String orgId;
    @Column(comment = "添加人姓名")
    private String createPersonName;
    @Column(comment = "发起流程人id", length = 50)
    private String startPersonId;
    @Column(comment = "发起流程人姓名")
    private String startPersonName;
    @Column(comment = "错误描述")
    private String errorDescription;

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

    public String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getStartPersonId() {
        return startPersonId;
    }

    public void setStartPersonId(String startPersonId) {
        this.startPersonId = startPersonId;
    }

    public String getStartPersonName() {
        return startPersonName;
    }

    public void setStartPersonName(String startPersonName) {
        this.startPersonName = startPersonName;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

}
