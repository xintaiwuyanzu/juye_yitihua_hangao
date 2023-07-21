package com.dr.archive.common.doneTask.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.controller.BaseProcessController;
import com.dr.framework.core.process.query.ProcessInstanceQuery;
import com.dr.framework.core.process.service.ProcessInstanceService;
import com.dr.framework.core.process.service.TaskInstanceService;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/donetask")
public class doneTaskController extends BaseProcessController {
    @Autowired
    ProcessInstanceService processInstanceService;
    @Autowired
    private TaskInstanceService taskInstanceService;

    @RequestMapping({"page"})
    public ResultEntity page(@Current Person person, String title, ProcessInstanceQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        query.createPersonEqual(person.getId());
        query.nameLike(title);
        return page ? ResultEntity.success(this.getProcessInstanceService().processInstancePage(query, pageIndex, pageSize)) : ResultEntity.success(this.getProcessInstanceService().processInstanceList(query));
    }

    @RequestMapping({"historyPage"})
    public ResultEntity historyPage(@Current Person person, ProcessInstanceQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        return page ? ResultEntity.success(this.getProcessInstanceService().processInstanceHistoryPage(query, pageIndex, pageSize)) : ResultEntity.success(this.getProcessInstanceService().processInstanceHistoryList(query));
    }

}
