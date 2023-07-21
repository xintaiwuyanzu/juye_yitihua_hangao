package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ESaveBackBatch;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;

public interface ESaveBackBatchService extends BaseService<ESaveBackBatch> {

    /**
     * 添加备份批次
     */
    ESaveBackBatch addBatch(String strategyId, String type);

    /**
     * 执行
     */
    void startWork(String batchId);

    ResultEntity<Boolean> delDetail(String batchId);
}
