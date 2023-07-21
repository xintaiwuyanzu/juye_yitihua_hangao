package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.annotation.CqbcLog;
import com.dr.archive.common.dzdacqbc.entity.ESaveTactics;
import com.dr.archive.common.dzdacqbc.entity.ESaveTacticsInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveTacticsService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局策略配置
 *
 * @author hyj
 */
@RestController
@RequestMapping("/api/earchive/tactics")
public class ESaveTacticsController extends BaseController<ESaveTactics> {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ESaveTacticsService ESaveTacticsService;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<ESaveTactics> sqlQuery, ESaveTactics entity) {
        super.onBeforePageQuery(request, sqlQuery, entity);
        sqlQuery.like(ESaveTacticsInfo.TACTICSNAME, entity.getTacticsName());
        sqlQuery.orderBy(ESaveTacticsInfo.CREATEDATE);
    }

    @Override
    protected void onBeforeInsert(HttpServletRequest request, ESaveTactics entity) {
        super.onBeforeInsert(request, entity);
        entity.setIsDefault("false");
    }

    @Override
    @CqbcLog("添加全局策略设置")
    public ResultEntity<ESaveTactics> insert(HttpServletRequest request, ESaveTactics entity) {
        return super.insert(request, entity);
    }

    @Override
    @CqbcLog("修改全局策略设置")
    public ResultEntity<ESaveTactics> update(HttpServletRequest request, ESaveTactics entity) {
        return super.update(request, entity);
    }

    @Override
    @CqbcLog("删除全局策略设置")
    public ResultEntity<Boolean> delete(HttpServletRequest request, ESaveTactics entity) {
        return super.delete(request, entity);
    }

    @RequestMapping("/updateDefault")
    public ResultEntity updateDefault(ESaveTactics tactics) {
        ESaveTacticsService.updateDefault(tactics);
        return ResultEntity.success("设置成功");
    }

    @RequestMapping("checkIsStart")
    public ResultEntity checkIsStart() {
        return ResultEntity.success(ESaveTacticsService.checkIsStart());
    }

}
