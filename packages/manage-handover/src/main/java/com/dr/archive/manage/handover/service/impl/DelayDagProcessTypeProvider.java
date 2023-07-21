package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverLibService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.TaskContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 档案室档案延期审核
 *
 * @author dr
 */
@Service
public class DelayDagProcessTypeProvider extends AbstractArchiveHandOverProcessProvider {

    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String businessId = (String) context.getBusinessParams().get("businessId");
        Assert.isTrue(StringUtils.hasText(businessId), "延期记录不能为空！");
        ArchiveBatchHandOver handOver = handOverService.selectById(businessId);

        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, businessId);
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
        Assert.isTrue(StringUtils.hasText(businessId), "未找到指定的延期记录");

        ArchiveBatchHandOver handOver = handOverService.selectById(businessId);
        //TODO 判断办理意见 更新办结状态
        handOver.setStatus(ArchiveBatchHandOverService.STATUS_DONE);
        handOverService.updateById(handOver);
        //更新移交状态
        libService.updateStatus(handOver.getLibIds(), ArchiveBatchHandOverLibService.STATUS_DELAY_DONE);
    }

    @Override
    public String getType() {
        return "delay_dag";
    }

    @Override
    public String getName() {
        return "档案馆档案延期审核";
    }

}
