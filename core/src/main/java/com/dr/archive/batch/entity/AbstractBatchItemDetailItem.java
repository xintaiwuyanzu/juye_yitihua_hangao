package com.dr.archive.batch.entity;

import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;

/**
 * 档案详情从表父类
 *
 * @author dr
 */
public class AbstractBatchItemDetailItem extends BaseCreateInfoEntity {
    @Column(comment = "批次Id", length = 100)
    private String batchId;
    @Column(comment = "详情Id", length = 100)
    private String detailId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
