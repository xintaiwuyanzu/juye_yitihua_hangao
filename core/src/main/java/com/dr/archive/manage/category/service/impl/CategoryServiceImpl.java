package com.dr.archive.manage.category.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.model.query.CategoryFormQuery;
import com.dr.archive.service.impl.BasePermissionResourceService;
import com.dr.archive.service.internal.CodingSchemeService;
import com.dr.archive.util.Constants;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.bo.PermissionResource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * describe
 * 档案门类分类
 * TODO 里面逻辑有点多，有点晕
 *
 * @author dr
 * @author caor
 * @author tzl
 * @date 2020/5/15 9:22
 */
@Service
public class CategoryServiceImpl extends BasePermissionResourceService<Category> implements CategoryService {
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    CategoryConfigService categoryConfigService;
    @Autowired
    CodingSchemeService codingSchemeService;
    @Autowired
    FondService fondService;


    @Transactional(rollbackFor = Exception.class)
    protected void deleteChildren(String aId) {
        Assert.isTrue(!StringUtils.isEmpty(aId), "分类id不能为空！");
        delete(SqlQuery.from(Category.class).equal(CategoryInfo.ID, aId));
        deleteScheme(aId);
        List<Category> categoryList = commonMapper.selectByQuery(
                SqlQuery.from(Category.class, false)
                        .column(CategoryInfo.ID)
                        .equal(CategoryInfo.PARENTID, aId)
        );
        if (categoryList.size() > 0) {
            for (Category category1 : categoryList) {
                deleteChildren(category1.getId());
            }
        }
    }

    /**
     * 功能描述: 删除分类下的检测方案，存储方案，编码方案，表单方案
     *
     * @param aId
     * @return :
     * @author : tzl
     * @date : 2020/8/12 11:37
     */
    private void deleteScheme(String aId) {
        //TODO
        commonMapper.deleteByQuery(SqlQuery.from(CategoryConfig.class).equal(CategoryConfigInfo.BUSINESSID, aId));
        codingSchemeService.deleteByBusinessId(aId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteCategory(String aId, Boolean deleteChildren) {
        Assert.isTrue(!StringUtils.isEmpty(aId), "分类id不能为空！");
        if (deleteChildren) {
            deleteChildren(aId);
        } else {
            if (commonMapper.countByQuery(SqlQuery.from(Category.class).equal(CategoryInfo.PARENTID, aId)) > 0) {
                return "删除该分类下子分类后才可以删除该分类";
            }
            deleteScheme(aId);
            commonMapper.deleteByQuery(SqlQuery.from(Category.class).equal(CategoryInfo.ID, aId));
        }
        return Constants.SUCCESS;
    }

    /**
     * ===========================================
     * 上面是给controller使用的
     * 下面是给其他模块使用的
     * ===========================================
     */

    @Override
    public Category findCategoryByCode(String categoryCode, String fondId) {
        return selectOne(SqlQuery.from(Category.class)
                .equal(CategoryInfo.CODE, categoryCode)
                .equal(CategoryInfo.FONDID, fondId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String inheritCategory(String parentId, String categoryId, Person person) {
        if (StringUtils.isEmpty(parentId)) {
            return "参数错误";
        }
        if (StringUtils.isEmpty(categoryId)) {
            return "参数错误";
        }
        Category category = selectById(parentId);
        String[] ids = categoryId.split(",");
        Set<String> idSet = new HashSet<>();
        for (String id : ids) {
            inherit(selectById(id), category, person, idSet);
        }
        return Constants.SUCCESS;
    }

    @Override
    public List<Category> pageBuBusinessId(String businessId) {
        return commonMapper.selectByQuery(SqlQuery.from(Category.class).equal(CategoryInfo.BUSINESSID, businessId));
    }

    /**
     * 功能描述: 继承并根据模板数据复制添加数据，继承至父类树下
     *
     * @param category
     * @param parentCategory
     * @param person
     * @param idSet          可能选择了父分类同时也选择了子分类，通过set去重
     * @return :
     * @author : tzl
     * @date : 2020/8/12 13:22
     */
    private void inherit(Category category, Category parentCategory, Person person, Set<String> idSet) {
        String categoryId = category.getId();
        if (idSet.contains(categoryId)) {
            return;
        }
        idSet.add(categoryId);

        Category newCategory = new Category();
        //门类被缓存管着，所以这里初始化一个新的对象
        BeanUtils.copyProperties(category, newCategory);
        //子类重新赋值判断code是否重复，不重复保存
        String uuid = UUIDUtils.getUUID();
        newCategory.setId(uuid);
        newCategory.setCreateDate(System.currentTimeMillis());
        newCategory.setUpdateDate(System.currentTimeMillis());
        newCategory.setCreatePerson(person.getId());
        newCategory.setUpdatePerson(person.getId());
        newCategory.setParentId(parentCategory.getId());
        newCategory.setBusinessId(parentCategory.getFondId());
        newCategory.setFondId(parentCategory.getFondId());
        insert(newCategory);

        List<CategoryConfig> categoryConfigs = categoryConfigService.selectList(SqlQuery.from(CategoryConfig.class)
                .equal(CategoryConfigInfo.BUSINESSID, categoryId));
        for (CategoryConfig categoryConfig : categoryConfigs) {
            categoryConfig.setId(UUIDUtils.getUUID());
            categoryConfig.setBusinessId(uuid);
            categoryConfig.setCode(uuid);
            categoryConfig.setCreatePerson(person.getId());
            categoryConfig.setCreateDate(System.currentTimeMillis());
            categoryConfigService.insert(categoryConfig);
        }
        List<Category> categoryList = selectList(SqlQuery.from(Category.class).equal(CategoryInfo.PARENTID, categoryId));
        if (categoryList != null && !categoryList.isEmpty()) {
            for (Category category1 : categoryList) {
                inherit(category1, category, person, idSet);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findCateByParentCode(String fondId, String categoryCode) {
        Category category = selectOne(SqlQuery.from(Category.class)
                .equal(CategoryInfo.CODE, categoryCode)
                .equal(CategoryInfo.FONDID, fondId));
        return selectList(SqlQuery.from(Category.class).equal(CategoryInfo.PARENTID, category.getParentId()));
    }

    @Override
    public List<FormModel> getCategoryForm(CategoryFormQuery categoryFormQuery) {
        return categoryConfigService.getCategoryForm(categoryFormQuery);
    }

    @Override
    public List<Map> getCategoryList() {
        SqlQuery<Category> group = SqlQuery.from(Category.class, false)
                .column(CategoryInfo.CODE)
                .column(CategoryInfo.NAME)
                .groupBy(CategoryInfo.NAME, CategoryInfo.CODE);
        return commonMapper.selectByQuery(group.setReturnClass(Map.class));
    }

    @Override
    public List<Category> getAllChildrenCategoryByParentId(String parentId) {
        List<Category> categoryList = new ArrayList<>();
        getAllChildrenCategoryByParentId(parentId, categoryList);
        return categoryList;
    }

    private void getAllChildrenCategoryByParentId(String parentId, List<Category> categoryList) {
        SqlQuery<Category> sqlQuery = SqlQuery.from(Category.class).equal(CategoryInfo.PARENTID, parentId);
        List<Category> tempList = selectList(sqlQuery);
        if (!tempList.isEmpty()) {
            categoryList.addAll(tempList);
            for (Category category : tempList) {
                getAllChildrenCategoryByParentId(category.getId(), categoryList);
            }
        }
    }

    @Override
    public String[] getCategoryCodeByParentCode(String fondID, String code) {
        Category category = findCategoryByCode(fondID, code);
        return categoryListToCodeArray(getAllChildrenCategoryByParentId(category.getId()));
    }

    @Override
    public String[] getCategoryCodeByFondId(String fondID) {
        return categoryListToCodeArray(getCategoryByFondId(fondID));
    }

    @Override
    public List<Category> getCategoryByFondId(String fondId) {
        SqlQuery sqlQuery = SqlQuery.from(Category.class)
                .equal(CategoryInfo.FONDID, fondId);
        return commonMapper.selectByQuery(sqlQuery);
    }

    @Override
    protected String getCacheName() {
        return "archive.category";
    }

    @Override
    public List<? extends PermissionResource> getGroupResource() {
        return fondService.selectList(SqlQuery.from(Fond.class));
    }

    @Override
    public List<? extends PermissionResource> getResources(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Collections.emptyList();
        }
        return selectList(SqlQuery.from(Category.class).equal(CategoryInfo.FONDID, groupId));
    }

    @Override
    public String getType() {
        return "category";
    }

    @Override
    public String getName() {
        return "门类分类";
    }

    public String[] categoryListToCodeArray(List<Category> categoryList) {
        String[] codes = new String[categoryList.size()];
        int i = 0;
        for (Category one : categoryList) {
            codes[i] = one.getCode();
        }
        return codes;
    }

}
