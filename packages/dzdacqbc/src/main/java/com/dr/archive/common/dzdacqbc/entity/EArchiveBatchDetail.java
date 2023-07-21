package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.archive.model.entity.AbstractArchiveRelateEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 出入库详情
 *
 * @author dr
 */
@Table(name = Constants.DZDNCQBC + "batch_detail", comment = "出库入库详情表", module = Constants.MODULE_NAME)
public class EArchiveBatchDetail extends AbstractArchiveRelateEntity {

    @Column(comment = "出入库批次Id", length = 200)
    private String batchId;

    @Column(comment = "出入库批次名称", length = 200)
    private String batchName;

    @Column(comment = "出库时间", length = 200)
    private String deliveryTime;

    @Column(comment = "出库人", length = 200)
    private String deliveryPerson;

    @Column(comment = "出库状态", length = 200)
    private String deliveryStatus;

    @Column(comment = "长期保存档案信息表id")
    private String archiveId;

    @Column(comment = "最后一次四性检测id")
    private String lastTestRecordId;

    @Column(comment = "档案类型", length = 50)
    private String modelType;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getLastTestRecordId() {
        return lastTestRecordId;
    }

    public void setLastTestRecordId(String lastTestRecordId) {
        this.lastTestRecordId = lastTestRecordId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
}
