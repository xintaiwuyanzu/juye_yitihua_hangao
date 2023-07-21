package com.dr.archive.utilization.consult.service;

import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchDetailConsult;

/**
 * 查档利用service
 *
 * @author dr
 */
public interface ArchiveBatchDetailConsultService extends BaseArchiveBatchDetailService<ArchiveBatchDetailConsult> {
    /**
     * 复制详情数据到查档详情
     *
     * @param detail
     * @param batchConsult
     */
    void copyDetail(ArchiveCarDetail detail, ArchiveBatchConsult batchConsult);
}
