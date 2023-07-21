package com.dr.archive.utilization.consult.service;

import com.dr.archive.batch.service.BaseArchiveBatchService;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;

/**
 * 查档利用service
 *
 * @author dr
 */
public interface ArchiveBatchConsultService extends BaseArchiveBatchService<ArchiveBatchConsult> {

    /**
     * 利用状态编辑中
     */
    String CONSULT_STATUS_EDIT = "10";
    /**
     * 审核中
     */
    String CONSULT_STATUS_AUDIT = "20";
    /**
     * 已办结
     */
    String CONSULT_STATUS_DONE = "30";

    void toEnd(String id,String remarks);
}
