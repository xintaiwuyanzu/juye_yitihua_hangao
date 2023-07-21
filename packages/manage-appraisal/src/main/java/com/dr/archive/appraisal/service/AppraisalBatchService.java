package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalBatch;
import com.dr.archive.appraisal.entity.AppraisalBatchTask;
import com.dr.archive.appraisal.entity.CommonCount;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface AppraisalBatchService extends BaseService<AppraisalBatch> {

    Page getAppraisalBatchPageByPerson(String personId, int pageIndex, int pageSize, boolean page, AppraisalBatch batch);

    ResultEntity getArchiveCountByBatch(String id);

    List<Map> getCategoryCountByBatch(String id);

    ResultEntity getArchiveCountByAuxiliary(String id);

    Map statisticsByBatchId(String batchId);

    long countByBatchID(String id);

    List<CommonCount> groupCountByBatchIDAndAuxiliary(String id);

    long releaseByAppraisalBatchId(String appraisalBatchId);

    long updateAppraisalBatchByTask(AppraisalBatchTask appraisalBatchTask);

    void releaseByAppraisalTaskId(String taskId);

    List statisticsFinishByBatchId(String batchId,String fondId);

    List<Fond> getFondByBatchId(String batchId);

    void updateAppraisalBatchDestory(String batchId);

    ResultEntity endBatch(String batchId) throws IOException;


}
