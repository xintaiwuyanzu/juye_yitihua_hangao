package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalBatchTask;
import com.dr.archive.appraisal.entity.AppraisalBatchTaskInfo;
import com.dr.archive.appraisal.service.*;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴定任务管理
 */
@RestController
@RequestMapping("api/appraisalBatchTask")
public class AppraisalBatchTaskController extends BaseServiceController<AppraisalBatchTaskService, AppraisalBatchTask> {

    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;
    @Autowired
    ArchiveAppraisalTaskService archiveAppraisalTaskService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    AppraisalBatchService appraisalBatchService;
    @Autowired
    ArchiveAppraisalMessageService archiveAppraisalMessageService;

    @Override
    protected SqlQuery<AppraisalBatchTask> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalBatchTask appraisalBatchTask) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatchTask.class)
                .equal(AppraisalBatchTaskInfo.BATCHID, appraisalBatchTask.getBatchId())
                .equal(AppraisalBatchTaskInfo.TASKWORDGROUP,appraisalBatchTask.getTaskWordGroup())
                .equal(AppraisalBatchTaskInfo.STATUS, appraisalBatchTask.getStatus())
                .orderByDesc(AppraisalBatchTaskInfo.CREATEDATE);
        if("true".equals(appraisalBatchTask.getCreatePerson())){
            sqlQuery.equal(AppraisalBatchTaskInfo.CREATEPERSON,getUserLogin(httpServletRequest).getId());
        }
        return sqlQuery;
    }

    @RequestMapping({"/createAppraisalBatchTask"})
    public ResultEntity createAppraisalBatchTask(HttpServletRequest httpServletRequest, AppraisalBatchTask appraisalBatchTask,String personId) {
        Person person;
        if (StringUtils.isEmpty(personId)){
            person = getUserLogin(httpServletRequest);
        }else{
            person = organisePersonService.getPersonById(personId);
        }
        return service.createAppraisalBatchTask(person, appraisalBatchTask);
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AppraisalBatchTask entity) {
        String [] ids = entity.getId().split(",");
        for(String id:ids){
            AppraisalBatchTask e = new AppraisalBatchTask();
            e.setId(id);
            appraisalBatchService.releaseByAppraisalTaskId(e.getId());
            archiveAppraisalTaskService.releaseByAppraisalTaskId(e.getId());
            super.delete(request, e);
            archiveAppraisalMessageService.deleteByTaskId(id);
        }
        return ResultEntity.success(true);
    }

    @RequestMapping({"/updateFinishQuantity"})
    public ResultEntity updateFinishQuantity(String id) {
        service.updateFinishQuantity(id);
        return ResultEntity.success();
    }

    @RequestMapping({"/submitTask"})
    public ResultEntity submitTask(String id) {
        return service.submitTask(id);
    }


}
