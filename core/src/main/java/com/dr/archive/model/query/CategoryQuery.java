package com.dr.archive.model.query;

/**
 * 门类分类查询对象
 *
 * @author dr
 */
public class CategoryQuery {
    /**
     * 是否查询树
     */
    private boolean tree;
    /**
     * 是否查询所有状态数据
     */
    private boolean all;
    /**
     * 根据父id查询一层分类树
     */
    private String parentId;
    /**
     * 根据全宗Id查询
     */
    private String fondId;
    /**
     * 根据全宗编码查询
     */
    private String fondCode;
    /**
     * 查询某人能看的分类
     */
    private String personId;

    public boolean isTree() {
        return tree;
    }

    public void setTree(boolean tree) {
        this.tree = tree;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
