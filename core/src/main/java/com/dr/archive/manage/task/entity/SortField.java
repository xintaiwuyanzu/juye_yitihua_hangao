package com.dr.archive.manage.task.entity;

/**
 * 排序信息
 *
 * @author dr
 */
public class SortField {
    private String key;
    private String order;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
