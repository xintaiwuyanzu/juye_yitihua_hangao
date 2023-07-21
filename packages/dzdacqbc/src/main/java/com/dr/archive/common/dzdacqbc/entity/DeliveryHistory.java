package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "DELIVERY_HISTORY", comment = "档案出库流转历史", module = Constants.MODULE_NAME)
public class DeliveryHistory extends BaseCreateInfoEntity {

    @Column(comment = "出库批次id", length = 200)
    private String deliveryId;

    @Column(comment = "发送人", length = 200)
    private String sender;

    @Column(comment = "发送时间", length = 200)
    private String senderTime;

    @Column(comment = "意见", length = 200)
    private String operationOpinion;

    @Column(comment = "接收人", length = 200)
    private String receiver;

    @Column(comment = "接收时间", length = 200)
    private String receiverTime;

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderTime() {
        return senderTime;
    }

    public void setSenderTime(String senderTime) {
        this.senderTime = senderTime;
    }

    public String getOperationOpinion() {
        return operationOpinion;
    }

    public void setOperationOpinion(String operationOpinion) {
        this.operationOpinion = operationOpinion;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverTime() {
        return receiverTime;
    }

    public void setReceiverTime(String receiverTime) {
        this.receiverTime = receiverTime;
    }
}
