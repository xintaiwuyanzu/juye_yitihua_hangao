package com.dr.archive.tag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 基础事实标签
 *
 * @author dr
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "FactTag", module = Constants.MODULE_NAME, comment = "基础事实标签")
public class FactTag extends StdTag {
    @Column(comment = "三级标签Id")
    private String labelId3rd;
    @Column(comment = "三级标签名称")
    private String labelName3rd;

    public String getLabelId3rd() {
        return labelId3rd;
    }

    public void setLabelId3rd(String labelId3rd) {
        this.labelId3rd = labelId3rd;
    }

    public String getLabelName3rd() {
        return labelName3rd;
    }

    public void setLabelName3rd(String labelName3rd) {
        this.labelName3rd = labelName3rd;
    }

    public FactTag() {
        super();
    }

    public FactTag(String labelName1st, String labelName2nd, String labelName3rd, String orderInfo1st, String content, String orderInfo2nd) {
        this.setLabelName1st(labelName1st);
        this.setLabelName2nd(labelName2nd);
        this.labelName3rd = labelName3rd;
        this.setOrderInfo1st(orderInfo1st);
        this.setOrderInfo2nd(orderInfo2nd);
        this.setContent(content);
    }
}
