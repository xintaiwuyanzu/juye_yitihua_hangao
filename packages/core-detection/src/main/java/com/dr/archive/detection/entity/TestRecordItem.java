package com.dr.archive.detection.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.common.entity.OrderEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import org.springframework.util.comparator.ComparableComparator;

/**
 * 四性检测小项
 *
 * @author dr
 */
public class TestRecordItem extends BaseStatusEntity<String> {

    /**
     * 下面两个字段是关联字段，共关联查询和删除时使用
     */
    @Column(comment = "检测数据Id", length = 100)
    private String formDataId;
    @Column(comment = "检测记录Id", length = 100)
    private String recordId;
    @Column(comment = "业务关联Id", length = 100)
    private String businessId;
    //与智能归档编号匹配
    @Column(comment = "检测类型编码", length = 100)
    private String itemCode;

    @Column(comment = "检测结果描述", type = ColumnType.CLOB)
    private String testResult;
    @Column(comment = "检测环节", length = 100)
    private String linkCode;
    //用于取 实现方式描述等数据
    @Column(comment = "实现方式编码", length = 100)
    private String modeCode;


    @Column(comment = "检测对象编码", length = 200)
    private String targetCode;
    @Column(comment = "检测对象名称", length = 200)
    private String targetName;
    @Column(comment = "检测对象值", length = 500)
    private String targetValue;


    /**
     * ==========================
     * 下面的字段都是service自动拼凑出来的，只是为了辅助前端显示使用
     * ==========================
     **/
    private String testRecordCode;
    //实现方式
    private String testRecordMethod;
    //标准要求
    private String testRecordSubstance;
    //检测内容 真实性/完整性/可用性/安全性
    private String testRecordTargetType;
    private String testRecordTarget;
    //检测小项
    private String testRecordItems;


    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }


    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public String getModeCode() {
        return modeCode;
    }

    public void setModeCode(String modeCode) {
        this.modeCode = modeCode;
    }

    /**
     * ==========================
     * 下面的字段都是service自动拼凑出来的，只是为了辅助前端显示使用
     * ==========================
     **/
    public String getTestRecordCode() {
        return testRecordCode;
    }

    public void setTestRecordCode(String testRecordCode) {
        this.testRecordCode = testRecordCode;
    }

    public String getTestRecordMethod() {
        return testRecordMethod;
    }

    public void setTestRecordMethod(String testRecordMethod) {
        this.testRecordMethod = testRecordMethod;
    }

    public String getTestRecordSubstance() {
        return testRecordSubstance;
    }

    public void setTestRecordSubstance(String testRecordSubstance) {
        this.testRecordSubstance = testRecordSubstance;
    }

    public String getTestRecordTargetType() {
        return testRecordTargetType;
    }

    public void setTestRecordTargetType(String testRecordTargetType) {
        this.testRecordTargetType = testRecordTargetType;
    }

    public String getTestRecordTarget() {
        return testRecordTarget;
    }

    public void setTestRecordTarget(String testRecordTarget) {
        this.testRecordTarget = testRecordTarget;
    }

    public String getTestRecordItems() {
        return testRecordItems;
    }

    public void setTestRecordItems(String testRecordItems) {
        this.testRecordItems = testRecordItems;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public int compareTo(OrderEntity o) {
        if (o instanceof TestRecordItem) {
            TestRecordItem other = (TestRecordItem) o;
            int result = ComparableComparator.INSTANCE.compare(getModeCode(), other.getModeCode());
            /*if (result == 0) {
                return super.compareTo(o);
            }*/
            return result;
        } else {
            return 0;
        }
    }
}
