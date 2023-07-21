package com.dr.archive.statistics.enums;

import org.springframework.util.StringUtils;

/**
 * 移交报表默认统计数据
 */

public enum HandOverField {

    title("题名", "title"),
    archiveCode("档号", "archiveCode"),
    year("年度", "year"),
//    wjxcrq("文件形成日期", "wjxcrq"),
    saveTerm("保管期限", "saveTerm");

    private String label;
    private String key;

    public static HandOverField form(String keyOrLabel) {
        if (!StringUtils.isEmpty(keyOrLabel)) {
            for (HandOverField value : HandOverField.values()) {
                if (value.key.equals(keyOrLabel) || value.label.equals(keyOrLabel)) {
                    return value;
                }
            }
        }
        throw new IllegalArgumentException("未找到指定字段：" + keyOrLabel);

    }

    HandOverField(String label, String key) {
        this.label = label;
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
