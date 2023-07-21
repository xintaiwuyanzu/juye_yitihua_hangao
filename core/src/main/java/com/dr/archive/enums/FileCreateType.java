package com.dr.archive.enums;

/**
 * 文件创建类型
 *
 * @author dr
 */
public enum FileCreateType {
    /**
     * 上传
     */
    UPLOAD("上传"),
    /**
     * 离线导入
     */
    OFFLINERECEIVING("离线导入"),
    /**
     * 在线接收
     */
    ONLINERECEIVING("在线接收"),
    /**
     * 格式转换
     */
    CONVERSION("格式转换");
    private final String label;

    FileCreateType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
