package com.dr.archive.utilization.compilation.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-02-20 17:20
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "MATERIAL", module = Constants.MODULE_NAME, comment = "素材表")
public class Material extends BaseStatusEntity<String> {
    @Column(comment = "素材名称")
    private String materialName;
    @Column(comment = "素材类型")
    private String materialType;
    @Column(comment = "素材来源")
    private String materialSource;
    @Column(comment = "素材内容", type = ColumnType.CLOB)
    private String materialContent;
    @Column(comment = "表单定义id", length = 50)
    private String formDefinitionId;
    @Column(comment = "formDataId", length = 50)
    private String formDataId;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialSource() {
        return materialSource;
    }

    public void setMaterialSource(String materialSource) {
        this.materialSource = materialSource;
    }

    public String getMaterialContent() {
        return materialContent;
    }

    public void setMaterialContent(String materialContent) {
        this.materialContent = materialContent;
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
}
