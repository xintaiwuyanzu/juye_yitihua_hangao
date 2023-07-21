package com.dr.archive.detection.enums;

/**
 * 四性检测环节
 *
 * @author dr
 */
public enum LinkType {
    //归档环节
    ARCHIVING("gd"),
    //移交环节
    TRANSFORM("yj"),
    //长期保存环节
    FILLING("bc"),
    //人工手动检测
    CUSTOM("rg"),
    //其他环节
    OTHER("qt"),
    //不知道啥环节，搞个默认环节
    DEFAULT("mr");

    /**
     * 静态方法，根据code创建枚举对象
     *
     * @param code
     * @return
     */
    public static LinkType fromCode(String code) {
        for (LinkType value : LinkType.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 环节编码
     */
    private String code;

    LinkType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
