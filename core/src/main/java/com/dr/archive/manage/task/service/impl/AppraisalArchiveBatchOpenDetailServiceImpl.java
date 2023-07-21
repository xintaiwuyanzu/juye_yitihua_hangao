package com.dr.archive.manage.task.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailServiceImpl;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.task.entity.AppraisalBatchDetail;
import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 鉴定申请
 *
 * @author: caor
 * @date: 2020/11/18 1:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppraisalArchiveBatchOpenDetailServiceImpl extends AbstractArchiveBatchDetailServiceImpl<AppraisalBatchDetail> {
    @Override
    public String getType() {
        return BATCH_TYPE_KFJD;
    }

    @Override
    public String getName() {
        return "开放鉴定";
    }
    @Override
    protected void bindBaseInfo(AppraisalBatchDetail detail, FormData data, Fond fond, Category category, BatchCreateQuery query) {
        detail.setSourceCode(query.getSourceCode());
        detail.setSourceName(query.getSourceName());
        detail.setSourceValue(data.get(query.getSourceCode()));
        detail.setTargetValue(query.getTargetValue());
        detail.setAppraisalType(query.getAppraisalType());
        super.bindBaseInfo(detail, data, fond, category, query);
    }

    @Override
    public void doUpdateFormDate(AbstractArchiveBatch archiveBatch, List<AbstractBatchDetailEntity> abstractBatchDetailEntityList) {
        abstractBatchDetailEntityList.forEach(detail -> {
            FormData formData = dataManager.selectOneFormData(detail.getFormDefinitionId(), detail.getFormDataId());
            AppraisalBatchDetail appraisalBatchDetail = (AppraisalBatchDetail) detail;
            formData.put(appraisalBatchDetail.getSourceCode(), appraisalBatchDetail.getTargetValue());
            if ("destruction".equalsIgnoreCase(appraisalBatchDetail.getAppraisalType())) {
                dataManager.deleteFormData(detail.getCategoryId(), formData.getFormDefinitionId(), formData.getId());
            } else {
                dataManager.updateFormData(formData, "", "");
            }
        });
    }

    @Override
    protected AppraisalBatchDetail newBatchDetail(FormData data, AbstractArchiveBatch batch, BatchCreateQuery query) {
//        addArchiveTask(batch);
        return super.newBatchDetail(data, batch, query);
    }

    void addArchiveTask(AbstractArchiveBatch batch) {
        ArchiveTask task = new ArchiveTask();
        task.setBatchId(batch.getId());
        task.setSourcePersonId(SecurityHolder.get().currentPerson().getId());
        task.setSourcePersonName(SecurityHolder.get().currentPerson().getUserName());
        task.setSourceDate(System.currentTimeMillis());
        task.setTaskType(batch.getBatchType());
        task.setTaskName(batch.getBatchName());
        task.setStatus(StatusEntity.STATUS_DISABLE_STR);
        task.setType(StatusEntity.STATUS_DISABLE_STR);
        CommonService.bindCreateInfo(task);
        commonMapper.insert(task);
    }
}
