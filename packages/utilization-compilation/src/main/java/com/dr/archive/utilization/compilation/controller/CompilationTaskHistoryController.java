package com.dr.archive.utilization.compilation.controller;

import com.dr.archive.utilization.compilation.entity.CompilationTaskHistory;
import com.dr.archive.utilization.compilation.entity.CompilationTaskHistoryInfo;
import com.dr.archive.utilization.compilation.service.CompilationTaskHistoryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-03-03 16:18
 * @Description:
 */
@RestController
@RequestMapping("api/compilationTaskHistory")
public class CompilationTaskHistoryController extends BaseServiceController<CompilationTaskHistoryService, CompilationTaskHistory> {
    @Override
    protected SqlQuery<CompilationTaskHistory> buildPageQuery(HttpServletRequest httpServletRequest, CompilationTaskHistory compilationTaskHistory) {
        return SqlQuery.from(CompilationTaskHistory.class)
                .equal(CompilationTaskHistoryInfo.COMPILATIONTASKID, compilationTaskHistory.getCompilationTaskId())
                .equal(CompilationTaskHistoryInfo.PROCESSINSTANCEID, compilationTaskHistory.getProcessInstanceId())
                .equal(CompilationTaskHistoryInfo.TASKINSTANCEID, compilationTaskHistory.getTaskInstanceId())
                .equal(CompilationTaskHistoryInfo.HISTORYTYPE, compilationTaskHistory.getHistoryType())
                .orderByDesc(CompilationTaskHistoryInfo.CREATEDATE);
    }
}