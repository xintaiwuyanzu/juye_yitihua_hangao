package com.dr.archive.manage.task.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractArchiveBatchInfo;
import com.dr.archive.manage.task.entity.*;
import com.dr.archive.manage.task.service.ArchiveBatchDetailsService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArchiveBatchDetailsServiceImpl implements ArchiveBatchDetailsService {
    @Autowired
    CommonMapper commonMapper;

    @Override
    public List<Map> getBatchId(String taskId) {

        return commonMapper.selectByQuery(SqlQuery.from(ArchiveTask.class, false).column(ArchiveTaskInfo.ID.alias("taskid"))
                .column(AbstractArchiveBatchInfo.ID.alias("batchid"))
                .column(AppraisalBatchDetailInfo.ID.alias("appraisalid"))
                .leftOuterJoin(ArchiveTaskInfo.BATCHID, AbstractArchiveBatchInfo.ID)
                .leftOuterJoin(ArchiveTaskInfo.BATCHID, AppraisalBatchDetailInfo.BATCHID)
                .equal(ArchiveTaskInfo.ID, taskId).setReturnClass(Map.class));
    }

    @Override
    public Map getSourceValue(String appraisalid) {

        return commonMapper.selectOneByQuery(SqlQuery.from(AppraisalBatchDetail.class, false)
                .column(AppraisalBatchDetailInfo.SOURCECODE.alias("sourceCode"))
                .column(AppraisalBatchDetailInfo.SOURCENAME.alias("sourceName"))
                .column(AppraisalBatchDetailInfo.SOURCEVALUE.alias("sourceValue"))
                .column(AppraisalBatchDetailInfo.TARGETVALUE.alias("targetValue"))
                .equal(AppraisalBatchDetailInfo.ID, appraisalid).setReturnClass(Map.class));
    }

    @Override
    public Map getBatchDetailsCount(String stakId, String batchId) {

        //成功
        int success = (int) commonMapper.countByQuery(SqlQuery.from(BatchDetails.class, false).count(BatchDetailsInfo.ID).equal(BatchDetailsInfo.TASKID, stakId)
                .equal(BatchDetailsInfo.BATCHID, batchId).equal(BatchDetailsInfo.STATUS, "1"));

        //失败
        int fail = (int) commonMapper.countByQuery(SqlQuery.from(BatchDetails.class, false).count(BatchDetailsInfo.ID).equal(BatchDetailsInfo.TASKID, stakId)
                .equal(BatchDetailsInfo.BATCHID, batchId).equal(BatchDetailsInfo.STATUS, "-1"));


        int undo = (int) commonMapper.countByQuery(SqlQuery.from(BatchDetails.class, false).count(BatchDetailsInfo.ID).equal(BatchDetailsInfo.TASKID, stakId)
                .equal(BatchDetailsInfo.BATCHID, batchId).equal(BatchDetailsInfo.STATUS, "0"));


        BatchDetails batchDetails = commonMapper.selectOneByQuery(SqlQuery.from(BatchDetails.class, false).column(BatchDetailsInfo.FLAG).equal(BatchDetailsInfo.TASKID, stakId)
                .equal(BatchDetailsInfo.BATCHID, batchId).groupBy(BatchDetailsInfo.FLAG));

        ArchiveTask archiveTask = commonMapper.selectOneByQuery(SqlQuery.from(ArchiveTask.class).column(ArchiveTaskInfo.STATUS).equal(ArchiveTaskInfo.ID, stakId));


        Map map = new HashMap();
        map.put("success", success);
        map.put("fail", fail);
        map.put("undo", undo);
        map.put("total", success + fail + undo);
        map.put("status", archiveTask.getStatus());
        if (!StringUtils.isEmpty(batchDetails)) {
            map.put("flag", batchDetails.getFlag());
        }
        return map;
    }

    @Override
    public void updateBatchDetails(String taskId, String status, String batchId, String appraisalid, String advice, String targetValue) {

        BatchDetails batchDetails = commonMapper.selectOneByQuery(SqlQuery.from(BatchDetails.class).equal(BatchDetailsInfo.BATCHID, batchId).equal(BatchDetailsInfo.TASKID, taskId)
                .equal(BatchDetailsInfo.APPRAISALID, appraisalid));

        if (!StringUtils.isEmpty(advice)) {
            batchDetails.setAdvice(advice);
        }
        if (!StringUtils.isEmpty(targetValue)) {
            batchDetails.setTargetValue(targetValue);
        }

        batchDetails.setStatus(status);
        commonMapper.updateById(batchDetails);

    }

    @Override
    public List<BatchDetails> getloadPending(String taskId, String batchId) {
        return commonMapper.selectByQuery(SqlQuery.from(BatchDetails.class, false).column(BatchDetailsInfo.APPRAISALID)
                .equal(BatchDetailsInfo.BATCHID, batchId)
                .equal(BatchDetailsInfo.TASKID, taskId).orderBy(BatchDetailsInfo.APPRAISALID));
    }

    @Override
    public Map getAppraisalByid(String id) {
        return commonMapper.selectOneByQuery(SqlQuery.from(AppraisalBatchDetail.class).equal(AppraisalBatchDetailInfo.ID, id)
                .setReturnClass(Map.class));
    }

    @Override
    public String getAppraisalStatusByid(String id, String taskId) {
        //SELECT status_info FROM archive_batch_details where taskid='${taskId}' and appraisalid='${id}'

        return commonMapper.selectOneByQuery(SqlQuery.from(BatchDetails.class, false).column(BatchDetailsInfo.STATUS)
                .equal(BatchDetailsInfo.TASKID, taskId)
                .equal(BatchDetailsInfo.APPRAISALID, id).setReturnClass(String.class));
    }

    @Override
    public void updateDetailsStatus(String batchId, String taskId, String status) {
        //update archive_batch_details set status_info = '${status}' where batchid='${batchId}' and taskid='${taskId}'
        commonMapper.updateByQuery(SqlQuery.from(BatchDetails.class).set(BatchDetailsInfo.STATUS, status)
                .equal(BatchDetailsInfo.BATCHID, batchId)
                .equal(BatchDetailsInfo.TASKID, taskId));

    }

    @Override
    public void regect(String batchId, String taskId, String status) {
        commonMapper.updateByQuery(SqlQuery.from(BatchDetails.class).set(BatchDetailsInfo.STATUS, status)
                .equal(BatchDetailsInfo.BATCHID, batchId)
                .equal(BatchDetailsInfo.TASKID, taskId));

        commonMapper.updateByQuery(SqlQuery.from(AppraisalBatchDetail.class).set(AppraisalBatchDetailInfo.STATUS, status)
                .equal(AppraisalBatchDetailInfo.BATCHID, batchId));


        commonMapper.updateByQuery(SqlQuery.from(AbstractArchiveBatch.class).set(AbstractArchiveBatchInfo.STATUS, "1").equal(AbstractArchiveBatchInfo.ID, batchId));

        commonMapper.updateByQuery(SqlQuery.from(ArchiveTask.class).set(ArchiveTaskInfo.STATUS, "1").equal(ArchiveTaskInfo.ID, taskId));

    }

    @Override
    public Map getBatchDetailsFlag(String taskId, String batchId) {

        return commonMapper.selectOneByQuery(SqlQuery.from(BatchDetails.class, false).column(BatchDetailsInfo.FLAG)
                .equal(BatchDetailsInfo.BATCHID, batchId)
                .equal(BatchDetailsInfo.TASKID, taskId)
                .groupBy(BatchDetailsInfo.FLAG).setReturnClass(Map.class));
    }

    @Override
    public Map getTaskStatus(String taskId) {
        return commonMapper.selectOneByQuery(SqlQuery.from(ArchiveTask.class, false).column(ArchiveTaskInfo.STATUS.alias("status"))
                .equal(ArchiveTaskInfo.ID, taskId).setReturnClass(Map.class));
    }
}
