package com.dr.archive.onlineGuide.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-06-10 14:58
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "BUSINESS_GUIDANCE_BATCH_DETAIL", comment = "指导批次详情", module = Constants.MODULE_NAME)
public class BusinessGuidanceBatchDetail extends AbstractBatchDetailEntity {
    @Column(comment = "描述", length = 500)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
