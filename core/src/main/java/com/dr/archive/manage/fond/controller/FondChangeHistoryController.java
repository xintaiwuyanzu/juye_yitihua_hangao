package com.dr.archive.manage.fond.controller;

import com.dr.archive.manage.fond.entity.FondChangeHistory;
import com.dr.archive.manage.fond.entity.FondChangeHistoryInfo;
import com.dr.archive.manage.fond.service.FondChangeHistoryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 */
@RestController
@RequestMapping("/api/manage/fondchangehistory")
public class FondChangeHistoryController extends BaseServiceController<FondChangeHistoryService, FondChangeHistory> {


    @Override
    protected SqlQuery<FondChangeHistory> buildPageQuery(HttpServletRequest request, FondChangeHistory fondChangeHistory) {
        SqlQuery<FondChangeHistory> sqlQuery = SqlQuery.from(FondChangeHistory.class);
        if (!StringUtils.isEmpty(fondChangeHistory.getFondName())) {
            sqlQuery.like(FondChangeHistoryInfo.FONDNAME, fondChangeHistory.getFondName());
        }
        if (!StringUtils.isEmpty(fondChangeHistory.getNewFondName())) {
            sqlQuery.like(FondChangeHistoryInfo.NEWFONDNAME, fondChangeHistory.getNewFondName());
        }
        sqlQuery.orderByDesc(FondChangeHistoryInfo.NEWCREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, FondChangeHistory entity) {
        String ids = request.getParameter("ids");
        if (!StringUtils.isEmpty(ids)) {
            service.deleteById(ids.split(","));
            return ResultEntity.success(true);
        }
        return ResultEntity.error(false);
    }
}
