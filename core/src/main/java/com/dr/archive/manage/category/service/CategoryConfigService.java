package com.dr.archive.manage.category.service;

import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.model.query.CategoryFormQuery;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.BaseService;

import java.util.List;


/**
 * 描述：门类表单配置表
 *
 * @author tuzl
 * @date 2020/6/9 8:46
 */
public interface CategoryConfigService extends BaseService<CategoryConfig> {
    /**
     * 根据主键删除数据
     *
     * @param ids
     * @return
     */
    long delete(String ids);

    /**
     * 功能描述: 根据分类Id删除数据
     *
     * @param categoryId
     * @author : tzl
     * @date : 2020/6/9 10:48
     */
    void deleteByCategoryId(String categoryId);

    /**
     * 根据分类Id查询数据
     *
     * @param categoryId
     * @return
     */
    List<CategoryConfig> selectByCategoryId(String categoryId);

    /**
     * 根据条件查询门类表单基本关联信息
     *
     * @param categoryFormQuery
     * @return
     */
    CategoryConfig getCategoryForms(CategoryFormQuery categoryFormQuery);

    CategoryConfig selectOneByCategoryId(String categoryId);

    /**
     * 根据查询条件查询指定类型的表单定义
     *
     * @param categoryFormQuery
     * @return
     */
    List<FormModel> getCategoryForm(CategoryFormQuery categoryFormQuery);

}
