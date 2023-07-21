package com.dr.archive.fuzhou.configManager.bo;

/**
 * 门类年度
 *
 * @author dr
 */
public class CategoryYear extends AbstractConfigEntity {
    /**
     * 门类档案ID
     */
    private String typeId;
    /**
     * 分类
     */
    private String type;
    /**
     * 起始时间(年)
     */
    private String startTime;
    /**
     * 结束时间(年)
     */
    private String endTime;
    /**
     * 规范标准
     */
    private String standard;
    /**
     * 系统生成的编码
     */
    private String sysCode;
    /**
     * remark
     */
    private String remark;

    /**
     * 机构编码
     */
    private String orgCode;

    /*
    1是电子档案 2是纸质化副本
    * */
    private String classify;

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
