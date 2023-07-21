package com.dr.archive.specialTag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * 专题标签
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "SPECIAL_TAG", module = Constants.MODULE_NAME, comment = "专题标签表")
public class SpecialTag extends BaseStatusEntity<String> {
    @Column(comment = "标签名")
    private String tagName;
    @Column(comment = "描述", type = ColumnType.CLOB)
    private String tagDescribe;
    @Column(comment = "父类id", name = "parent_id")
    private String parentId;
    @Column(name = "leaf")
    private int leaf;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDescribe() {
        return tagDescribe;
    }

    public void setTagDescribe(String tagDescribe) {
        this.tagDescribe = tagDescribe;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getLeaf() {
        return leaf;
    }

    public void setLeaf(int leaf) {
        this.leaf = leaf;
    }

}
