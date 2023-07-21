package com.dr.archive.detection.enums;

/**
 * 四性检测类型
 *
 * @author dr
 */
public enum DetectionType {
    /**
     * 其他四性检测类型
     */
    OTHER("OTHER", "其他", 0),
    /**
     * 真实性检测
     */
    REAL("authenticity", "真实性", 1),
    /**
     * 完整性检测
     */
    FULL("integrity", "完整性", 2),
    /**
     * 可用性检测
     */
    USABLE("usability", "可用性", 3),
    /**
     * 安全性检测
     */
    SAFE("security", "安全性", 4);
    /**
     * 编码
     */
    private String code;
    /**
     * 显示名称
     */
    private String label;
    /**
     * 排序
     */
    private Integer order;

    DetectionType(String code, String label, Integer order) {
        this.code = code;
        this.label = label;
        this.order = order;
    }

    public static DetectionType fromCode(String detectionType) {
        for (DetectionType value : DetectionType.values()) {
            if (value.code.equalsIgnoreCase(detectionType)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
