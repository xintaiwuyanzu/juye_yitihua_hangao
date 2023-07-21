package com.dr.archive.model.to;

/**
 * 基础父类
 *
 * @author dr
 */
public class BaseOrderTo extends BaseTo implements Comparable<BaseOrderTo> {
    /**
     * 排序
     */
    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(BaseOrderTo o) {
        return order - o.order;
    }
}
