package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalBatchWordGroup;
import com.dr.archive.appraisal.service.AppraisalBatchWordGroupService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 档案鉴定批次关联词组
 */
@RestController
@RequestMapping("api/appraisalBatchWordGroup")
public class AppraisalBatchWordGroupController extends BaseServiceController<AppraisalBatchWordGroupService, AppraisalBatchWordGroup> {

    @Override
    protected SqlQuery<AppraisalBatchWordGroup> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalBatchWordGroup appraisalBatchWordGroup) {
        return null;
    }

    @RequestMapping({"/updatePriority"})
    public ResultEntity updatePriority(AppraisalBatchWordGroup appraisalBatchWordGroup) {
        service.updatePriority(appraisalBatchWordGroup);
        return ResultEntity.success();
    }

    @RequestMapping({"/getWordGroupIdsByBatchId"})
    public ResultEntity getWordGroupIdsByBatchId(String batchId) {
        return ResultEntity.success(service.getWordGroupIdsByBatchId(batchId));
    }

    @RequestMapping({"/updateAppraisalBatchWordGroup"})
    public ResultEntity updateAppraisalBatchWordGroup(String batchId,String wordGroupIds) {
        service.updateAppraisalBatchWordGroup(batchId,wordGroupIds);
        return ResultEntity.success();
    }
}
