package com.dr.archive.fuzhou.ofd.enums;

/**
 * @Title: WebOFDTimeModelEnum
 * @Description 云阅读阅读时间模式定义
 * @Author zhaoJing
 * @Date Create on 2020/8/5 16:57
 */
public enum WebOFDTimeModelEnum {
    ABSOLUTE_TIME("0","绝对时间"),
    RELATIVE_TIME("1","相对时间"),
    UNLIMITED_TIME("2","不限时间");


    private String timeModel;
    private String desc;

    WebOFDTimeModelEnum() {
    }

    WebOFDTimeModelEnum(String timeModel, String desc) {
        this.timeModel = timeModel;
        this.desc = desc;
    }

    public String getTimeModel() {
        return timeModel;
    }

    public void setTimeModel(String timeModel) {
        this.timeModel = timeModel;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
