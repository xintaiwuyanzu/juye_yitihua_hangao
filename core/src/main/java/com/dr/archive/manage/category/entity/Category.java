package com.dr.archive.manage.category.entity;

import com.dr.archive.enums.CategoryType;
import com.dr.archive.model.entity.BaseBusinessIdEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.core.security.bo.PermissionResource;

/**
 * describe
 * 分类门类
 *
 * @author tzl
 * @date 2020/5/15 9:06
 */
@Table(name = Constants.TABLE_PREFIX + "CATEGORY", module = Constants.MODULE_NAME, comment = "分类表")
public class Category extends BaseBusinessIdEntity implements PermissionResource {

    @Column(comment = "全宗Id", length = 500, order = 1)
    private String fondId;
    /**
     * 分类具体类型，文书，会计，科技等
     * 这个没多大用处，在执行具体业务逻辑的时候能用到
     */
    @Column(comment = "分类类型", length = 500, order = 2)
    private String categoryType;
    /**
     * 项目 案卷  文件
     *
     * @see CategoryType
     */
    @Column(comment = "档案类型", length = 500, order = 3)
    private String archiveType;

    @Column(name = "parent_id", comment = "父Id", length = 100)
    private String parentId;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
