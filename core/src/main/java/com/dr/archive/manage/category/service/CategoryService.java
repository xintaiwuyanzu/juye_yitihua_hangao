package com.dr.archive.manage.category.service;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.model.query.CategoryFormQuery;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

import java.util.List;
import java.util.Map;

/**
 * describe
 *
 * @author tzl
 * @date 2020/5/15 9:21
 */
public interface CategoryService extends BaseService<Category> {
    /**
     * 功能描述: 删除分类,删除分类配置根据配置id
     *
     * @param aId
     * @param deleteChildren 是否删除子分类
     * @return : {@link String}
     * @author : tzl
     * @date : 2020/6/4 15:19
     */
    String deleteCategory(String aId, Boolean deleteChildren);

    /**
     * 功能描述: 继承分类
     *
     * @param parentId   继承后父类id
     * @param categoryId 继承后子分类id
     * @param person     操作人员
     * @return : {@link String}
     * @author : tzl
     * @date : 2020/5/15 19:07
     */
    String inheritCategory(String parentId, String categoryId, Person person);

    List<Category> pageBuBusinessId(String businessId);

    /**
     * 根据全宗Id和分类编码查询所有的children
     *
     * @param fondId
     * @param categoryCode
     * @return
     */
    List<Category> findCateByParentCode(String fondId, String categoryCode);

    /**
     * 功能描述: 根据code获取分类信息
     *
     * @param categoryCode 分类编码
     * @param fondId       全宗id
     * @author : tzl
     * @date : 2020/6/18 13:41
     */
    Category findCategoryByCode(String categoryCode, String fondId);

    /**
     * 根据查询条件查询指定类型的表单定义
     *
     * @param categoryFormQuery
     * @return
     */
    List<FormModel> getCategoryForm(CategoryFormQuery categoryFormQuery);

    List<Map> getCategoryList();

    List<Category> getAllChildrenCategoryByParentId(String parent);

    String[] getCategoryCodeByParentCode(String fondID, String code);

    String[] getCategoryCodeByFondId(String fondID);

    List<Category> getCategoryByFondId(String fondId);
}
