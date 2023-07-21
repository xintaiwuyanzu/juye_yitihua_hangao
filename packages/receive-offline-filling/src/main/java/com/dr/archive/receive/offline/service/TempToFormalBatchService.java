package com.dr.archive.receive.offline.service;

import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.offline.entity.TempToFormalBatch;
import com.dr.framework.common.service.BaseService;

import java.util.Map;

/**
 * 临时库到正式库批次
 */
public interface TempToFormalBatchService extends BaseService<TempToFormalBatch> {

    /**
     * 创建批次
     */
    Map newBatch(ArchiveDataQuery query, String status, String archiveType);
    /**
     * 获得入库检测的接收报告
     */
    Map<String, Long> getReport(String batchId);

}
