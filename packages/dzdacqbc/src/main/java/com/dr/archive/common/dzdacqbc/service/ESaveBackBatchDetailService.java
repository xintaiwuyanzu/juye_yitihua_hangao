package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ESaveBackBatchDetail;
import com.dr.framework.common.service.BaseService;

public interface ESaveBackBatchDetailService extends BaseService<ESaveBackBatchDetail> {

    void addDetail(String batchId, String earchiveId, String spaceId, String newPath);


    void addDetailLX(String offLineId);

    void dataRecovery(String detailId, String type);
}
