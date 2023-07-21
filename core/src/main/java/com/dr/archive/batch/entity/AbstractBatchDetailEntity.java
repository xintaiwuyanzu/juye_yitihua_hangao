package com.dr.archive.batch.entity;

import com.dr.archive.model.entity.AbstractArchiveRelateEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.annotations.Column;

import static com.dr.framework.common.entity.StatusEntity.STATUS_COLUMN_KEY;

/**
 * 批次详情
 *
 * @author dr
 */
public class AbstractBatchDetailEntity extends AbstractArchiveRelateEntity {
    /**
     * 批次Id列名称
     */
    public static final String BATCH_ID_COLUMN_NAME = "batchId";
    /**
     * 状态
     */
    @Column(name = STATUS_COLUMN_KEY, comment = "状态", length = 10)
    private String status;
    @Column(name = BATCH_ID_COLUMN_NAME, comment = "批次Id", length = 100)
    private String batchId;

    @Override
    public void bindFormData(FormData formData) {
        super.bindFormData(formData);
        //初始化表单状态  todo
        //setStatus(StatusEntity.STATUS_DISABLE_STR);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }


}
