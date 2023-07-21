package com.dr.archive.receive.vo;

import com.dr.archive.receive.online.entity.AbstractArchiveReceiveBatch;

/**
 * @author caor
 * @date 2021-10-22 11:48
 * 接收报告
 */
public class BatchReportVo extends AbstractArchiveReceiveBatch {
    //接收成功的元数据数量
    private long metadataSucessNum;
    //接收失败的元数据数量
    private long metadataFalseNum;
    //挂接成功数量
    private long hookSucessNum;
    //挂接失败数量
    private long hookFalseNum;
    //四性检测成功数量
    private long fourSexTestSucessNum;
    //四性检测失败数量
    private long fourSexTestFalseNum;

    public long getMetadataSucessNum() {
        return metadataSucessNum;
    }

    public void setMetadataSucessNum(long metadataSucessNum) {
        this.metadataSucessNum = metadataSucessNum;
    }

    public long getMetadataFalseNum() {
        return metadataFalseNum;
    }

    public void setMetadataFalseNum(long metadataFalseNum) {
        this.metadataFalseNum = metadataFalseNum;
    }

    public long getHookSucessNum() {
        return hookSucessNum;
    }

    public void setHookSucessNum(long hookSucessNum) {
        this.hookSucessNum = hookSucessNum;
    }

    public long getHookFalseNum() {
        return hookFalseNum;
    }

    public void setHookFalseNum(long hookFalseNum) {
        this.hookFalseNum = hookFalseNum;
    }

    public long getFourSexTestSucessNum() {
        return fourSexTestSucessNum;
    }

    public void setFourSexTestSucessNum(long fourSexTestSucessNum) {
        this.fourSexTestSucessNum = fourSexTestSucessNum;
    }

    public long getFourSexTestFalseNum() {
        return fourSexTestFalseNum;
    }

    public void setFourSexTestFalseNum(long fourSexTestFalseNum) {
        this.fourSexTestFalseNum = fourSexTestFalseNum;
    }
}
