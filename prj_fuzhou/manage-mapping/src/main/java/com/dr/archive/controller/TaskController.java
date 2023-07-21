package com.dr.archive.controller;

import com.dr.archive.entity.task.Task;
import com.dr.archive.entity.task.TaskInfo;
import com.dr.archive.service.TaskService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-06-17 15:27
 **/
@RestController
@RequestMapping("/api/mapping_task")
public class TaskController extends BaseServiceController<TaskService, Task> {

    @Override
    protected SqlQuery<Task> buildPageQuery(HttpServletRequest httpServletRequest, Task task) {
        return SqlQuery.from(Task.class);
    }

    @RequestMapping("/getTaskById")
    ResultEntity getTaskById(String formId, String type) {
        List<Task> tasks = service.selectList(SqlQuery.from(Task.class)
                .equal(TaskInfo.DATARESOURCEID, formId)
                .equal(TaskInfo.TASKTYPE, type.equals("0") ? "dataMark" : "relationMark"));
        return ResultEntity.success(tasks);
    }

    @RequestMapping("/updateIsOpen")
    ResultEntity updateIsOpen(String id, int isOpen) {
        Task task = service.selectById(id);
        task.setIsOpen(isOpen);
        service.updateById(task);
        return ResultEntity.success();
    }
}
