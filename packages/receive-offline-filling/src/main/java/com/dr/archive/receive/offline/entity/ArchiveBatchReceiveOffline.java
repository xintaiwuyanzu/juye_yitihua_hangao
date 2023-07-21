package com.dr.archive.receive.offline.entity;

import com.dr.archive.receive.online.entity.AbstractArchiveReceiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 离线接收批次
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_RECEIVE_OFFLINE", comment = "离线接收批次", module = Constants.MODULE_NAME)
public class ArchiveBatchReceiveOffline extends AbstractArchiveReceiveBatch {
    /**
     * 批量挂接
     */
    // 0：未挂接 1：已挂接
    @Column(comment = "挂接状态")
    private String hookStatus;
    @Column(comment = "挂接开始时间")
    private long hookStartTime;
    @Column(comment = "挂接结束时间")
    private long hookEndTime;
    @Column(comment = "挂接结束时间")
    private String hookNum;
    @Column(comment = "挂接成功数量")
    private long hookSuccessNum;
    @Column(comment = "挂接失败数量")
    private long hookFalseNum;
    @Column(comment = "四性检测状态", length = 10)
    private String testStatus;
    @Column(comment = "导入方式:1 目录+原文; 2 包结构")
    private String impType;

    @Column(comment = "原文挂接失败数")
    private int fileHookFalseNum;
    @Column(comment = "生成分类")
    private String typologic;

    public String getTypologic() {
        return typologic;
    }

    public void setTypologic(String typologic) {
        this.typologic = typologic;
    }

    public String getHookStatus() {
        return hookStatus;
    }

    public void setHookStatus(String hookStatus) {
        this.hookStatus = hookStatus;
    }

    public long getHookStartTime() {
        return hookStartTime;
    }

    public void setHookStartTime(long hookStartTime) {
        this.hookStartTime = hookStartTime;
    }

    public long getHookEndTime() {
        return hookEndTime;
    }

    public void setHookEndTime(long hookEndTime) {
        this.hookEndTime = hookEndTime;
    }

    public String getHookNum() {
        return hookNum;
    }

    public void setHookNum(String hookNum) {
        this.hookNum = hookNum;
    }

    public long getHookSuccessNum() {
        return hookSuccessNum;
    }

    public void setHookSuccessNum(long hookSuccessNum) {
        this.hookSuccessNum = hookSuccessNum;
    }

    public long getHookFalseNum() {
        return hookFalseNum;
    }

    public void setHookFalseNum(long hookFalseNum) {
        this.hookFalseNum = hookFalseNum;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getImpType() {
        return impType;
    }

    public void setImpType(String impType) {
        this.impType = impType;
    }

    public int getFileHookFalseNum() {
        return fileHookFalseNum;
    }

    public void setFileHookFalseNum(int fileHookFalseNum) {
        this.fileHookFalseNum = fileHookFalseNum;
    }
}
