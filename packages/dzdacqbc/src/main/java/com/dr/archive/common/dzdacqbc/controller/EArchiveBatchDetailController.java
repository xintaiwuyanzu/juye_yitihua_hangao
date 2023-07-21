package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.EArchiveBatchDetail;
import com.dr.archive.common.dzdacqbc.entity.EArchiveBatchDetailInfo;
import com.dr.archive.common.dzdacqbc.service.EArchiveBatchDetailService;
import com.dr.archive.common.dzdacqbc.service.EArchiveBatchService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 出入库批次详情
 */
@RestController
@RequestMapping("/api/earchive/batchDetail")
public class EArchiveBatchDetailController extends BaseServiceController<EArchiveBatchDetailService, EArchiveBatchDetail> {

    @Autowired
    EArchiveBatchService EArchiveBatchService;

    @Autowired
    EArchiveBatchDetailService eArchiveBatchDetailService;

    @RequestMapping({"/checkSubmit"})
    public ResultEntity checkSubmit(String deliveryId) {
        return ResultEntity.success(EArchiveBatchService.checkSubmit(deliveryId));
    }

    @RequestMapping({"/fastDelivery"})
    public ResultEntity fastDelivery(String deliveryId) {
        return ResultEntity.success(EArchiveBatchService.fastDelivery(deliveryId));
    }

    @Override
    protected SqlQuery<EArchiveBatchDetail> buildPageQuery(HttpServletRequest request, EArchiveBatchDetail entity) {
        SqlQuery<EArchiveBatchDetail> sqlQuery = SqlQuery.from(EArchiveBatchDetail.class);

        sqlQuery.equal(EArchiveBatchDetailInfo.BATCHID, entity.getBatchId());
        sqlQuery.like(EArchiveBatchDetailInfo.ARCHIVECODE, entity.getArchiveCode());
        sqlQuery.like(EArchiveBatchDetailInfo.TITLE, entity.getTitle());
        return sqlQuery;
    }
    @RequestMapping({"/deleteById"})
    public ResultEntity deleteById(String id,String batchId){
        return eArchiveBatchDetailService.deleteById(id,batchId);
    }
    /**
     * 获得档案出库详情page
     */
    @RequestMapping("/getDetail/page")
    public ResultEntity getDetailPage(String batchId,  String archiveCode,
                                      @RequestParam(defaultValue = "0") int pageIndex,
                                      @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE_STR) int pageSize){
        return ResultEntity.success(service.getDetailPage(batchId, archiveCode, pageIndex, pageSize));
    }
}
