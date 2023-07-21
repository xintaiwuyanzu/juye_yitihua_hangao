package com.dr.archive.fuzhou.configManager.bo;

/**
 * 四性检测配置项
 *
 * @author dr
 * TODO 这里面的参数基本没办法用
 */
public class TestItem extends AbstractConfigEntity {
    //检测数据包
    public static final String TEST_TARGET_TYPE_FILE = "1";
    //检测元数据
    public static final String TEST_TARGET_TYPE_META = "2";

    public enum TestType {
        //真实性检测
        Authenticity,
        //完整性检测
        Integrity,
        //安全性检测
        Security,
        //可用性检测
        Usability
    }


    /**
     * 检测项编码
     */
    private String code;
    /**
     * 检测方法描述
     */
    private String method;
    /**a
     * 检测对象描述
     */
    private String substance;

    /**
     * 检测目标类型
     * （1：数据包，2：元数据）
     */
    private String targetType;
    /**
     * 排序
     */
    private int sort;
    /**
     * 四性检测大类
     *
     * @see TestType
     */
    private TestType type;
    /**
     * 检测目的
     */
    private String target;
    /**
     * 检测项目
     */
    private String items;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
