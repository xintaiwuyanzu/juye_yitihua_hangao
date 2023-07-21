package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalBatchTask;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;


public interface AppraisalBatchTaskService extends BaseService<AppraisalBatchTask> {

    ResultEntity createAppraisalBatchTask(Person person, AppraisalBatchTask appraisalBatchTask);

    void deleteBatchTaskByBatchId(String batchId);

    void updateFinishQuantity(String id);

    ResultEntity submitTask(String id);


}
