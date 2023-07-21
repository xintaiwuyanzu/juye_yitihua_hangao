package com.dr.archive.managefondsdescriptivefile.service;

import com.dr.archive.managefondsdescriptivefile.entity.ManagementHistory;
import com.dr.framework.common.service.BaseService;

/**
 * @Author: caor
 * @Date: 2022-02-23 20:52
 * @Description:
 */
public interface ManagementHistoryService extends BaseService<ManagementHistory> {
    /**
     *
     * @param bussinessId
     * @return
     */
    long deleteByBussinessId(String bussinessId);
}
