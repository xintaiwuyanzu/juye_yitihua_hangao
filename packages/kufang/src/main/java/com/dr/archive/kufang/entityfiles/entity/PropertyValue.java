package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 属性值扩展表
 *
 * @author cuiyj
 * @Date 2021-09-02
 */
@Table(name = Constants.TABLE_PREFIX + "Property_Value", module = Constants.MODULE_NAME, comment = "属性值扩展表")
public class PropertyValue extends BaseEntity {

    @Column(name = "pValue", comment = "属性值", length = 100)
    private String pValue;
    @Column(name = "propertyId", comment = "属性", length = 50)
    private String propertyId;
    @Column(name = "entityId", comment = "实体档案id", length = 50)
    private String entityId;

    public String getpValue() {
        return pValue;
    }

    public void setpValue(String pValue) {
        this.pValue = pValue;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
