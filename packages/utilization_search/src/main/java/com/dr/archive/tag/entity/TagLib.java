package com.dr.archive.tag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * @date: 2022/3/14 18:27
 * 标签库 纯标签数据 与档案无关
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "TAG_LIB", module = Constants.MODULE_NAME, comment = "标签表")
public class TagLib extends BaseStatusEntity<String> {
    @Column(comment = "标签名")
    private String tagName;
    //ctype gt 固体标签,st 实体标签,zdy 自定义标签，guojia 国家标签，jichushishi基础事实标签
    @Column(comment = "类型(gt 固体标签,st 实体标签,zdy 自定义标签)")
    private String ctype;
    @Column(comment = "实体分类(当类型为实体时 人,机构名,地名,时间)")
    private String stType;
    @Column(comment = "描述", type = ColumnType.CLOB)
    private String tagDescribe;
    @Column(comment = "父类id", name = "parent_id")
    private String parentId;
    @Column(comment = "全标签", name = "fullLable")
    private String fullLable;
    @Column(name = "leaf")
    private int leaf;
    @Column(comment = "添加人")
    private String createPersonName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getStType() {
        return stType;
    }

    public void setStType(String stType) {
        this.stType = stType;
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

    public String getFullLable() {
        return fullLable;
    }

    public void setFullLable(String fullLable) {
        this.fullLable = fullLable;
    }

    public int getLeaf() {
        return leaf;
    }

    public void setLeaf(int leaf) {
        this.leaf = leaf;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }
}
