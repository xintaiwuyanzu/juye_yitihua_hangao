package com.dr.archive.onlineGuide.service;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetail;
import com.dr.framework.common.service.BaseService;

/**
 * @Author: caor
 * @Date: 2022-06-10 15:22
 * @Description:
 */
public interface BusinessGuidanceBatchDetailService extends BaseService<BusinessGuidanceBatchDetail> {

    Long getBatchTaskQuantity(String batchId);

    Long fastReply(String batchId,String result);

    Long fastReplyByIds(String batchId,String result,String ids);

    Long fastReplyBySearch(String batchId,String result,BusinessGuidanceBatchDetail businessGuidanceBatchDetail);
}
