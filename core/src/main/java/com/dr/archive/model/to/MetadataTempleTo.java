package com.dr.archive.model.to;

import com.dr.archive.enums.CategoryType;
import com.dr.archive.enums.KindType;

/**
 * 元数据模板基本信息
 *
 * @author zhangb
 */
public class MetadataTempleTo extends BaseTo {
    /**
     * 模板名称
     */
    private String templeName;
    /**
     * 门类类型
     */
    KindType categoryType;
    /**
     * 模板类型
     */
    CategoryType archiveCategoryType;

    public String getTempleName() {
        return templeName;
    }

    public void setTempleName(String templeName) {
        this.templeName = templeName;
    }


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
