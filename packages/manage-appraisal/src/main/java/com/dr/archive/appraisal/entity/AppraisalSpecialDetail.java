package com.dr.archive.appraisal.entity;


import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_special_detail", module = Constants.MODULE_NAME, comment = "档案鉴定依据规则中的专题详情")
public class AppraisalSpecialDetail extends BaseCreateInfoEntity {

    @Column(comment = "专题Id")
    private String specialId;

    @Column(comment = "字段")
    private String field;

    /**
     * like：
     * equal：
     * startWith：
     * endWith：
     * between：
     */
    @Column(comment = "关系")
    private String relation;

    @Column(comment = "关系值1")
    private String value1;

    @Column(comment = "关系值2")
    private String value2;

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
}
