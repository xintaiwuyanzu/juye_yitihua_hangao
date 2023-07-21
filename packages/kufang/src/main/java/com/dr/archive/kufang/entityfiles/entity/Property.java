package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 档案属性
 */
@Table(name = Constants.TABLE_PREFIX + "Property", module = Constants.MODULE_NAME, comment = "属性")
public class Property extends BaseStatusEntity<String> {


    @Column(comment = "档案类别", length = 200)
    private String archiveTypeId;
    @Column(comment = "属性", length = 100)
    private String propertyName;


    public String getArchiveTypeId() {
        return archiveTypeId;
    }

    public void setArchiveTypeId(String archiveTypeId) {
        this.archiveTypeId = archiveTypeId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
