package com.dr.archive.receive.offline.service;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.offline.entity.TempToFormalBatch;
import com.dr.archive.receive.offline.entity.TempToFormalBatchDetail;
import com.dr.framework.common.service.BaseService;

/**
 * 临时库到正式库批次详情
 */
public interface TempToFormalBatchDetailService extends BaseService<TempToFormalBatchDetail> {

    TempToFormalBatch createDetail2(ArchiveDataQuery query, AbstractArchiveBatch entity, String status, String archiveType);

    void deleteEntityFiles(BatchCreateQuery query);
}
