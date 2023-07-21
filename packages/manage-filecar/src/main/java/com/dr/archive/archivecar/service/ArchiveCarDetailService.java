package com.dr.archive.archivecar.service;

import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.framework.common.service.BaseService;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-01-18 19:35
 * @Description:
 */
public interface ArchiveCarDetailService extends BaseService<ArchiveCarDetail> {
    /**
     * 根据档案批次和标记查询档案车档案列表
     *
     *
     * @param batchId
     * @param tag
     * @return
     */
    List<ArchiveCarDetail> selectByBatchAndTag(String batchId, String tag);
}
