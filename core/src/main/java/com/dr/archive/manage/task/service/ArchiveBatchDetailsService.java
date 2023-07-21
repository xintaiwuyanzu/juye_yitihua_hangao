package com.dr.archive.manage.task.service;

import com.dr.archive.manage.task.entity.BatchDetails;

import java.util.List;
import java.util.Map;

public interface ArchiveBatchDetailsService {
    List<Map> getBatchId(String taskId);

    Map getSourceValue(String appraisalid);

    Map getBatchDetailsCount(String stakId, String batchId);

    void updateBatchDetails(String taskId, String status, String batchId, String appraisalid,String advice,String targetValue);

    List<BatchDetails> getloadPending(String taskId, String batchId);

    Map getAppraisalByid(String id);

    String getAppraisalStatusByid(String id,String taskId);

    void updateDetailsStatus(String batchId,String taskId,String status);

    void regect(String batchId, String taskId, String status);

    Map getBatchDetailsFlag(String taskId, String batchId);

    Map getTaskStatus(String taskId);
}
