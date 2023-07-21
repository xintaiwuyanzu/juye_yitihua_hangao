package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalBatchTask;
import com.dr.archive.appraisal.entity.ArchiveAppraisalMessage;
import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;
import com.dr.framework.common.service.BaseService;

public interface ArchiveAppraisalMessageService extends BaseService<ArchiveAppraisalMessage> {

    void cpoyMessageFromArchiveTask(ArchiveAppraisalTask archiveAppraisalTask, AppraisalBatchTask appraisalBatchTask);

    void deleteByTaskId(String taskId);

    void deleteByBatchId(String batchId);


}
