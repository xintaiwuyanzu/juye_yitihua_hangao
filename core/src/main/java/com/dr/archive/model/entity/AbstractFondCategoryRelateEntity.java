package com.dr.archive.model.entity;

import com.dr.archive.manage.category.entity.Category;
import com.dr.framework.core.orm.annotations.Column;

import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_CATEGORY_CODE;

/**
 * 全宗门类关联实体类
 *
 * @author dr
 */
public class AbstractFondCategoryRelateEntity extends AbstractFondRelateEntity {
    /**
     * 门类编码
     */
    @Column(name = COLUMN_CATEGORY_CODE, comment = "分类号")
    private String categoryCode;
    @Column(comment = "分类Id", length = 100)
    private String categoryId;
    /**
     * 冗余字段
     */
    @Column(comment = "分类名称", length = 500)
    private String categoryName;

    /**
     * 绑定门类信息
     *
     * @param category
     */
    public void bindCategoryInfo(Category category) {
        if (category != null) {
            setCategoryCode(category.getCode());
            setCategoryName(category.getName());
            setCategoryId(category.getId());
        }
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
