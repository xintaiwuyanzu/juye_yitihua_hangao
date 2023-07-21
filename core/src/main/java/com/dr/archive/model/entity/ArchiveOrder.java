package com.dr.archive.model.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "archiveorder", module = Constants.MODULE_NAME, comment = "档案排序")
public class ArchiveOrder extends BaseStatusEntity<String> {

    @Column(comment = "门类ID")
    private String cateGoryId;

    @Column(name = "pCode", comment = "库")
    private String code;

    @Column(comment = "排序字段")
    private String fieldOrder;

    @Column(comment = "排序类型")
    private String orderType;

    @Column(comment = "档案类型")
    private String archiveType;

    @Column(comment = "组织机构id", length = 50, order = 2)
    private String organiseId;

    @Column(comment = "组织机构名称", length = 50, order = 2)
    private String organiseName;

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCateGoryId() {
        return cateGoryId;
    }

    public void setCateGoryId(String cateGoryId) {
        this.cateGoryId = cateGoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(String fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }
}
