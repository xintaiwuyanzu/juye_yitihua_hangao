package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.EArchiveBatchDetail;
import com.dr.archive.common.dzdacqbc.vo.DeliveryDetailVo;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;

/**
 * 出入库批次详情service
 *
 * @author dr
 */
public interface EArchiveBatchDetailService extends BaseService<EArchiveBatchDetail> {

    ResultEntity deleteById(String id, String batchId);

    Page<DeliveryDetailVo> getDetailPage(String batchId, String archiveCode, int pageIndex, int pageSize);
}
