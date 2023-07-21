package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.ESaveBackBatchDetail;
import com.dr.archive.common.dzdacqbc.entity.ESaveBackBatchDetailInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveBackBatchDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/backBatchDetail")
public class ESaveBackBatchDetailController extends BaseServiceController<ESaveBackBatchDetailService, ESaveBackBatchDetail> {


    @Override
    protected SqlQuery<ESaveBackBatchDetail> buildPageQuery(HttpServletRequest httpServletRequest, ESaveBackBatchDetail entity) {
        SqlQuery<ESaveBackBatchDetail> sqlQuery = SqlQuery.from(ESaveBackBatchDetail.class);
        if(StringUtils.hasText(entity.getBatchId())){
            sqlQuery.equal(ESaveBackBatchDetailInfo.BATCHID, entity.getBatchId());
        }
        if(StringUtils.hasText(entity.getTiming())){
            sqlQuery.like(ESaveBackBatchDetailInfo.TIMING, entity.getTiming());
        }
        if(StringUtils.hasText(entity.getBackType())){
            sqlQuery.like(ESaveBackBatchDetailInfo.BACKTYPE, entity.getBackType());
        }
        sqlQuery.orderBy(ESaveBackBatchDetailInfo.ARCHIVECODE);
        return sqlQuery;
    }
    /**
     * 备份数据恢复
     */
    @RequestMapping("/dataRecovery")
    public ResultEntity dataRecovery(String detailId){
        service.dataRecovery(detailId, "bf");
        return ResultEntity.success();
    }
}
