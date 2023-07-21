package com.dr.archive.utilization.compilation.controller;

import com.dr.archive.utilization.compilation.entity.CompilationTaskDetail;
import com.dr.archive.utilization.compilation.entity.CompilationTaskDetailInfo;
import com.dr.archive.utilization.compilation.service.CompilationTaskDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-08-20 16:55
 * @Description:
 */
@RestController
@RequestMapping("api/compilationTaskDetail")
public class CompilationTaskDetailController extends BaseServiceController<CompilationTaskDetailService, CompilationTaskDetail> {
    @Override
    protected SqlQuery<CompilationTaskDetail> buildPageQuery(HttpServletRequest httpServletRequest, CompilationTaskDetail compilationTaskDetail) {
        return SqlQuery.from(CompilationTaskDetail.class)
                .equal(CompilationTaskDetailInfo.BATCHID, compilationTaskDetail.getBatchId())
                .orderBy(CompilationTaskDetailInfo.CREATEDATE);
    }
}
