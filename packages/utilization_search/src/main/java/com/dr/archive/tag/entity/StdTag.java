package com.dr.archive.tag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 国家标准标签
 *
 * @author dr
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "StdTag", module = Constants.MODULE_NAME, comment = "国家标准标签")
public class StdTag extends BaseCreateInfoEntity {
    @Column(comment = "一级标签Id")
    private String labelId1st;
    @Column(comment = "一级标签名称")
    private String labelName1st;
    @Column(comment = "二级标签Id")
    private String labelId2nd;
    @Column(comment = "二级标签名称")
    private String labelName2nd;
    @Column(comment = "标签级别")
    private int labelLevel;
    @Column(comment = "标签是否有子标签")
    private int childCount;
    @Column(name = "orderInfo1st", comment = "一级标签排序")
    private String orderInfo1st;
    @Column(name = "orderInfo2nd", comment = "二级标签排序")
    private String orderInfo2nd;
    @Column(type = ColumnType.CLOB, comment = "词库内容")
    private String content;

    public StdTag() {
    }

    public StdTag(String labelName1st, String labelName2nd, String orderInfo1st, String content) {
        this.labelName1st = labelName1st;
        this.labelName2nd = labelName2nd;
        this.orderInfo1st = orderInfo1st;
        this.content = content;
    }

    public String getOrderInfo1st() {
        return orderInfo1st;
    }

    public void setOrderInfo1st(String orderInfo1st) {
        this.orderInfo1st = orderInfo1st;
    }

    public String getOrderInfo2nd() {
        return orderInfo2nd;
    }

    public void setOrderInfo2nd(String orderInfo2nd) {
        this.orderInfo2nd = orderInfo2nd;
    }

    public String getLabelId1st() {
        return labelId1st;
    }

    public void setLabelId1st(String labelId1st) {
        this.labelId1st = labelId1st;
    }

    public String getLabelName1st() {
        return labelName1st;
    }

    public void setLabelName1st(String labelName1st) {
        this.labelName1st = labelName1st;
    }

    public String getLabelId2nd() {
        return labelId2nd;
    }

    public void setLabelId2nd(String labelId2nd) {
        this.labelId2nd = labelId2nd;
    }

    public String getLabelName2nd() {
        return labelName2nd;
    }

    public void setLabelName2nd(String labelName2nd) {
        this.labelName2nd = labelName2nd;
    }

    public int getLabelLevel() {
        return labelLevel;
    }

    public void setLabelLevel(int labelLevel) {
        this.labelLevel = labelLevel;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
