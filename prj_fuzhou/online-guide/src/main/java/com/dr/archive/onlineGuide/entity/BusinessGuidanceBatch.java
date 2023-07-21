package com.dr.archive.onlineGuide.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-06-10 14:52
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "BUSINESS_GUIDANCE_BATCH", comment = "指导批次", module = Constants.MODULE_NAME)
public class BusinessGuidanceBatch extends AbstractArchiveBatch {
    //待指导
    public static final String status_wait = "0";
    //指导中
    public static final String status_ing = "1";
    //指导结束
    public static final String status_end = "2";
    @Column(comment = "描述", length = 500)
    private String description;
    @Column(comment = "档案车批次id")
    private String archiveCarId;
    @Column(comment = "接收单位id")
    private String receiveOrgId;
    @Column(comment = "接收单位名称")
    private String receiveOrgName;
    @Column(comment = "接收人id")
    private String receiveUserId;
    @Column(comment = "接收人姓名")
    private String receiveUserName;
    @Column(comment = "群组id")
    private String gid;
    @Column(comment = "群名")
    private String gidName;

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
    @Column(comment = "归类类型id")
    private String classifyId;
    @Column(comment = "归类类型")
    private String classifyType;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(String classifyType) {
        this.classifyType = classifyType;
    }

    public String getGidName() {
        return gidName;
    }

    public void setGidName(String gidName) {
        this.gidName = gidName;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArchiveCarId() {
        return archiveCarId;
    }

    public void setArchiveCarId(String archiveCarId) {
        this.archiveCarId = archiveCarId;
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
}
