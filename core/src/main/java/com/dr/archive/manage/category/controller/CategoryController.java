package com.dr.archive.manage.category.controller;

import com.dr.archive.enums.KindType;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.log.annotation.SysLog;
import com.dr.archive.util.Constants;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * describe
 *
 * @author tzl
 * @date 2020/5/15 9:20
 */
@RestController
@RequestMapping("/api/manage/category")
public class CategoryController extends BaseServiceController<CategoryService, Category> {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/inheritCategory")
    @SysLog("继承模板分类")
    public ResultEntity inheritCategory(String parentId, String categoryId, @Current Person person) {
        String message = service.inheritCategory(parentId, categoryId, person);
        return result(message);
    }

    /**
     * 获取档案的类型分类
     *
     * @return
     */
    @RequestMapping("/getCategoryType")
    public ResultListEntity<Map<String, String>> getCategoryType() {
        return ResultListEntity.success(KindType.getMapList());
    }

    private ResultEntity result(String message) {
        if (Constants.SUCCESS.equals(message)) {
            return ResultEntity.success();
        } else {
            return ResultEntity.error(message);
        }
    }

    @Override
    protected SqlQuery<Category> buildPageQuery(HttpServletRequest httpServletRequest, Category category) {
        SqlQuery<Category> sqlQuery = SqlQuery.from(Category.class).equal(CategoryInfo.ARCHIVETYPE,category.getArchiveType());
        return sqlQuery;
    }

    @Override
    @SysLog("删除分类")
    public ResultEntity<Boolean> delete(HttpServletRequest request, Category entity) {
        boolean deleteChildren = false;
        try {
            deleteChildren = Boolean.getBoolean(request.getParameter("deleteChildren"));
        } catch (Exception ignored) {
        }
        String message = service.deleteCategory(entity.getId(), deleteChildren);
        return result(message);
    }

    @RequestMapping("/pageByBusinessId")
    public ResultEntity pageByBusinessId(String businessId) {
        return ResultEntity.success(categoryService.pageBuBusinessId(businessId));
    }

    @RequestMapping("/selectByFondId")
    public ResultEntity selectByFondId(HttpServletRequest request, Category entity) {
        SqlQuery<Category> sqlQuery = SqlQuery.from(Category.class);
        if (!StringUtils.isEmpty(entity.getFondId())) {
            sqlQuery.equal(CategoryInfo.FONDID, entity.getFondId());
        }
        List<Category> categories = service.selectList(sqlQuery);
        return ResultEntity.success(categories);
    }

    /**
     * 获取去重后的分类门类
     *
     * @return
     */
    @RequestMapping("/getCategoryList")
    public ResultListEntity getCategoryList() {
        List<Map> map = categoryService.getCategoryList();
        return ResultListEntity.success(map);
    }


}
