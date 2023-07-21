package com.dr.archive.manage.task.controller;

import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.manage.task.entity.ArchiveTaskInfo;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.manage.task.service.ArchiveTaskService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 档案待办任务
 *
 * @author dr
 */
@RestController
@RequestMapping({"${common.api-path:/api}/archiveTask"})
public class ArchiveTaskController extends BaseServiceController<ArchiveTaskService, ArchiveTask> {
    @Autowired
    ArchiveTaskService archiveTaskService;

    @Override
    protected SqlQuery<ArchiveTask> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveTask archiveTask) {
        Person person = getUserLogin(httpServletRequest);

        SqlQuery<ArchiveTask> sqlQuery = SqlQuery.from(ArchiveTask.class);
        String taskType = httpServletRequest.getParameter("taskType");
        //创建人，发送人，或者接收人为查询条件
        sqlQuery.equal(ArchiveTaskInfo.CREATEPERSON, person.getId())
                .or()
                .equal(ArchiveTaskInfo.TARGETPERSONID, person.getId())
                .or()
                .equal(ArchiveTaskInfo.SOURCEPERSONID, person.getId());
        if (!StringUtils.isEmpty(taskType)) {
            sqlQuery.andNew()
                    .equal(ArchiveTaskInfo.TASKTYPE, taskType);
        }
        if (!StringUtils.isEmpty(archiveTask.getStatus())) {
            sqlQuery.equal(ArchiveTaskInfo.STATUS, archiveTask.getStatus());
        }
        if (!StringUtils.isEmpty(archiveTask.getTaskName())) {
            sqlQuery.andNew()
                    .like(ArchiveTaskInfo.TASKNAME, archiveTask.getTaskName());
        }
        sqlQuery.orderByDesc(ArchiveTaskInfo.CREATEDATE);
        return sqlQuery;
    }

    /**
     * 发起任务
     * personId 接收人 ;formDefinitionId 表单定义Id; fondId 全宗Id ;categoryId 门类Id
     *
     * @param type  接收人
     * @param query 表单定义Id
     * @return
     */
    @PostMapping("/startTask")
    public ResultEntity<ArchiveTask> startTask(
            @Current Person person,
            String type,
            @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent,
            BatchCreateQuery query
    ) {
        Assert.isTrue(!StringUtils.isEmpty(type), "发送任务类型不能为空");
        query.parseQuery(queryContent);
        return ResultEntity.success(service.startTask(person, type, query));
    }


    /**
     * 发送任务给下一人
     *
     * @param request
     * @param taskId
     * @param targetPersonId
     * @return
     */
    @RequestMapping("/sendTask")
    public ResultEntity<ArchiveTask> sendTask(HttpServletRequest request, String taskId, String targetPersonId, String success) {
        return ResultEntity.success(service.sendTask(taskId, targetPersonId, success));
    }

    /**
     * 办结任务
     *
     * @param request
     * @param taskId
     * @return
     */
    @RequestMapping("/endTask")
    public ResultEntity<ArchiveTask> endTask(HttpServletRequest request, String taskId) {
        return ResultEntity.success(service.endTask(taskId));
    }

    @RequestMapping("/getTaskStatus")
    public ResultEntity getTaskStatus(String taskId) {
        return ResultEntity.success(archiveTaskService.getTaskStatus(taskId));
    }
}
