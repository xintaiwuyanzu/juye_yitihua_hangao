package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalSpecialDetail;
import com.dr.archive.appraisal.entity.AppraisalSpecialDetailInfo;
import com.dr.archive.appraisal.service.AppraisalSpecialDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴定依据专题管理
 */
@RestController
@RequestMapping("api/appraisalSpecialDetail")
public class AppraisalSpecialDetailController extends BaseServiceController<AppraisalSpecialDetailService, AppraisalSpecialDetail> {

    @Override
    protected SqlQuery<AppraisalSpecialDetail> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalSpecialDetail appraisalSpecialDetail) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalSpecialDetail.class)
                .equal(AppraisalSpecialDetailInfo.SPECIALID,appraisalSpecialDetail.getSpecialId());
        return sqlQuery;
    }
}
