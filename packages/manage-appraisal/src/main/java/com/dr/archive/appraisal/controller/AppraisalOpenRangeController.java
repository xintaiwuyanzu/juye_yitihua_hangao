package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalOpenRange;
import com.dr.archive.appraisal.entity.AppraisalOpenRangeInfo;
import com.dr.archive.appraisal.service.AppraisalOpenRangeService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴定依据管理
 */
@RestController
@RequestMapping("api/appraisalOpenRange")
public class AppraisalOpenRangeController extends BaseServiceController<AppraisalOpenRangeService,AppraisalOpenRange> {

    @Override
    protected SqlQuery<AppraisalOpenRange> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalOpenRange appraisalOpenRange) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalOpenRange.class)
                .equal(AppraisalOpenRangeInfo.AUXILIARYRESULT,appraisalOpenRange.getAuxiliaryResult())
                .equal(AppraisalOpenRangeInfo.CODE,appraisalOpenRange.getCode())
                .equal(AppraisalOpenRangeInfo.OPENRANGE,appraisalOpenRange.getOpenRange())
                .equal(AppraisalOpenRangeInfo.PRIORITY,appraisalOpenRange.getPriority())
                .orderBy(AppraisalOpenRangeInfo.PRIORITY);
        return sqlQuery;
    }
}
