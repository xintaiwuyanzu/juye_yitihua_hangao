package com.dr.archive.tag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "ARCHIVETAG", module = Constants.MODULE_NAME, comment = "标签表")
public class Tag extends BaseStatusEntity<String> {

    @Column(comment = "全宗ID")
    private String fondId;

    @Column(comment = "全宗code")
    private String fondCode;

    @Column(comment = "分类ID")
    private String cateGoryId;

    @Column(comment = "分类code")
    private String cateGoryCode;

    @Column(comment = "表单ID")
    private String formDefinitionId;

    @Column(comment = "档案数据ID")
    private String formDataId;

    @Column(comment = "标签", length = 500)
    private String tag;

    @Column(comment = "类型", name = "pType")
    private String type;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getCateGoryId() {
        return cateGoryId;
    }

    public void setCateGoryId(String cateGoryId) {
        this.cateGoryId = cateGoryId;
    }

    public String getCateGoryCode() {
        return cateGoryCode;
    }

    public void setCateGoryCode(String cateGoryCode) {
        this.cateGoryCode = cateGoryCode;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
