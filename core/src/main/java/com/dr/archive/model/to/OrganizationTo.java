package com.dr.archive.model.to;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/30 9:16
 */
public class OrganizationTo {
    private String organzationName;
    private String organzationCode;
    private long proNumber;
    private long arcNumber;
    private long fileNumber;

    public String getOrganzationName() {
        return organzationName;
    }

    public void setOrganzationName(String organzationName) {
        this.organzationName = organzationName;
    }

    public String getOrganzationCode() {
        return organzationCode;
    }

    public void setOrganzationCode(String organzationCode) {
        this.organzationCode = organzationCode;
    }

    public long getProNumber() {
        return proNumber;
    }

    public void setProNumber(long proNumber) {
        this.proNumber = proNumber;
    }

    public long getArcNumber() {
        return arcNumber;
    }

    public void setArcNumber(long arcNumber) {
        this.arcNumber = arcNumber;
    }

    public long getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(long fileNumber) {
        this.fileNumber = fileNumber;
    }
}
