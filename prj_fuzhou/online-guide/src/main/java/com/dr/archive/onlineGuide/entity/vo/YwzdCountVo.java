package com.dr.archive.onlineGuide.entity.vo;

/**
 * @author Mr.Zhu
 * @date 2022/5/6 - 16:25
 */
public class YwzdCountVo {
    //单位名
    private String orgName;
    //待指导
    private long dzd;
    //已指导
    private long yzd;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public long getDzd() {
        return dzd;
    }

    public void setDzd(long dzd) {
        this.dzd = dzd;
    }

    public long getYzd() {
        return yzd;
    }

    public void setYzd(long yzd) {
        this.yzd = yzd;
    }
}
