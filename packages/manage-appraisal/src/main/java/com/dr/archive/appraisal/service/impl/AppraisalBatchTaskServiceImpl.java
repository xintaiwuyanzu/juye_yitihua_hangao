package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalBatch;
import com.dr.archive.appraisal.entity.AppraisalBatchTask;
import com.dr.archive.appraisal.entity.AppraisalBatchTaskInfo;
import com.dr.archive.appraisal.service.AppraisalBatchService;
import com.dr.archive.appraisal.service.AppraisalBatchTaskService;
import com.dr.archive.appraisal.service.Archive4ToBeAppraisalService;
import com.dr.archive.appraisal.service.ArchiveAppraisalTaskService;
import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AppraisalBatchTaskServiceImpl extends DefaultBaseService<AppraisalBatchTask> implements AppraisalBatchTaskService, ProcessTypeProvider {
    public static final String DATE_PATTEN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    AppraisalBatchService appraisalBatchService;
    @Autowired
    ArchiveAppraisalTaskService archiveAppraisalTaskService;
    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;

    final ExecutorService executorService = Executors.newFixedThreadPool(10);


    @Override
    public ResultEntity createAppraisalBatchTask(Person person, AppraisalBatchTask appraisalBatchTask) {
        AppraisalBatch appraisalBatch = appraisalBatchService.selectById(appraisalBatchTask.getBatchId());
        String taskId = UUID.randomUUID().toString();
        appraisalBatchTask.setId(taskId);
        appraisalBatchTask.setAppraisalType(appraisalBatch.getAppraisalType());
        long quantity = appraisalBatchService.updateAppraisalBatchByTask(appraisalBatchTask);
        if(quantity==0){
            return ResultEntity.error("无鉴定档案可领取");
        }
        appraisalBatchTask.setBatchTaskQuantity(String.valueOf(quantity));
        appraisalBatchTask.setFinishQuantity("0");
        appraisalBatchTask.setAppraisalType(appraisalBatch.getAppraisalType());
        appraisalBatchTask.setBatchTaskCode(person.getUserCode()+"-"+getXh(appraisalBatchTask.getBatchId(),person));
        appraisalBatchTask.setCreatePerson(person.getId());
        appraisalBatchTask.setCurrentPerson(person.getId());
        appraisalBatchTask.setCreateDate(System.currentTimeMillis());
        appraisalBatchTask.setStatus("0");
        appraisalBatchTask.setCurrentPersonName(person.getUserName());
        appraisalBatchTask.setReceiveTaskName(person.getUserName());
        insert(appraisalBatchTask);
        return ResultEntity.success();
    }

    @Override
    public void deleteBatchTaskByBatchId(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatchTask.class)
                                    .equal(AppraisalBatchTaskInfo.BATCHID,batchId);
        delete(sqlQuery);
    }

    @Override
    public void updateFinishQuantity(String id) {
        AppraisalBatchTask appraisalBatchTask = selectById(id);
        appraisalBatchTask.setFinishQuantity(String.valueOf(Integer.parseInt(appraisalBatchTask.getFinishQuantity())+1));
        updateById(appraisalBatchTask);
    }

    @Override
    public ResultEntity submitTask(String id) {
        AppraisalBatchTask appraisalBatchTask = selectById(id);
        if(appraisalBatchTask.getBatchTaskQuantity().equals(appraisalBatchTask.getFinishQuantity())){
            appraisalBatchTask.setStatus("1");
            updateById(appraisalBatchTask);
            return ResultEntity.success();
        }
        return ResultEntity.error("还有未鉴定的档案数据，禁止提交");
    }

    public String getXh(String appraisalBatchId, Person person){
        List<AppraisalBatchTask> appraisalBatchTaskList = commonMapper.selectByQuery(
                SqlQuery.from(AppraisalBatchTask.class)
                        .equal(AppraisalBatchTaskInfo.BATCHID,appraisalBatchId)
                        .equal(AppraisalBatchTaskInfo.CREATEPERSON,person.getId())
                        .orderByDesc(AppraisalBatchTaskInfo.CREATEDATE));

        if(appraisalBatchTaskList.size()==0){
            return "001";
        }
        String maxAppraisalBatchTaskCode = appraisalBatchTaskList.get(appraisalBatchTaskList.size()-1).getBatchTaskCode();
        maxAppraisalBatchTaskCode = maxAppraisalBatchTaskCode.replace(person.getUserCode()+"-","");
        int index = Integer.parseInt(maxAppraisalBatchTaskCode);
        if((index+1)<10){
            return "00"+(index+1);
        }else if((index+1)<100){
            return "0"+(index+1);
        }
        return (index+1)+"";
    }

    @Override
    public String getType() {
        return Constants.PROCESS_TYPE_APPRAISAL;
    }

    @Override
    public String getName() {
        return "档案鉴定";
    }

    /**
     * 启动流程前回调
     *
     * @param context
     */
    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String apparisalTaskId = (String) context.getBusinessParams().get("businessId");
        AppraisalBatch appraisalBatch = appraisalBatchService.selectById(selectById(apparisalTaskId).getBatchId());
        context.setBusinessId(apparisalTaskId);
        context.setProcessInstanceTitle(appraisalBatch.getBatchName()+"----"+ DateFormatUtils.format(new Date(), DATE_PATTEN) + "发起的" + getName() + "审核");
        //设置流程查看详情跳转页面
        context.addVar(ProcessConstants.PROCESS_FORM_URL_KEY, "/appraisal/appraisalTask/examination/archive");
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, context.getBusinessId());
    }

    /**
     * 启动流程后回调
     *
     * @param context
     */
    @Override
    public void onAfterStartProcess(ProcessContext context) {
        //修改全宗鉴定任务状态为审批中
        AppraisalBatchTask appraisalBatchTask = new AppraisalBatchTask();
        appraisalBatchTask.setId(context.getBusinessId());
        appraisalBatchTask.setFinishQuantity("0");
        appraisalBatchTask.setStatus("1");//0:鉴定中，1:审核中，2:已完成
        commonMapper.updateIgnoreNullById(appraisalBatchTask);
        archiveAppraisalTaskService.resetIsSee(context.getBusinessId());
        SecurityHolder s  = SecurityHolder.get();
        executorService.execute(()->{
            SecurityHolder.set(s);
            archiveAppraisalTaskService.addApprasisalRecord(context.getBusinessId());
        });

    }

    /**
     * 结束流程后回调
     *
     * @param context
     */
    @Override
    public void onAfterEndProcess(TaskContext context) {
        String apparisalTaskId = context.getTaskInstance().getProcessVariables().get("$businessId").toString();
        AppraisalBatchTask appraisalBatchTask = new AppraisalBatchTask();
        appraisalBatchTask.setId(apparisalTaskId);
        appraisalBatchTask.setStatus("2");//0:鉴定中，1:审核中，2:已完成
        commonMapper.updateIgnoreNullById(appraisalBatchTask);
        SecurityHolder s  = SecurityHolder.get();
        executorService.execute(()->{
            SecurityHolder.set(s);
            archiveAppraisalTaskService.endApprasisalTask(context.getTaskInstance().getProcessVariables().get("$businessId").toString());
        });
    }

    @Override
    public void onBeforeCompleteTask(TaskContext context) {
        AppraisalBatchTask appraisalBatchTask = new AppraisalBatchTask();
        appraisalBatchTask.setId(context.getTaskInstance().getProcessVariables().get("$businessId").toString());
        appraisalBatchTask.setFinishQuantity("0");
        appraisalBatchTask.setCurrentPerson(SecurityHolder.get().currentPerson().getId());
        appraisalBatchTask.setCurrentPersonName(SecurityHolder.get().currentPerson().getUserName());
        commonMapper.updateIgnoreNullById(appraisalBatchTask);
    }

    /**
     * 环节执行完回调
     *
     * @param context
     */
    @Override
    public void onAfterCompleteTask(TaskContext context) {
        AppraisalBatchTask appraisalBatchTask = selectById(context.getTaskInstance().getProcessVariables().get("$businessId").toString());
        appraisalBatchTask.setFinishQuantity("0");
        appraisalBatchTask.setCurrentPerson("");
        commonMapper.updateById(appraisalBatchTask);
        archiveAppraisalTaskService.resetIsSee(context.getTaskInstance().getProcessVariables().get("$businessId").toString());
        archiveAppraisalTaskService.setIsSign(context.getTaskInstance().getProcessVariables().get("$businessId").toString());
        SecurityHolder s  = SecurityHolder.get();
        executorService.execute(()->{
            SecurityHolder.set(s);
            archiveAppraisalTaskService.addApprasisalRecord(context.getTaskInstance().getProcessVariables().get("$businessId").toString());
        });
    }

}
