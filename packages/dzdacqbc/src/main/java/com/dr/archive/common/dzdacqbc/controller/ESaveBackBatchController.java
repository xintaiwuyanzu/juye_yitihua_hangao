package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.ESaveBackBatch;
import com.dr.archive.common.dzdacqbc.entity.ESaveBackBatchInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveBackBatchService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/backBatch")
public class ESaveBackBatchController extends BaseServiceController<ESaveBackBatchService, ESaveBackBatch> {


    @Override
    protected SqlQuery<ESaveBackBatch> buildPageQuery(HttpServletRequest httpServletRequest, ESaveBackBatch eSaveBackBatch) {
        SqlQuery<ESaveBackBatch> sqlQuery = SqlQuery.from(ESaveBackBatch.class);
        if(StringUtils.hasText(eSaveBackBatch.getBatchName())){
            sqlQuery.like(ESaveBackBatchInfo.BATCHNAME, eSaveBackBatch.getBatchName());
        }
        if(StringUtils.hasText(eSaveBackBatch.getStrategyId())){
            sqlQuery.equal(ESaveBackBatchInfo.STRATEGYID, eSaveBackBatch.getStrategyId());
        }
        sqlQuery.equal(ESaveBackBatchInfo.ORGID, SecurityHolder.get().currentOrganise().getId());
        sqlQuery.orderByDesc(ESaveBackBatchInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<ESaveBackBatch> insert(HttpServletRequest request, ESaveBackBatch entity) {
        entity.setStatus("0");
        entity.setBackCount(0);
        entity.setBatchCode(String.valueOf(System.currentTimeMillis()));
        entity.setIsExpire("0");
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        return super.insert(request, entity);
    }

    /**
     * 数据删除
     * @return
     */
    @RequestMapping("/delDetail")
    public ResultEntity<Boolean> delDetail(String batchId) {
        return service.delDetail(batchId);
    }

    /**
     * 执行备份策略
     */
    @RequestMapping("/startWork")
    public ResultEntity startWork(String batchId) {
        service.startWork(batchId);
        return ResultEntity.success();
    }
}
