package com.dr.archive.onlineGuide.entity;

import com.dr.archive.model.entity.BaseBusinessIdEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-06-10 14:58
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "BUSINESS_GUIDANCE_RECORD", comment = "指导交流记录", module = Constants.MODULE_NAME)
public class BusinessGuidanceRecord extends BaseBusinessIdEntity {
    //添加
    public static final String type_add = "add";
    //回复
    public static final String type_reply = "reply";
    @Column(comment = "添加人姓名")
    private String createUserName;
    @Column(comment = "添加人单位id")
    private String createOrgId;
    @Column(comment = "添加人单位名称")
    private String createOrgName;
    @Column(comment = "发送人Id")
    private String sendUserId;
    @Column(comment = "发送人姓名")
    private String sendUserName;
    @Column(comment = "发送人单位id")
    private String sendOrgId;
    @Column(comment = "发送人单位名称")
    private String sendOrgName;

    @Column(comment = "接收人id")
    private String receiveUserId;
    @Column(comment = "接收人姓名")
    private String receiveUserName;
    @Column(comment = "接收人单位id")
    private String receiveOrgId;
    @Column(comment = "接收人单位名称")
    private String receiveOrgName;
    @Column(comment = "消息内容", length = 500)
    private String message;

    //业务id为formDataId
    @Column(comment = "批次id")
    private String batchId;
    @Column(comment = "详情id")
    private String detailId;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getCreateOrgName() {
        return createOrgName;
    }

    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendOrgId() {
        return sendOrgId;
    }

    public void setSendOrgId(String sendOrgId) {
        this.sendOrgId = sendOrgId;
    }

    public String getSendOrgName() {
        return sendOrgName;
    }

    public void setSendOrgName(String sendOrgName) {
        this.sendOrgName = sendOrgName;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getReceiveOrgId() {
        return receiveOrgId;
    }

    public void setReceiveOrgId(String receiveOrgId) {
        this.receiveOrgId = receiveOrgId;
    }

    public String getReceiveOrgName() {
        return receiveOrgName;
    }

    public void setReceiveOrgName(String receiveOrgName) {
        this.receiveOrgName = receiveOrgName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
