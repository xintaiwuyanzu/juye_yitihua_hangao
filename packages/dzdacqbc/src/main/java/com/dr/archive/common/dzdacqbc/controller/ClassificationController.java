package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.annotation.CqbcLog;
import com.dr.archive.common.dzdacqbc.entity.CqbcClassification;
import com.dr.archive.common.dzdacqbc.entity.CqbcClassificationInfo;
import com.dr.archive.common.dzdacqbc.service.ClassificationService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 分类信息配置
 *
 * @author hyj
 */

@RestController
@RequestMapping("/api/classification")
public class ClassificationController extends BaseServiceController<ClassificationService, CqbcClassification> {

    @Override
    protected SqlQuery<CqbcClassification> buildPageQuery(HttpServletRequest request, CqbcClassification entity) {
        return SqlQuery.from(CqbcClassification.class).like(CqbcClassificationInfo.CLASSIFICATIONNAME, entity.getClassificationName()).like(CqbcClassificationInfo.FONDNAME, entity.getFondName()).like(CqbcClassificationInfo.CLASSNAME, entity.getClassName()).orderByDesc(CqbcClassificationInfo.CREATEDATE);
    }

    @Override
    @CqbcLog("新增分类设置")
    public ResultEntity<CqbcClassification> insert(HttpServletRequest request, CqbcClassification entity) {
        return super.insert(request, entity);
    }

    @Override
    @CqbcLog("删除分类设置")
    public ResultEntity<Boolean> delete(HttpServletRequest request, CqbcClassification entity) {
        return super.delete(request, entity);
    }

    @Override
    @CqbcLog("修改分类设置")
    public ResultEntity<CqbcClassification> update(HttpServletRequest request, CqbcClassification entity) {
        return super.update(request, entity);
    }

    @RequestMapping("/updateDefault")
    public ResultEntity updateDefault(CqbcClassification cqbcClassification) {
        service.updateDefault(cqbcClassification);
        return ResultEntity.success("设置成功");
    }
}
