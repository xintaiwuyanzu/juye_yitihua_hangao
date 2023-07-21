package com.dr.archive.utilization.consult.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 查档详情信息
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_CONSULT_DETAIL", comment = "查档申请详情表", module = Constants.MODULE_NAME)
public class ArchiveBatchDetailConsult extends AbstractBatchDetailEntity {
    @Column(comment = "能否查看原文")
    private boolean useFile;

    public boolean isUseFile() {
        return useFile;
    }

    public void setUseFile(boolean useFile) {
        this.useFile = useFile;
    }
}
