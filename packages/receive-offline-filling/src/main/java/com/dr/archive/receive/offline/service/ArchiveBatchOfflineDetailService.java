package com.dr.archive.receive.offline.service;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;

/**
 * 离线导入详情表
 *
 * @author dr
 */
public interface ArchiveBatchOfflineDetailService extends BaseArchiveBatchDetailService<ArchiveBatchReceiveOfflineDetail> {
    /**
     * 入库详情数据
     *
     * @param offline
     */
    void filling(ArchiveBatchReceiveOffline offline);

    long deleteDetail(String id);

    void createDetail2(BatchCreateQuery query, AbstractArchiveBatch entity);
}
