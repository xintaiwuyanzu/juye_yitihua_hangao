package com.dr.archive.detection.model;

/**
 * 四性检测小项下级具体实现的相关描述信息
 * <br>
 * 这些信息正常可以放在{@link com.dr.archive.detection.service.ItemDetectionService}实现类中声明，
 * 但是这些配置信息太多了，没必要都放在代码中，会造成代码量十分庞大。
 * 现在的解决思路是配置信息直接放在json文件中
 * <br>
 * 具体配置加载类可以参考
 * {@link com.dr.archive.detection.service.TestStdModelLoader}
 *
 * @author dr
 */
public class TestItemInfo {

    /**
     * 四性检测上级小项描述信息
     */
    private TestSubType subType;

    /**
     * 四性检测实现小项的编码
     */
    private String itemCode;
    /**
     * 检测项目名称
     */
    private String title;
    /**
     * 检测目的
     */
    private String purpose;
    /**
     * 检测对项目描述
     */
    private String target;
    /**
     * 检测方法描述
     */
    private String method;
    /**
     * 自定义备注信息
     */
    private String comment;

    /*
     * 实现方式
     * */
    private String testRecordMethod;

    public TestSubType getSubType() {
        return subType;
    }

    public void setSubType(TestSubType subType) {
        this.subType = subType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTestRecordMethod() {
        return testRecordMethod;
    }

    public void setTestRecordMethod(String testRecordMethod) {
        this.testRecordMethod = testRecordMethod;
    }
}
