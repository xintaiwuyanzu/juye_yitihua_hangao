package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 介质管理
 */

@Table(name = Constants.DZDNCQBC + "medium", module = Constants.MODULE_NAME, comment = "电子档案长期保存介质管理表")
public class ESaveMedium extends BaseStatusEntity<String> {

    @Column(comment = "介质名称", length = 200)
    private String mediumName;

    //gp：光盘  yp：硬盘
    @Column(comment = "介质类型", length = 200)
    private String mediumType;
    @Column(comment = "备份说明")
    private String marks;

    @Column(comment = "机构id")
    private String orgId;

    public String getMediumName() {
        return mediumName;
    }

    public void setMediumName(String mediumName) {
        this.mediumName = mediumName;
    }

    public String getMediumType() {
        return mediumType;
    }

    public void setMediumType(String mediumType) {
        this.mediumType = mediumType;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
