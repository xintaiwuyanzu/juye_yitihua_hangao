package com.dr.archive.managefondsdescriptivefile.vo;

/**
 * @Author: caor
 * @Date: 2022-02-21 14:01
 * @Description: 展示全宗卷统计使用的
 */
public class FondsDescriptiveFileCountVo {
    private String name;
    /**
     * 数量
     */
    private long count;
    /**
     * 进馆时间
     */
    private long receiveDate;
    /**
     * 移交批次号
     */
    private String batchId;
    /**
     * 移交批次号
     */
    private String batchName;
    /**
     * 年度范围
     */
    private String annualScope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(long receiveDate) {
        this.receiveDate = receiveDate;
    }

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

    public String getAnnualScope() {
        return annualScope;
    }

    public void setAnnualScope(String annualScope) {
        this.annualScope = annualScope;
    }
}
