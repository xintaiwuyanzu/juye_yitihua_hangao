package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_taskrange", module = Constants.MODULE_NAME, comment = "档案鉴定批次排除范围")
public class AppraisalRangeExclude extends BaseCreateInfoEntity {

    @Column(comment = "排除鉴定范围全宗")
    private String fondId;

    @Column(comment = "排除鉴定范围门类")
    private String categoryId;

    @Column(comment = "排除鉴定范围文本")
    private String rangeTxt;

    @Column(comment = "鉴定")
    private String orgId;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRangeTxt() {
        return rangeTxt;
    }

    public void setRangeTxt(String rangeTxt) {
        this.rangeTxt = rangeTxt;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
