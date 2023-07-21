package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalBatchPerson;
import com.dr.framework.common.service.BaseService;


public interface AppraisalBatchPersonService extends BaseService<AppraisalBatchPerson> {

    void deleteBatchPersonByBatchId(String batchId);

}
