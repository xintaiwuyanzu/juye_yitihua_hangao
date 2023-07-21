package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalBatchWordGroup;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface AppraisalBatchWordGroupService extends BaseService<AppraisalBatchWordGroup> {

    List<AppraisalBatchWordGroup> getAppraisalBatchWordGroupByBatchId(String batchId);

    void updatePriority(AppraisalBatchWordGroup appraisalBatchWordGroup);

    String getWordGroupIdsByBatchId(String batchId);

    void updateAppraisalBatchWordGroup(String batchId,String wordGroupIds);


}
