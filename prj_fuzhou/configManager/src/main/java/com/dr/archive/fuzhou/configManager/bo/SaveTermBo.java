package com.dr.archive.fuzhou.configManager.bo;

/**
 * @Author: caor
 * @Date: 2022-04-22 14:55
 * @Description:
 */
public class SaveTermBo {
    //保管期限
    String period;
    //门类编码
    String code;
    //优先级
    String priority;
    //机构编码
    String orgCode;
    //方案id
    String schemaId;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }
}
