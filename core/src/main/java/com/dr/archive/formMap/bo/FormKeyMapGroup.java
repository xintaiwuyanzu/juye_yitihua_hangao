package com.dr.archive.formMap.bo;

/**
 * 表单字段映射分组接口
 *
 * @author dr
 */
public class FormKeyMapGroup implements Comparable<FormKeyMapGroup> {
    /**
     * 主键
     */
    private String groupId;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 分组名称
     */
    private String groupDescription;
    /**
     * 排序
     */
    private Integer order = 0;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public int compareTo(FormKeyMapGroup o) {
        return Integer.compare(order, o.order);
    }
}
