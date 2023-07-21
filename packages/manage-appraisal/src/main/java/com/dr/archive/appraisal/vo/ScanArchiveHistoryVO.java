package com.dr.archive.appraisal.vo;

public class ScanArchiveHistoryVO {

    private long startTime;

    private long endTime;

    private String orgId;

    private String refreshQuantity;

    private String id;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRefreshQuantity() {
        return refreshQuantity;
    }

    public void setRefreshQuantity(String refreshQuantity) {
        this.refreshQuantity = refreshQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
