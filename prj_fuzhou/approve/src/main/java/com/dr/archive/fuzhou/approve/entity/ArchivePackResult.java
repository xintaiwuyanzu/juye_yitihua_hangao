package com.dr.archive.fuzhou.approve.entity;

/**
 * @author caor
 * @date 2021-10-19 18:20
 * 归档回执接口传输对象
 */

public class ArchivePackResult {
    /* @param businessId 档案电子文件号
     * @param state      归档状态（1-归档成功，3-归档失败）
     * @param result     档案包解析结果（0-待推送，1-归档成功，2-归档失败，3-文件下载失败，4-文件解包失败，5-四性检测失败）
     * @param result_str 解析详细结果/
     */

    public ArchivePackResult(String businessId, String state, String result, String result_str) {
        this.businessId = businessId;
        this.state = state;
        this.result = result;
        this.result_str = result_str;
    }

    private String businessId;
    private String state;
    private String result;
    private String result_str;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult_str() {
        return result_str;
    }

    public void setResult_str(String result_str) {
        this.result_str = result_str;
    }
}
