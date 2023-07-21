package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.DestoryHistory;
import com.dr.archive.appraisal.entity.DestoryHistoryInfo;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 销毁记录管理
 */
@RestController
@RequestMapping("api/destoryHistory")
public class DestoryHistoryController extends BaseController<DestoryHistory> {

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<DestoryHistory> sqlQuery, DestoryHistory entity) {
        sqlQuery.equal(DestoryHistoryInfo.ORGID, SecurityHolder.get().currentOrganise().getId())
                .equal(DestoryHistoryInfo.BATCHID,entity.getBatchId())
                .orderBy(DestoryHistoryInfo.CREATEDATE);
    }

}
