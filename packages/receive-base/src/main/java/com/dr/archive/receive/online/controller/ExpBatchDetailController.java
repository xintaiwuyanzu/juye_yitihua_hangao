package com.dr.archive.receive.online.controller;

import com.dr.archive.receive.online.entity.ExpBatchDetail;
import com.dr.archive.receive.online.entity.ExpBatchDetailInfo;
import com.dr.archive.receive.online.service.ExpBatchDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yang
 * @create: 2022-08-04 11:20
 **/
@RestController
@RequestMapping("/api/batchDetail")
public class ExpBatchDetailController extends BaseServiceController<ExpBatchDetailService, ExpBatchDetail> {
    @Override
    protected SqlQuery<ExpBatchDetail> buildPageQuery(HttpServletRequest httpServletRequest, ExpBatchDetail expBatchDetail) {
        return SqlQuery.from(ExpBatchDetail.class).equal(ExpBatchDetailInfo.BATCHID, expBatchDetail.getBatchId()).like(ExpBatchDetailInfo.TITLE,expBatchDetail.getTitle()).like(ExpBatchDetailInfo.FONDNAME,expBatchDetail.getFondName());
    }
}
