package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.ESaveStrategy;
import com.dr.archive.common.dzdacqbc.entity.ESaveStrategyInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveStrategyService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 存储和备份策略
 */

@RestController
@RequestMapping("/api/strategy")
public class ESaveStrategyController extends BaseServiceController<ESaveStrategyService, ESaveStrategy> {

    @Override
    protected SqlQuery<ESaveStrategy> buildPageQuery(HttpServletRequest httpServletRequest, ESaveStrategy eSaveStrategy) {
        SqlQuery<ESaveStrategy> sqlQuery = SqlQuery.from(ESaveStrategy.class);
        if (StringUtils.hasText(eSaveStrategy.getStrategyName())) {
            sqlQuery.like(ESaveStrategyInfo.STRATEGYNAME, eSaveStrategy.getStrategyName());
        }
        sqlQuery.equal(ESaveStrategyInfo.ORGID, SecurityHolder.get().currentOrganise().getId());
        return sqlQuery;
    }

    @Override
    public ResultEntity<ESaveStrategy> insert(HttpServletRequest request, ESaveStrategy entity) {
        entity = service.dealData(entity);
        return super.insert(request, entity);
    }

    @Override
    public ResultEntity<ESaveStrategy> update(HttpServletRequest request, ESaveStrategy entity) {
        entity = service.dealData(entity);
        return super.update(request, entity);
    }
    /**
     * 获得当前全宗下，绑定过表单的门类
     */
    @RequestMapping("/getCateByBind")
    public ResultEntity getCateByBind(String fondId){
        return ResultEntity.success(service.getCateByBind(fondId));
    }

}
