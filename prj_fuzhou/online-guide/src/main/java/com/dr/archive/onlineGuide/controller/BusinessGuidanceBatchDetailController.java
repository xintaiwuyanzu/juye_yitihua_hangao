package com.dr.archive.onlineGuide.controller;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetail;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetailInfo;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-06-10 18:20
 * @Description:
 */
@RestController
@RequestMapping("api/businessGuidanceBatchDetail")
public class BusinessGuidanceBatchDetailController extends BaseServiceController<BusinessGuidanceBatchDetailService, BusinessGuidanceBatchDetail> {
    @Override
    protected SqlQuery<BusinessGuidanceBatchDetail> buildPageQuery(HttpServletRequest httpServletRequest, BusinessGuidanceBatchDetail businessGuidanceBatchDetail) {
        return SqlQuery.from(BusinessGuidanceBatchDetail.class)
                .equal(BusinessGuidanceBatchDetailInfo.BATCHID, businessGuidanceBatchDetail.getBatchId())
                .like(BusinessGuidanceBatchDetailInfo.TITLE, businessGuidanceBatchDetail.getTitle())
                .orderBy(BusinessGuidanceBatchDetailInfo.ARCHIVECODE);
    }

    @RequestMapping("/getBatchTaskQuantity")
    public ResultEntity getBatchTaskQuantity(String batchId) {
        return ResultEntity.success(service.getBatchTaskQuantity(batchId));
    }

    @RequestMapping("/fastReply")
    public ResultEntity fastReply(String batchId,String result) {
        return ResultEntity.success(service.fastReply(batchId,result));
    }

    @RequestMapping("/fastReplyByIds")
    public ResultEntity fastReplyByIds(String batchId,String result,String ids) {
        return ResultEntity.success(service.fastReplyByIds(batchId,result,ids));
    }

    @RequestMapping("/fastReplyBySearch")
    public ResultEntity fastReplyBySearch(String batchId,String result,BusinessGuidanceBatchDetail businessGuidanceBatchDetail) {
        return ResultEntity.success(service.fastReplyBySearch(batchId,result,businessGuidanceBatchDetail));
    }


}
