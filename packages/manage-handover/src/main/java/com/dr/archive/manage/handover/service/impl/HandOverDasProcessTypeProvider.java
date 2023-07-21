package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.TaskContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 档案室档案移交审核
 *
 * @author dr
 */
@Service
public class HandOverDasProcessTypeProvider extends AbstractArchiveHandOverProcessProvider {


    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String businessId = (String) context.getBusinessParams().get("businessId");
        Assert.isTrue(StringUtils.hasText(businessId), "移交记录不能为空！");
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, businessId);

        ArchiveBatchHandOver handOver = handOverService.selectById(businessId);
        //流程启动前更新批次状态
        handOver.setStatus(ArchiveBatchHandOverService.STATUS_AUDIT_DAS);
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
        handOver.setStatus(ArchiveBatchHandOverService.STATUS_RECEIVE_DAG);
        handOverService.updateById(handOver);
    }

    @Override
    public String getType() {
        return "handOver_das";
    }

    @Override
    public String getName() {
        return "档案室档案移交审核";
    }
}
