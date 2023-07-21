package com.dr.archive.batch.service;

import com.dr.archive.batch.entity.ArchiveBatchDetailComment;
import com.dr.framework.common.service.BaseService;

/**
 * 档案详情意见表
 *
 * @author dr
 */
public interface ArchiveBatchDetailCommentService extends BaseService<ArchiveBatchDetailComment> {
    /**
     * 根据详情Id删除数据
     *
     * @param detailId
     * @return
     */
    long deleteByDetailId(String detailId);

    /**
     * 根据批次Id删除数据
     *
     * @param batchId
     * @return
     */
    long deleteByBatchId(String batchId);
}
