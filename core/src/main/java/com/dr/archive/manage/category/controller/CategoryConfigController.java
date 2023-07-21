package com.dr.archive.manage.category.controller;

import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondInfo;
import com.dr.archive.manage.log.annotation.SysLog;
import com.dr.archive.model.query.CategoryFormQuery;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/9 14:15
 */
@RestController
@RequestMapping("/api/manage/categoryconfig")
public class CategoryConfigController extends BaseServiceController<CategoryConfigService, CategoryConfig> {


    @Autowired
    CommonMapper commonMapper;


    /**
     * 获取指定门类的表单配置信息
     *
     * @param formQuery
     * @return
     */
    @PostMapping("/forms")
    public ResultEntity<CategoryConfig> forms(CategoryFormQuery formQuery) {
        return ResultEntity.success(service.getCategoryForms(formQuery));
    }


    @Override
    @SysLog("添加分类表单配置")
    public ResultEntity<CategoryConfig> insert(HttpServletRequest request, CategoryConfig entity) {
        return super.insert(request, entity);
    }

    @Override
    @SysLog("修改分类表单配置")
    public ResultEntity<CategoryConfig> update(HttpServletRequest request, CategoryConfig entity) {
        return super.update(request, entity);
    }

    @Override
    @SysLog("删除分类表单配置")
    public ResultEntity<Boolean> delete(HttpServletRequest request, CategoryConfig entity) {
        return ResultEntity.success(service.delete(request.getParameter("ids")) > 0L);
    }

    @Override
    protected SqlQuery<CategoryConfig> buildPageQuery(HttpServletRequest httpServletRequest, CategoryConfig config) {
        String businessId = httpServletRequest.getParameter("businessId");
        return SqlQuery.from(CategoryConfig.class).equal(CategoryConfigInfo.BUSINESSID, businessId);
    }


    @RequestMapping("/getFondCode")
    public ResultEntity getFondCode(String fondId){

        return ResultEntity.success(commonMapper.selectOneByQuery(SqlQuery.from(Fond.class,false).column(FondInfo.CODE.alias("code")).equal(FondInfo.ID,fondId).setReturnClass(Map.class)));
    }


}
