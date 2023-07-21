package com.dr.archive.utilization.compilation.service;

import com.dr.archive.utilization.compilation.entity.CompilationTaskHistory;
import com.dr.framework.common.service.BaseService;

/**
 * @Author: caor
 * @Date: 2022-03-03 16:10
 * @Description:
 */
public interface CompilationTaskHistoryService extends BaseService<CompilationTaskHistory> {
    /**
     * 根据编研任务Id删除数据
     *
     * @param taskId
     * @return
     */
    long deleteByTaskId(String taskId);
}
