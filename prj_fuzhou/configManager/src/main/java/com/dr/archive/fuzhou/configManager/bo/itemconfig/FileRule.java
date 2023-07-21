package com.dr.archive.fuzhou.configManager.bo.itemconfig;

import com.dr.archive.fuzhou.configManager.bo.AbstractConfigEntity;

/**
 * 数据包检测规则
 * <p>
 * 系统自动捕获电子文件内容数据的电子属性（除了文件大小，还包括文件名、 文件类型、创建时间等），与电子属性信息中记录的数据进行比对
 * <p>
 * 只要有这个配置就是用默认规则检测文件包
 *
 * @author dr
 */
public class FileRule extends AbstractConfigEntity {
    private String code;
    /**
     * 检测描述
     */
    private String items;
    /**
     * 环节（1:归档环节，2:移交与接收环节，3:长期保存环节）
     */
    private String linkCode;
    /**
     * 检测方法描述
     */
    private String method;
    private int sort;
    private String substance;
    private String target;
    private String targetType;
    private String configId;
    private String testItemId;

    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getTestItemId() {
        return testItemId;
    }

    public void setTestItemId(String testItemId) {
        this.testItemId = testItemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
