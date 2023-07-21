package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "ReasonDictionary", module = Constants.MODULE_NAME, comment = "字典")
public class ReasonDictionary extends BaseEntity {


    @Column(comment = "字典数据",length = 100)
    private String reasonDictionary;

    public String getReasonDictionary() {
        return reasonDictionary;
    }

    public void setReasonDictionary(String reasonDictionary) {
        this.reasonDictionary = reasonDictionary;
    }
}
