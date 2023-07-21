package com.dr.archive.detection.model;

/**
 * 四性检测标准
 *
 * @author dr
 */
public class TestStd {
    //检测类别
    /**
     * "SAFE", "安全性"
     * "REAL", "真实性"
     * "FULL", "完整性"
     * "USABLE", "可用性"
     */
    private String category;
    /**
     * 编号
     */
    private String code;
    /**
     * 检测项
     */
    private String itemName;
    /**
     * 检测目的
     */
    private String itemGoal;
    /**
     * 检测对象
     */
    private String itemObject;
    /**
     * 环节名称
     */
    private String linkName;
    /**
     * 检测配置id
     */
    private String deployId;
    /**
     * 描述
     */
    private String itemDescribe;
    /**
     * 排序
     */
    private Integer itemOrder;


}
