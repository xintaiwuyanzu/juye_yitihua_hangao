package com.dr.archive.fuzhou.configManager.service;

import com.dr.archive.batch.query.BatchCreateQuery;

/**
 * @Author: caor
 * @Date: 2022-01-06 15:14
 * @Description:
 */
public interface ArchiveCodeService {
    /**
     * @param typeCode 分类编码
     * @param query
     * @return
     */
    String getArchiveByCategoryCodeAndYear(String typeCode, BatchCreateQuery query);
}
