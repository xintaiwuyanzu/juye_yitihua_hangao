package com.dr.archive.examine.controller;


import com.dr.archive.examine.entity.ZfjcCheckResult;
import com.dr.archive.examine.entity.ZfjcCheckResultInfo;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 执法检查结果
 */
@RestController
@RequestMapping("api/zfjcCheckResult/")
public class ZfjcCheckResultController extends BaseController<ZfjcCheckResult> {

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<ZfjcCheckResult> sqlQuery, ZfjcCheckResult entity) {
        sqlQuery.equal(ZfjcCheckResultInfo.PID, entity.getpId()).orderByDesc(ZfjcCheckResultInfo.UPDATEDATE);
    }
}
