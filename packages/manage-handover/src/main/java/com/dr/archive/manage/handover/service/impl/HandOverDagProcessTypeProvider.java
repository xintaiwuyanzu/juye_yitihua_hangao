package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.common.dzdacqbc.entity.EArchiveInfo;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverDetail;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverDetailInfo;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverDetailService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverLibService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 档案馆档案移交审核
 *
 * @author dr
 */
@Service
public class HandOverDagProcessTypeProvider extends AbstractArchiveHandOverProcessProvider {

    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String businessId = (String) context.getBusinessParams().get("businessId");
        Assert.isTrue(StringUtils.hasText(businessId), "移交记录不能为空！");
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, businessId);

        ArchiveBatchHandOver handOver = handOverService.selectById(businessId);
        //流程启动前更新批次状态
        handOver.setStatus(ArchiveBatchHandOverService.STATUS_AUDIT_DAG);
        handOverService.updateById(handOver);
        context.setBusinessId(businessId);
        context.setProcessInstanceTitle(handOver.getBatchName());
    }

    /**
     * 办结流程
     *
     * @param context
     */
    @Override
    public void onBeforeEndProcess(TaskContext context) {
        //更新办结状态
        String businessId = (String) context.getTaskInstance().getProcessVariables().get(ProcessConstants.PROCESS_BUSINESS_KEY);
        Assert.isTrue(StringUtils.hasText(businessId), "未找到指定的移交记录");

        ArchiveBatchHandOver handOver = handOverService.selectById(businessId);
        //TODO 判断办理意见 更新办结状态
        handOver.setStatus(ArchiveBatchHandOverService.STATUS_DONE);
        handOverService.updateById(handOver);

        libService.updateStatus(handOver.getLibIds(), ArchiveBatchHandOverLibService.STATUS_HAND_DONE);
    }

    @Autowired
    ArchiveBatchHandOverDetailService archiveBatchHandOverDetailService;
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    FondService fondService;
    @Autowired
    EArchiveService eArchiveService;
    /**
     * 用来执行异步操作
     */
    @Autowired
    @Qualifier("camundaTaskExecutor")
    protected Executor executor;

    /**
     * TODO 这里太麻烦了
     *
     * @param context
     */
    @Override
    public void onAfterEndProcess(TaskContext context) {
        String businessId = (String) context.getTaskInstance().getProcessVariables().get(ProcessConstants.PROCESS_BUSINESS_KEY);
        ArchiveBatchHandOver handOver = handOverService.selectById(businessId);
        String[] libIds = handOver.getLibIds().split(",");
        List<ArchiveBatchHandOverDetail> detailList = archiveBatchHandOverDetailService.selectList(SqlQuery.from(ArchiveBatchHandOverDetail.class)
                .in(ArchiveBatchHandOverDetailInfo.BATCHID, libIds));
        SecurityHolder securityHolder = SecurityHolder.get();
        executor.execute(() -> {
            SecurityHolder.set(securityHolder);
            detailList.forEach(archiveBatchHandOverDetail -> {
                FormData formData = dataManager.selectOneFormData(archiveBatchHandOverDetail.getFormDefinitionId(), archiveBatchHandOverDetail.getFormDataId());
                formData.put(ArchiveEntity.COLUMN_ORGANISEID, SecurityHolder.get().currentOrganise().getId()); //加机构id
                formData.put(ArchiveEntity.COLUMN_SUB_STATUS, ArchiveDataManager.SUBSTATUS_UNLOCKED); //档案解锁
                Fond fond = fondService.findFondByCode(archiveBatchHandOverDetail.getFondCode());
                dataManager.updateFormData(formData, fond == null ? "" : fond.getId(), archiveBatchHandOverDetail.getCategoryId());
                //更新长期保存库数据
                EArchive eArchive = eArchiveService.selectOne(SqlQuery.from(EArchive.class).equal(EArchiveInfo.FORMDATAID, formData.getId()));
                if(eArchive != null){
                    eArchive.setORGANISEID(SecurityHolder.get().currentOrganise().getId());
                    eArchiveService.updateById(eArchive);
                }
            });

        });
        super.onAfterEndProcess(context);
    }

    @Override
    public String getType() {
        return "handOver_dag";
    }

    @Override
    public String getName() {
        return "档案馆档案移交审核";
    }
}
