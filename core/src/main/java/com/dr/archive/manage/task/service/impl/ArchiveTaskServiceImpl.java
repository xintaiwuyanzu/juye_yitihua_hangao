package com.dr.archive.manage.task.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.ArchiveBatchService;
import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.manage.task.entity.ArchiveTaskInfo;
import com.dr.archive.manage.task.service.ArchiveTaskService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author dr
 */
@Service
public class ArchiveTaskServiceImpl extends DefaultBaseService<ArchiveTask> implements ArchiveTaskService {
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    ArchiveBatchService archiveBatchService;

    @Autowired
    CommonMapper commonMapper;

    /**
     * @param person
     * @param type
     * @param query
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArchiveTask startTask(Person person, String type, BatchCreateQuery query) {
        //创建批次信息
        AbstractArchiveBatch archiveBatch = archiveBatchService.newBatch(query, person);
        //创建待办任务
        return createTask(person.getId(), query.getTargetPersonId(), archiveBatch.getBatchName(), type, archiveBatch.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArchiveTask sendTask(String taskId, String targetPersonId, String success) {
        ArchiveTask oldTask = selectById(taskId);
        Assert.notNull(oldTask, "未查询到指定的待办任务!");
        Assert.isTrue(!StringUtils.isEmpty(targetPersonId), "任务接收人不能为空！");
        //办结时间
        oldTask.setTargetEndDate(System.currentTimeMillis());
        //办结状态
        oldTask.setStatus(StatusEntity.STATUS_ENABLE_STR);
        if (!StringUtils.isEmpty(success)) {
            oldTask.setSuccess(success);
        }
        updateById(oldTask);

        ArchiveTask newTask = new ArchiveTask();

        newTask.setTaskName(oldTask.getTaskName());
        newTask.setBatchId(oldTask.getBatchId());

        newTask.setTaskType(oldTask.getTaskType());
        newTask.setSourcePersonId(oldTask.getTargetPersonId());
        newTask.setSourcePersonName(oldTask.getTargetPersonName());
        newTask.setSourceDate(System.currentTimeMillis());

        Person person = organisePersonService.getPersonById(targetPersonId);

        newTask.setTargetPersonId(targetPersonId);
        newTask.setTargetPersonName(person.getUserName());
        if (!StringUtils.isEmpty(oldTask.getType())) {
            if (oldTask.getType().equals(StatusEntity.STATUS_ENABLE_STR)) {
                newTask.setType(StatusEntity.STATUS_DISABLE_STR);
            } else {
                newTask.setType(StatusEntity.STATUS_ENABLE_STR);
            }
        } else {
            newTask.setType(StatusEntity.STATUS_ENABLE_STR);
        }

        newTask.setStatus(StatusEntity.STATUS_DISABLE_STR);
        newTask.setPreTaskId(oldTask.getId());
        insert(newTask);

        return newTask;
    }

    @Override
    public ArchiveTask endTask(String taskId) {
        ArchiveTask task = selectById(taskId);
        Assert.notNull(task, "未查询到指定的待办任务!");
        task.setStatus(StatusEntity.STATUS_ENABLE_STR);
        task.setTargetEndDate(System.currentTimeMillis());
        updateById(task);

        return task;
    }

    @Override
    public ArchiveTask getTaskStatus(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveTask.class).equal(ArchiveTaskInfo.ID, taskId);
        return (ArchiveTask) commonMapper.selectOneByQuery(sqlQuery);
    }

    @Override
    public long updateById(ArchiveTask entity) {
        return commonMapper.updateIgnoreNullById(entity);
    }

    protected ArchiveTask createTask(String createPersonId, String targetPersonId, String taskName, String taskType, String batchId) {
        Assert.isTrue(!StringUtils.isEmpty(createPersonId), "任务创建人不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(targetPersonId), "任务接收人不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(taskName), "任务名称不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(taskType), "任务类型不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(batchId), "任务批次不能为空！");

        Person createPerson = organisePersonService.getPersonById(createPersonId);
        Person targetPerson = organisePersonService.getPersonById(targetPersonId);
        ArchiveTask archiveTask = new ArchiveTask();
        archiveTask.setBatchId(batchId);

        archiveTask.setTaskName(taskName);
        archiveTask.setTaskType(taskType);
        archiveTask.setType(StatusEntity.STATUS_DISABLE_STR);
        archiveTask.setSourcePersonId(createPerson.getId());
        archiveTask.setSourcePersonName(createPerson.getUserName());
        archiveTask.setSourceDate(System.currentTimeMillis());

        archiveTask.setTargetPersonId(targetPerson.getId());
        archiveTask.setTargetPersonName(targetPerson.getUserName());

        archiveTask.setStatus(StatusEntity.STATUS_DISABLE_STR);
        super.insert(archiveTask);
        return archiveTask;
    }
}
