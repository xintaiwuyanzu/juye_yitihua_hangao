package com.dr.archive.batch.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 用来存储字段问题详情
 * <p>
 * 可以当作审批意见使用
 * <p>
 * 这个表是详情表的扩展表，也可以用作批次表的扩展表
 * <p>
 * 如果业务逻辑过于复杂，扩展字段过多，可以根据业务逻辑新创建自己的扩展表
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAIL_ITEM", comment = "问题详情", module = Constants.MODULE_NAME)
public class ArchiveBatchDetailItem extends AbstractBatchItemDetailItem {

    @Column(comment = "字段编码", length = 100)
    private String fieldCode;
    @Column(comment = "字段名称", length = 100)
    private String fieldName;
    @Column(comment = "字段值", length = 500)
    private String fieldValue;
    @Column(comment = "问题描述", length = 1000)
    private String problem;
    @Column(comment = "问题类型", length = 100)
    private String problemType;

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }
}
