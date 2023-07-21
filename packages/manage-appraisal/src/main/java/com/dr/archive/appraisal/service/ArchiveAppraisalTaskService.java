package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface ArchiveAppraisalTaskService extends BaseService<ArchiveAppraisalTask> {

    ArchiveAppraisalTask getAppraisalOne(String formDefinitionId, String formDataId, String taskId);

    void resetIsSee(String taskId);

    void addApprasisalRecord(String taskId);

    void finishApprasisalRecord(String taskId);

    List<ArchiveAppraisalTask> getAppraisal(String taskId);

    Long checkSubmit(String taskId);

    void updateSeeHistory(String formDefinitionId, String formDataId, String taskId);

    Long countAllByBatchId(String batchId);

    Long countFinishByBatchId(String batchId);

    void releaseByAppraisalTaskId(String taskId);

    void deleteBatchTaskArchiveByBatchId(String batchId);

    void endApprasisalTask(String taskId);

    long fastAppraisal(String taskId, String auxiliaryResult, String delayYear);

    long fastAppraisalByIds(String taskId, String auxiliaryResult, String delayYear, String ids);

    long fastAppraisalBySearch(String taskId, String auxiliaryResult, String delayYear, ArchiveAppraisalTask archiveAppraisalTask);

    List<ArchiveAppraisalTask> getXhArchiveByBatchId(String batchId);

    void setIsSign(String taskId);
}
