package com.dr.archive.examine.controller;

import com.dr.archive.examine.entity.JdzdCategory;
import com.dr.archive.examine.entity.JdzdCategoryInfo;
import com.dr.archive.examine.service.JdzdCategoryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 监督指导  档案类别管理
 *
 * @author cuiyj
 */
@RestController
@RequestMapping("api/jdzdCategory")
public class JdzdCategoryController extends BaseServiceController<JdzdCategoryService, JdzdCategory> {

    @Autowired
    JdzdCategoryService jdzdCategoryService;

    @Override
    protected SqlQuery<JdzdCategory> buildPageQuery(HttpServletRequest httpServletRequest, JdzdCategory jdzdCategory) {
        //根据机构id查询
        Organise organise = SecurityHolder.get().currentOrganise();
        return SqlQuery.from(JdzdCategory.class).equal(JdzdCategoryInfo.ORGANISEID, organise.getId()).like(JdzdCategoryInfo.LARGECATEGORY, jdzdCategory.getLargeCategory()).like(JdzdCategoryInfo.SMALLCATEGORY, jdzdCategory.getSmallCategory()).orderBy(JdzdCategoryInfo.LARGECATEGORY).orderBy(JdzdCategoryInfo.SMALLCATEGORY);
    }

    @Override
    protected SqlQuery<JdzdCategory> buildDeleteQuery(HttpServletRequest request, JdzdCategory entity) {
        Assert.isTrue(StringUtils.hasText(entity.getId()), "要删除的数据不能为空！");
        return SqlQuery.from(JdzdCategory.class).in(JdzdCategoryInfo.ID, entity.getId().split(","));
    }

    /**
     * 类别树
     *
     * @return
     */
    @RequestMapping("cateTree")
    public ResultEntity cateTree(@RequestParam(defaultValue = "") String pId) {
        List<TreeNode> treeNodes = jdzdCategoryService.cateTree(pId);
        return ResultEntity.success(treeNodes);
    }


}
