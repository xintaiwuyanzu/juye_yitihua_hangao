package com.dr.archive.manage.handover.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "HAND_OVER_DETAIL", comment = "档案到期移交详情", module = Constants.MODULE_NAME)
public class ArchiveBatchHandOverDetail extends AbstractBatchDetailEntity {
    /**
     * 详情类型
     * 1为档案
     * 2为全宗卷
     */
    @Column(comment = "详情类型")
    private String detailType;

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }
}
