package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 档案出入库批次记录表
 *
 * @author dr
 */
@Table(name = Constants.DZDNCQBC + "batch", comment = "档案出库批次信息表", module = Constants.MODULE_NAME)
public class EArchiveBatch extends AbstractArchiveBatch {
    @Column(comment = "出库时间", length = 200)
    private String deliveryTime;

    @Column(comment = "出库人", length = 200)
    private String deliveryPerson;

    @Column(comment = "出库状态", length = 200)
    private String deliveryStatus;

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
}
