package com.dr.archive.manage.task.service;

import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

/**
 * 档案任务service
 *
 * @author dr
 */
public interface ArchiveTaskService extends BaseService<ArchiveTask> {
    //0表示任务办理中
    //1表示任务办理成功
    //-1表示任务办理失败

    /**
     * 发起指定类型的代办任务
     *
     * @param person
     * @param type
     * @param query
     * @return
     */
    ArchiveTask startTask(Person person, String type, BatchCreateQuery query);

    /**
     * 发送任务
     *
     * @param taskId
     * @param targetPersonId
     * @param success
     * @return
     */
    ArchiveTask sendTask(String taskId, String targetPersonId, String success);

    /**
     * 办结任务
     *
     * @param taskId
     * @return
     */
    ArchiveTask endTask(String taskId);

    ArchiveTask getTaskStatus(String taskId);
}
