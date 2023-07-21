package com.dr.archive.common.dzdacqbc.vo;

/**
 * 用来展示长期保存系统监控的数据Vo类
 *
 * @author dr
 */
public class MonitorDataVo {
    /**
     * 描述
     */
    private String label;
    /**
     * 数据值
     */
    private double value;
    /**
     * 最大值
     */
    private double max;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
