package com.dr.archive.manage.fond.controller;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondInfo;
import com.dr.archive.manage.fond.service.FondChangeHistoryService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author
 */
@RestController
@RequestMapping("/api/manage/fond")
public class FondController extends BaseServiceController<FondService, Fond> {
    @Autowired
    FondChangeHistoryService fondChangeHistoryService;
    @Autowired
    ResourceManager resourceManager;
    @Autowired
    CategoryService categoryService;
    /**
     * 实现父类分页查询方法
     *
     * @param request
     * @param fond
     * @return
     */
    @Override
    protected SqlQuery<Fond> buildPageQuery(HttpServletRequest request, Fond fond) {
        SqlQuery<Fond> sqlQuery = SqlQuery.from(Fond.class)
                .equal(FondInfo.ENABLED, request.getParameter("enabled"))
                .like(FondInfo.NAME, fond.getName())
                .orderBy(FondInfo.ORDERBY);
        Organise organise = SecurityHolder.get().currentOrganise();
        if (organise.getOrganiseType().equals("das")){
            sqlQuery.equal(FondInfo.PARTYID,organise.getId());
        }
        return sqlQuery;
    }

    @Override
    public ResultEntity<Fond> update(HttpServletRequest request, Fond entity) {
        Fond oldFond = service.selectById(entity.getId());
        ResultEntity<Fond> update = null;
        try {
            update = super.update(request, entity);
            //添加变更记录
            fondChangeHistoryService.updateByChange(oldFond, entity);
        } catch (Exception e) {
            logger.error("更新全宗失败：" + entity.getCode(), e);
        }
        return update;
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, Fond entity) {
        String ids = request.getParameter("ids");
        String[] split = ids.split(",");
        for (String s : split) {
            List<Category> categoryList = categoryService.getCategoryByFondId(s);
            if (categoryList.size()>0){
                return ResultEntity.error("该全宗下还有门类");
            }else {
                service.deleteById(s);
            }
        }
        return ResultEntity.success();
    }
    @RequestMapping("/getFondAll")
    public ResultEntity getFondAll(@Current Person person){
        List<Fond> fonds = service.selectList(SqlQuery.from(Fond.class).equal(FondInfo.ENABLED, true));
        return ResultEntity.success(fonds);
    }

    /**
     * 获取当前登陆人所有全宗
     *
     * @param person
     * @return
     */
    @RequestMapping("/getFondListBypermission")
    public ResultEntity getFondListBypermission(@Current Person person) {
        List<Fond> fonds = (List<Fond>) resourceManager.getPersonResources(person.getId(), "fond");
        return ResultEntity.success(fonds);
    }
}
