package com.dr.archive.fuzhou.approve.fileReturn.service;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.framework.common.entity.ResultEntity;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2021-12-01 16:04
 * @Description:
 */
public interface ReturnService {
    /**
     * @param query
     * @param returnReason
     * @return
     */
    String fileReturn(BatchCreateQuery query, String returnReason);

    ResultEntity fileReturnCheck(BatchCreateQuery query);


    void newFileReturn(BatchCreateQuery query, String receiptIds);
}
