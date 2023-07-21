package com.dr.archive.statistics.enums;

import org.springframework.util.StringUtils;

/**
 * 报表默认统计数据
 */

public enum ReportField {

    arcArchiveNum("案卷目录数量", "arcArchiveNum"),
    fileArchiveNum("文件目录数量", "fileArchiveNum"),
    arcFileNum("案卷原文数量", "arcFileNum"),
    fileFileNum("文件原文数量", "fileFileNum"),
    arcFileSize("案卷原文大小", "arcFileSize"),
    fileFileSize("文件原文大小", "fileFileSize");

    private String label;
    private String key;

    public static ReportField form(String keyOrLabel) {
        if (!StringUtils.isEmpty(keyOrLabel)) {
            for (ReportField value : ReportField.values()) {
                if (value.key.equals(keyOrLabel) || value.label.equals(keyOrLabel)) {
                    return value;
                }
            }
        }
        throw new IllegalArgumentException("未找到指定字段：" + keyOrLabel);

    }

    ReportField(String label, String key) {
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
