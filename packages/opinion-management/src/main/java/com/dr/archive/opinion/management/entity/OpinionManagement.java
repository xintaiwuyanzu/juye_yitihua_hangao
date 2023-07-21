package com.dr.archive.opinion.management.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * wx
 */
@Table(name = Constants.TABLE_PREFIX + "OPINION_MANAGEMENT", comment = "意见管理", module = Constants.MODULE_NAME)
public class OpinionManagement extends BaseStatusEntity<String> {

    @Column(comment = "业务外键",length = 100)
    private String businessId;

    @Column(comment = "意见内容",length = 200)
    private String opinion;

    @Column(comment = "默认数据",length = 50)
    private String defEnable; //0是默认,1是个人的


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getDefEnable() {
        return defEnable;
    }

    public void setDefEnable(String defEnable) {
        this.defEnable = defEnable;
    }
}
