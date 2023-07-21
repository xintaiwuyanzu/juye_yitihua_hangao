package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.annotation.CqbcLog;
import com.dr.archive.common.dzdacqbc.entity.ESaveSpaces;
import com.dr.archive.common.dzdacqbc.entity.ESaveSpacesInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveSpacesService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局策略配置
 *
 * @author hyj
 */

@RestController
@RequestMapping("/api/earchive/spaces")
public class ESaveSpacesController extends BaseServiceController<ESaveSpacesService, ESaveSpaces> {

    @Override
    protected SqlQuery<ESaveSpaces> buildPageQuery(HttpServletRequest request, ESaveSpaces entity) {
        return SqlQuery.from(ESaveSpaces.class)
                .equal(ESaveSpacesInfo.ID,entity.getId())
                .like(ESaveSpacesInfo.SPACENAME, entity.getSpaceName())
                .like(ESaveSpacesInfo.CATALOGUE, entity.getCatalogue())
                .orderByDesc(ESaveSpacesInfo.CREATEDATE);
    }

    @Override
    @CqbcLog("新增存储空间设置")
    public ResultEntity<ESaveSpaces> insert(HttpServletRequest request, ESaveSpaces entity) {
        return super.insert(request, entity);
    }

    @Override
    @CqbcLog("删除存储空间设置")
    public ResultEntity<Boolean> delete(HttpServletRequest request, ESaveSpaces entity) {
        return super.delete(request, entity);
    }

    @RequestMapping("/updateDefault")
    public ResultEntity updateDefault(ESaveSpaces spaces) {
        service.updateDefault(spaces);
        return ResultEntity.success("设置成功");
    }

    @RequestMapping("/getSpace")
    public ResultEntity getSpace(String catalogue) {
        return ResultEntity.success(service.getCapacity(catalogue));
    }

    @RequestMapping("/getMonitorData")
    public ResultEntity getMonitorData() {
        return ResultEntity.success(service.getMonitorData());
    }
}
