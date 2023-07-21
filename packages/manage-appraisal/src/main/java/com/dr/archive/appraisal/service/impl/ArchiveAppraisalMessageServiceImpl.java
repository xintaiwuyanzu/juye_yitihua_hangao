package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalBatchTask;
import com.dr.archive.appraisal.entity.ArchiveAppraisalMessage;
import com.dr.archive.appraisal.entity.ArchiveAppraisalMessageInfo;
import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;
import com.dr.archive.appraisal.service.ArchiveAppraisalMessageService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

@Service
public class ArchiveAppraisalMessageServiceImpl extends DefaultBaseService<ArchiveAppraisalMessage> implements ArchiveAppraisalMessageService {

    @Override
    public void cpoyMessageFromArchiveTask(ArchiveAppraisalTask archiveAppraisalTask, AppraisalBatchTask appraisalBatchTask) {

        ArchiveAppraisalMessage archiveAppraisalMessage = new ArchiveAppraisalMessage();
        archiveAppraisalMessage.setAppraisalType(archiveAppraisalTask.getAppraisalType());
        archiveAppraisalMessage.setBatchId(archiveAppraisalTask.getBatchId());
        archiveAppraisalMessage.setTaskId(archiveAppraisalTask.getTaskId());
        archiveAppraisalMessage.setFormDataId(archiveAppraisalTask.getFormDataId());
        archiveAppraisalMessage.setIsEqual(archiveAppraisalTask.getIsEqual());
        archiveAppraisalMessage.setFormDefinitionId(archiveAppraisalTask.getFormDefinitionId());
        archiveAppraisalMessage.setRemarks(archiveAppraisalTask.getRemarks());
        archiveAppraisalMessage.setPersonResult(archiveAppraisalTask.getPersonResult());
        archiveAppraisalMessage.setPersonAppraisalKeyWord(archiveAppraisalTask.getPersonAppraisalKeyWord());
        archiveAppraisalMessage.setPersonAppraisalBasis(archiveAppraisalTask.getPersonAppraisalBasis());
        archiveAppraisalMessage.setCreatePerson(appraisalBatchTask.getCurrentPerson());
        archiveAppraisalMessage.setAppraisalPersonName(appraisalBatchTask.getCurrentPersonName());
        archiveAppraisalMessage.setCreateDate(archiveAppraisalTask.getUpdateDate());
        insert(archiveAppraisalMessage);

    }

    @Override
    public void deleteByTaskId(String taskId) {
        commonMapper.deleteByQuery(
                SqlQuery.from(ArchiveAppraisalMessage.class)
                        .equal(ArchiveAppraisalMessageInfo.TASKID,taskId)
        );
    }

    @Override
    public void deleteByBatchId(String batchId) {
        commonMapper.deleteByQuery(
                SqlQuery.from(ArchiveAppraisalMessage.class)
                        .equal(ArchiveAppraisalMessageInfo.BATCHID,batchId)
        );
    }
}
