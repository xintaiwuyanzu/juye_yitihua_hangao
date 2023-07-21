package com.dr.archive.receive.offline.controller;

import com.dr.archive.receive.offline.entity.TempToFormalBatchDetail;
import com.dr.archive.receive.offline.entity.TempToFormalBatchDetailInfo;
import com.dr.archive.receive.offline.service.TempToFormalBatchDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 临时库到正式库前的四性检测批次详情controller
 */
@RestController
@RequestMapping("${common.api-path:/api}/tempToFormal/batch/detail")
public class TempToFormalBatchDetailController extends BaseServiceController<TempToFormalBatchDetailService, TempToFormalBatchDetail> {

    /**
     * 查询方法
     */
    @Override
    protected SqlQuery<TempToFormalBatchDetail> buildPageQuery(HttpServletRequest httpServletRequest, TempToFormalBatchDetail detail) {
        SqlQuery<TempToFormalBatchDetail> sqlQuery = SqlQuery.from(TempToFormalBatchDetail.class);
        if (StringUtils.hasText(detail.getBatchId())) {
            sqlQuery.equal(TempToFormalBatchDetailInfo.BATCHID, detail.getBatchId());
        }
        if(StringUtils.hasText(detail.getTitle())){
            sqlQuery.like(TempToFormalBatchDetailInfo.TITLE, detail.getTitle());
        }
        if(StringUtils.hasText(detail.getTestStatus())){
            sqlQuery.equal(TempToFormalBatchDetailInfo.TESTSTATUS, detail.getTestStatus());
        }
        sqlQuery.orderByDesc(TempToFormalBatchDetailInfo.CREATEDATE);
        return sqlQuery;
    }
}
