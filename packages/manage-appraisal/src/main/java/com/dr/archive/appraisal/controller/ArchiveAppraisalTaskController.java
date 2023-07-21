package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;
import com.dr.archive.appraisal.entity.ArchiveAppraisalTaskInfo;
import com.dr.archive.appraisal.service.ArchiveAppraisalTaskService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴定依据管理
 */
@RestController
@RequestMapping("api/archiveAppraisalTask")
public class ArchiveAppraisalTaskController extends BaseServiceController<ArchiveAppraisalTaskService, ArchiveAppraisalTask> {

    @Override
    protected SqlQuery<ArchiveAppraisalTask> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveAppraisalTask archiveAppraisalTask) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class);
        sqlQuery.equal(ArchiveAppraisalTaskInfo.BATCHID, archiveAppraisalTask.getBatchId());
        sqlQuery.equal(ArchiveAppraisalTaskInfo.TASKID, archiveAppraisalTask.getTaskId());
        sqlQuery.equal(ArchiveAppraisalTaskInfo.STATUS, archiveAppraisalTask.getStatus());
        sqlQuery.equal(ArchiveAppraisalTaskInfo.PERSONRESULT, archiveAppraisalTask.getPersonResult());
        sqlQuery.equal(ArchiveAppraisalTaskInfo.VINTAGES, archiveAppraisalTask.getVintages());
        sqlQuery.like(ArchiveAppraisalTaskInfo.ARCHIVECODE, archiveAppraisalTask.getArchiveCode());
        sqlQuery.like(ArchiveAppraisalTaskInfo.TITLE, archiveAppraisalTask.getTitle());
        sqlQuery.orderBy(ArchiveAppraisalTaskInfo.ARCHIVECODE);
        Organise organise = SecurityHolder.get().currentOrganise();
        if (!StringUtils.isEmpty(archiveAppraisalTask.getOrganiseId())&&"dag".equals(organise.getOrganiseType())) {
            sqlQuery.equal(ArchiveAppraisalTaskInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId());
        }
        return sqlQuery;
    }

    @RequestMapping({"/getAppraisalOne"})
    public ResultEntity getAppraisalOne(String formDefinitionId, String formDataId, String taskId) {
        return ResultEntity.success(service.getAppraisalOne(formDefinitionId, formDataId, taskId));
    }

    @RequestMapping({"/checkSubmit"})
    public ResultEntity checkSubmit(String taskId) {
        return ResultEntity.success(service.checkSubmit(taskId));
    }

    @RequestMapping({"/updateSeeHistory"})
    public ResultEntity updateSeeHistory(String formDefinitionId, String formDataId, String taskId) {
        service.updateSeeHistory(formDefinitionId, formDataId, taskId);
        return ResultEntity.success();
    }

    @RequestMapping({"/fastAppraisal"})
    public ResultEntity fastAppraisal(String taskId, String auxiliaryResult, String delayYear) {
        return ResultEntity.success(service.fastAppraisal(taskId, auxiliaryResult, delayYear));
    }

    @RequestMapping({"/fastAppraisalByIds"})
    public ResultEntity fastAppraisalByIds(String taskId, String auxiliaryResult, String delayYear, String ids) {
        return ResultEntity.success(service.fastAppraisalByIds(taskId, auxiliaryResult, delayYear, ids));
    }

    @RequestMapping({"/fastAppraisalBySearch"})
    public ResultEntity fastAppraisalBySearch(String taskId, String auxiliaryResult, String delayYear, ArchiveAppraisalTask archiveAppraisalTask) {
        return ResultEntity.success(service.fastAppraisalBySearch(taskId, auxiliaryResult, delayYear, archiveAppraisalTask));
    }


}
