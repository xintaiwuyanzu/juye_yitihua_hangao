package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.*;
import com.dr.archive.appraisal.service.*;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.service.ResourceManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;


@Service
public class ArchiveAppraisalTaskServiceImpl extends DefaultBaseService<ArchiveAppraisalTask> implements ArchiveAppraisalTaskService {

    @Autowired
    ArchiveAppraisalMessageService archiveAppraisalMessageService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;
    @Autowired
    AppraisalBatchTaskService appraisalBatchTaskService;
    @Autowired
    ArchiveMetadataRecordService archiveMetadataRecordService;
    @Autowired
    AppraisalOpenRangeService appraisalOpenRangeService;
    @Autowired
    ResourceManager resourceManager;

    @Override
    public ArchiveAppraisalTask getAppraisalOne(String formDefinitionId, String formDataId, String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class);
        sqlQuery.equal(ArchiveAppraisalTaskInfo.FORMDEFINITIONID, formDefinitionId)
                .equal(ArchiveAppraisalTaskInfo.FORMDATAID, formDataId)
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId);
        return selectOne(sqlQuery);
    }

    @Override
    public void resetIsSee(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class);
        sqlQuery.equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .set(ArchiveAppraisalTaskInfo.ISSEEHISTORY, "0")
                .set(ArchiveAppraisalTaskInfo.STATUS, "0");
        updateBySqlQuery(sqlQuery);
    }

    @Override
    public void addApprasisalRecord(String taskId) {
        AppraisalBatchTask appraisalBatchTask = appraisalBatchTaskService.selectById(taskId);
        List<ArchiveAppraisalTask> archiveAppraisalTaskList = getAppraisal(taskId);
        for (ArchiveAppraisalTask archiveAppraisalTask : archiveAppraisalTaskList) {
            archiveAppraisalMessageService.cpoyMessageFromArchiveTask(archiveAppraisalTask, appraisalBatchTask);
        }
    }

    @Override
    public void finishApprasisalRecord(String taskId) {


    }

    @Override
    public List<ArchiveAppraisalTask> getAppraisal(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class);
        sqlQuery.equal(ArchiveAppraisalTaskInfo.TASKID, taskId);
        return selectList(sqlQuery);
    }

    @Override
    public long updateById(ArchiveAppraisalTask archiveAppraisalTask) {
        if (archiveAppraisalTask.getPersonResult().equals(archiveAppraisalTask.getAuxiliaryResult())) {
            archiveAppraisalTask.setIsEqual("1");
        } else {
            archiveAppraisalTask.setIsEqual("0");
        }
        if ("0".equals(selectById(archiveAppraisalTask.getId()).getStatus())) {
            appraisalBatchTaskService.updateFinishQuantity(archiveAppraisalTask.getTaskId());
        }
        return getCommonService().update(archiveAppraisalTask);
    }

    @Override
    public Long checkSubmit(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.STATUS, "0")
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId);
        return count(sqlQuery);
    }

    @Override
    public void updateSeeHistory(String formDefinitionId, String formDataId, String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .equal(ArchiveAppraisalTaskInfo.FORMDATAID, formDataId)
                .equal(ArchiveAppraisalTaskInfo.FORMDEFINITIONID, formDefinitionId)
                .set(ArchiveAppraisalTaskInfo.ISSEEHISTORY, "1");
        updateBySqlQuery(sqlQuery);
    }

    @Override
    public Long countAllByBatchId(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .isNotNull(ArchiveAppraisalTaskInfo.TASKID)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId);
        return commonMapper.countByQuery(sqlQuery);
    }

    @Override
    public Long countFinishByBatchId(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.STATUS, "2")
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId);
        return commonMapper.countByQuery(sqlQuery);
    }

    @Override
    public void releaseByAppraisalTaskId(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .set(ArchiveAppraisalTaskInfo.TASKID, (Serializable) null);
        delete(sqlQuery);
    }

    @Override
    public void deleteBatchTaskArchiveByBatchId(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId);
        delete(sqlQuery);
    }

    @Override
    public void endApprasisalTask(String taskId) {
        AppraisalBatchTask appraisalBatchTask = appraisalBatchTaskService.selectById(taskId);
        List<ArchiveAppraisalTask> archiveAppraisalTaskList = getAppraisal(taskId);
        for (ArchiveAppraisalTask archiveAppraisalTask : archiveAppraisalTaskList) {
            archiveAppraisalTask.setStatus("2");
            if (Constants.ROLE_DQJD.equals(archiveAppraisalTask.getAppraisalType())) {
                archiveAppraisalTask.setIsDel("0");
            }
            getCommonService().update(archiveAppraisalTask);
            archiveAppraisalMessageService.cpoyMessageFromArchiveTask(archiveAppraisalTask, appraisalBatchTask);
            FormData oldF = formDataService.selectOneFormData(archiveAppraisalTask.getFormDefinitionId(), archiveAppraisalTask.getFormDataId());
            FormData newF = (FormData) oldF.clone();
            if (Constants.ROLE_KFJD.equals(archiveAppraisalTask.getAppraisalType())) {
                AppraisalOpenRange appraisalOpenRange = appraisalOpenRangeService.selectById(archiveAppraisalTask.getPersonResult());
                newF.put(ArchiveEntity.COLUMN_OPEN_SCOPE, ("kz".equals(appraisalOpenRange.getAuxiliaryResult()) ? "控制" : "开放") + "--" + appraisalOpenRange.getOpenRange());
                if (!StringUtils.isEmpty(archiveAppraisalTask.getPersonAppraisalKeyWord()) &&
                        !archiveAppraisalTask.getPersonResult().equals(archiveAppraisalTask.getAuxiliaryResult())) {
                    AppraisalRecommendKeyWord appraisalRecommendKeyWord = new AppraisalRecommendKeyWord();
                    appraisalRecommendKeyWord.setCreatePersonName(appraisalBatchTask.getCurrentPersonName());
                    appraisalRecommendKeyWord.setKeyWord(archiveAppraisalTask.getPersonAppraisalKeyWord());
                    appraisalRecommendKeyWord.setAuxiliaryResult(appraisalOpenRange.getAuxiliaryResult());
                    appraisalRecommendKeyWord.setBatchId(appraisalBatchTask.getBatchId());
                    appraisalRecommendKeyWord.setStatus("0");
                    appraisalRecommendKeyWord.setOpenRange(archiveAppraisalTask.getPersonResult());
                    appraisalRecommendKeyWord.setFormDataId(archiveAppraisalTask.getFormDataId());
                    appraisalRecommendKeyWord.setFormDefinitionId(archiveAppraisalTask.getFormDefinitionId());
                    appraisalRecommendKeyWord.setSourceType(AppraisalRecommendKeyWordService.SOURCE_TYPE_APPRAISAL);
                    getCommonService().insert(appraisalRecommendKeyWord);
                }
            }
            Archive4ToBeAppraisal archive4ToBeAppraisal = null;
            if ("yq".equals(archiveAppraisalTask.getPersonResult())) {
                archive4ToBeAppraisal = archive4ToBeAppraisalService.getOneByForm(archiveAppraisalTask.getFormDataId(), archiveAppraisalTask.getFormDefinitionId());
                switch (newF.getString(ArchiveEntity.COLUMN_SAVE_TERM)) {
                    case "D30":
                    case "C":
                    case "长期":
                        archive4ToBeAppraisal.setExpirationTime((Integer.parseInt(archive4ToBeAppraisal.getVintages()) + 30 + Integer.parseInt(archiveAppraisalTask.getDelayYear())) + "");
                        break;
                    case "D10":
                    case "D":
                    case "短期":
                        archive4ToBeAppraisal.setExpirationTime((Integer.parseInt(archive4ToBeAppraisal.getVintages()) + 10 + Integer.parseInt(archiveAppraisalTask.getDelayYear())) + "");
                        break;
                    default:
                        break;
                }

            }
            ArchiveMetadataRecord archiveMetadataRecord = new ArchiveMetadataRecord(
                    archiveAppraisalTask.getFormDefinitionId(),
                    archiveAppraisalTask.getFormDataId(),
                    archiveAppraisalTask.getFondCode(),
                    archiveAppraisalTask.getCategoryCode(),
                    ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_JIANDING_KAIFANG,
                    "开放鉴定",
                    "开放鉴定",
                    "开放鉴定",
                    ArchiveEntity.COLUMN_OPEN_SCOPE,
                    "开放范围",
                    oldF.getString(ArchiveEntity.COLUMN_OPEN_SCOPE),
                    newF.getString(ArchiveEntity.COLUMN_OPEN_SCOPE),
                    null
            );
            archiveMetadataRecord.setChangePersonName(appraisalBatchTask.getCurrentPersonName());
            if (Constants.ROLE_DQJD.equals(archiveAppraisalTask.getAppraisalType())) {
                archiveMetadataRecord.setXWMS("到期鉴定");
                archiveMetadataRecord.setXWYJ("到期鉴定");
                archiveMetadataRecord.setYWZT("到期鉴定");
                archiveMetadataRecord.setYWXW(ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_JIANDING_DAOQI);
                archiveMetadataRecord.setAlterCode(null);
                archiveMetadataRecord.setAlterName("到期时间");
                archiveMetadataRecord.setOldValue(null);
            }
            if ("xh".equals(archiveAppraisalTask.getPersonResult())) {
                archiveMetadataRecord.setNewValue("销毁");
            } else if ("yq".equals(archiveAppraisalTask.getPersonResult())) {
                archiveMetadataRecord.setNewValue("延期[" + archiveAppraisalTask.getDelayYear() + "]年");
            }

            archiveMetadataRecordService.insert(archiveMetadataRecord);
            formDataService.updateFormDataById(newF);
        }
    }

    @Override
    public long fastAppraisal(String taskId, String auxiliaryResult, String delayYear) {
        return fastAppraisalByIds(taskId, auxiliaryResult, delayYear, null);
    }

    @Override
    public long fastAppraisalByIds(String taskId, String auxiliaryResult, String delayYear, String ids) {
        return fastAppraisalByCondition(taskId, auxiliaryResult, delayYear, ids, new ArchiveAppraisalTask());
    }

    @Override
    public long fastAppraisalBySearch(String taskId, String auxiliaryResult, String delayYear, ArchiveAppraisalTask archiveAppraisalTask) {
        return fastAppraisalByCondition(taskId, auxiliaryResult, delayYear, null, new ArchiveAppraisalTask());
    }

    public long fastAppraisalByCondition(String taskId, String auxiliaryResult, String delayYear, String ids, ArchiveAppraisalTask archiveAppraisalTask) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .equal(ArchiveAppraisalTaskInfo.STATUS, 0)
                // .in(ArchiveAppraisalTaskInfo.ID, ids) ids为空时还是会加这个条件导致查出为空
                .notEqual(ArchiveAppraisalTaskInfo.SIGN, "1")
                .equal(ArchiveAppraisalTaskInfo.VINTAGES, archiveAppraisalTask.getVintages())
                .equal(ArchiveAppraisalTaskInfo.SIGN, archiveAppraisalTask.getSign())
                .like(ArchiveAppraisalTaskInfo.ARCHIVECODE, archiveAppraisalTask.getArchiveCode())
                .set(ArchiveAppraisalTaskInfo.STATUS, 1);
        if ("machine".equals(auxiliaryResult)) {
            sqlQuery.notEqual(ArchiveAppraisalTaskInfo.AUXILIARYRESULT, "null");
            sqlQuery.set(ArchiveAppraisalTaskInfo.PERSONRESULT, ArchiveAppraisalTaskInfo.AUXILIARYRESULT);
        } else if ("person".equals(auxiliaryResult)) { //审核过程中一键审核选 以上次鉴定人员确定结果为准 时

        } else if ("yq".equals(auxiliaryResult)) {
            sqlQuery.set(ArchiveAppraisalTaskInfo.PERSONRESULT, auxiliaryResult);
            sqlQuery.set(ArchiveAppraisalTaskInfo.DELAYYEAR, delayYear);
        } else {
            sqlQuery.set(ArchiveAppraisalTaskInfo.PERSONRESULT, auxiliaryResult);
        }
        if (!StringUtils.isEmpty(ids)) {
            String[] idArr = ids.split(",");
            sqlQuery.in(ArchiveAppraisalTaskInfo.ID, idArr);
        }
        long u = commonMapper.updateByQuery(sqlQuery);
        updateTaskFinishQuantity(taskId);
        return u;
    }

    @Override
    public List<ArchiveAppraisalTask> getXhArchiveByBatchId(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId)
                .equal(ArchiveAppraisalTaskInfo.PERSONRESULT, "xh")
                .equal(ArchiveAppraisalTaskInfo.STATUS, "2");
        return commonMapper.selectByQuery(sqlQuery);
    }

    public void updateTaskFinishQuantity(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .equal(ArchiveAppraisalTaskInfo.STATUS, 1);
        long f = commonMapper.countByQuery(sqlQuery);
        AppraisalBatchTask appraisalBatchTask = appraisalBatchTaskService.selectById(taskId);
        appraisalBatchTask.setFinishQuantity(f + "");
        commonMapper.updateById(appraisalBatchTask);
    }

    public void updateIsEqual(String taskId, String auxiliaryResult) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .set(ArchiveAppraisalTaskInfo.ISEQUAL, 0)
                .notEqual(ArchiveAppraisalTaskInfo.AUXILIARYRESULT, auxiliaryResult);
        commonMapper.updateByQuery(sqlQuery);

        sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .set(ArchiveAppraisalTaskInfo.ISEQUAL, 1)
                .equal(ArchiveAppraisalTaskInfo.AUXILIARYRESULT, auxiliaryResult);
        commonMapper.updateByQuery(sqlQuery);
    }

    @Override
    public void setIsSign(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class);
        sqlQuery.equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .set(ArchiveAppraisalTaskInfo.SIGN, ArchiveAppraisalTaskInfo.NEXTSIGN)
                .set(ArchiveAppraisalTaskInfo.NEXTSIGN, (Serializable) null);
        updateBySqlQuery(sqlQuery);
    }
}
