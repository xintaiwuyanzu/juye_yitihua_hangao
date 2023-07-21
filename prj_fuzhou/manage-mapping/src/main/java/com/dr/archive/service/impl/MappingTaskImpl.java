package com.dr.archive.service.impl;

import com.dr.archive.entity.task.Task;
import com.dr.archive.entity.task.TaskInfo;
import com.dr.archive.service.DataResourceService;
import com.dr.archive.service.TaskService;
import com.dr.framework.common.task.entity.TaskDefinition;
import com.dr.framework.common.task.entity.TaskInstance;
import com.dr.framework.common.task.service.TaskTypeProvider;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: yang
 * @create: 2022-06-17 11:52
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class MappingTaskImpl implements TaskTypeProvider {

    final ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Autowired
    DataResourceService dataResourceService;
    @Autowired
    TaskService taskService;

    @Override
    public String type() {
        return "mapping_task";
    }

    @Override
    public String name() {
        return "图谱定时任务";
    }

    @Override
    public String executeTask(TaskDefinition taskDefinition, TaskInstance taskInstance, Person person, Map<String, String> map) {
        doDetect();
        return "启动成功";
    }

    /**
     * 业务
     */
    private synchronized void doDetect() {
        if (!executorService.isShutdown()) {
            long n = taskService.updateBySqlQuery(SqlQuery.from(Task.class).set(TaskInfo.STATUS, "0"));
            if (n > 0) {
                //查询所有标注任务
                List<Task> tasks = taskService.selectList(SqlQuery.from(Task.class)
                        .equal(TaskInfo.STATUS, "0")
                        .equal(TaskInfo.ISOPEN, 1)
                        .orderBy(TaskInfo.UPDATEDATE));
                for (Task task : tasks) {
                    startDetect(task);
                    executorService.execute(() -> startDetect(task));
                }
            }
        }
    }

    public synchronized void startDetect(Task task) {
        long total = task.getTotalSize();
        int index = task.getPageIndex();
        long size = task.getPageSize();
        //获取参数
        String params = task.getParams();

        task.setStatus("1");
        taskService.updateById(task);

        if (task.getTaskType().equals("dataMark")) {
            while (total >= index * size) {
                //获取参数
                task.setPageIndex(index);
                //标注
                dataResourceService.transform(params, task);
                index++;
            }
        } else {
            while (total >= index * size) {
                task.setPageIndex(index);
                //保存关系
                dataResourceService.saveRelationResult(params, task);
                index++;
            }
        }
    }
}
