package com.dr.archive.tag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * @date: 2022/3/17 10:09
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "TAG_TYPE", module = Constants.MODULE_NAME, comment = "标签类别表")
public class TagType extends BaseStatusEntity<String> {
    @Column(comment = "类别名称")
    private String TypeName;
    @Column(comment = "类别编码")
    private String TypeCode;
    @Column(comment = "父类id", name = "parent_id")
    private String parentId;
    @Column(comment = "是否父节点", name = "leaf")
    private int leaf;
    @Column(comment = "父节点名称集合", name ="pname")
    private String pname;

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getTypeCode() {
        return TypeCode;
    }

    public void setTypeCode(String typeCode) {
        TypeCode = typeCode;
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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
