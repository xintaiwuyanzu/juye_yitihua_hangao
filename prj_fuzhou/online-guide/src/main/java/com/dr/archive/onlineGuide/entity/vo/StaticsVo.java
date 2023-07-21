package com.dr.archive.onlineGuide.entity.vo;

/**
 * @author wx
 */
public class StaticsVo {
    //单位名
    private String orgName;
    //类型
    private String type;
    //数量
    private long quantity;

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }



}
