package com.dr.archive.model.query;

import com.dr.archive.enums.CategoryType;
import com.dr.archive.enums.KindType;

/**
 * 查询元数据模块
 *
 * @author zhangb
 */
public class MetadataTempleQuery {
    /**
     * 门类类型
     */
    KindType categoryType;
    /**
     * 模板类型
     */
    CategoryType archiveCategoryType;

    public KindType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(KindType categoryType) {
        this.categoryType = categoryType;
    }

    public CategoryType getArchiveCategoryType() {
        return archiveCategoryType;
    }

    public void setArchiveCategoryType(CategoryType archiveCategoryType) {
        this.archiveCategoryType = archiveCategoryType;
    }
}
