package com.dr.archive.fuzhou.ofd.enums;

/**
 * @Title: WebExtensionResponseDefintation
 * @Description 响应参数值定义
 * @Author zhaoJing
 * @Date Create on 2020/11/18 16:07
 */
public enum WebExtensionResponseDefinitionEnum {
    SUCCESS(0,"成功"),
    DATA_NOT_EXISTS(1,"不存在该数据"),
    SERVER_ERROR(2,"系统错误"),
    NONE_ARGUMENTS(3,"请求参数为空")
    ;


    private Integer ret;
    private String desc;

    WebExtensionResponseDefinitionEnum() {
    }

    WebExtensionResponseDefinitionEnum(Integer ret, String desc) {
        this.ret = ret;
        this.desc = desc;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
