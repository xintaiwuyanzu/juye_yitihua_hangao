package com.dr.archive.managefondsdescriptivefile.service.impl;

import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.managefile.service.ManageFileService;
import com.dr.archive.managefondsdescriptivefile.entity.*;
import com.dr.archive.managefondsdescriptivefile.service.DossierTypeService;
import com.dr.archive.managefondsdescriptivefile.service.ManagementHistoryService;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.archive.managefondsdescriptivefile.service.ManagementTypeService;
import com.dr.archive.managefondsdescriptivefile.vo.FondsDescriptiveFileCountVo;
import com.dr.archive.util.Constants;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ManagementServiceImpl extends DefaultBaseService<Management> implements ManagementService, ProcessTypeProvider {
    public static final String DATE_PATTEN = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    DossierTypeService dossierTypeService;
    @Autowired
    ManagementTypeService managementTypeService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    ManagementHistoryService managementHistoryService;
    @Autowired
    FondService fondService;
    @Autowired
    ManageFileService manageFileService;

    @Override
    public long insert(Management entity) {
        Assert.isTrue(!StringUtils.isEmpty(entity.getFondId()) || !StringUtils.isEmpty(entity.getFond_code()), "未选择全宗！");
        Assert.isTrue(!StringUtils.isEmpty(entity.getManagementTypeCode()), "未选择分类！");
        if (!StringUtils.isEmpty(entity.getFondId())) {
            entity.setFond_code(fondService.selectById(entity.getFondId()).getCode());
        }
        if (!StringUtils.isEmpty(entity.getFond_code())) {
            entity.setFondId(fondService.findFondByCode(entity.getFond_code()).getId());
        }
        //查询全宗
        ManagementType managementType = new ManagementType();
        //查询子分类分类
        DossierType dossierType = dossierTypeService.getDossierTypeByCode(entity.getManagementTypeCode());
        if (null == dossierType) {
            //再查询大分类
            managementType = managementTypeService.getManagementTypeByCode(entity.getManagementTypeCode());
            entity.setManagementTypeId(managementType.getId());
            entity.setManagementTypeCode(managementType.getCode());//业务关联code
            entity.setManagementTypeName(managementType.getName());
        } else {
            //设置子分类
            entity.setDossierTypeId(dossierType.getId());
            entity.setDossierTypeName(dossierType.getName());
            //根据业务id查询大分类
            managementType = managementTypeService.selectById(dossierType.getBusinessId());
            entity.setManagementTypeId(dossierType.getId());
            entity.setManagementTypeCode(dossierType.getCode());//业务关联code
            entity.setManagementTypeName(managementType.getName());
        }
        //保管期限
        entity.setRetentionPeriod(dossierType.getSaveTerm());
        //设置当前机构名称
        entity.setAdministrative(SecurityHolder.get().currentOrganise().getOrganiseName());
        //默认状态待提交
        entity.setStatus(Optional.ofNullable(entity.getStatus()).orElse(StatusEntity.STATUS_DISABLE_STR));
        //设置移交状态为0未移交
        entity.setTransferStatus(Optional.ofNullable(entity.getTransferStatus()).orElse(StatusEntity.STATUS_DISABLE_STR));
        System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        simpleDateFormat.format(new Date());
        if (entity.getFileTime() != null && entity.getFileTime().length() > 4) {
            entity.setVintages(entity.getFileTime().substring(0, 4));
        } else {
            entity.setVintages(simpleDateFormat.format(new Date()));
        }
        return super.insert(entity);
    }

    @Override
    public long delete(SqlQuery<Management> sqlQuery) {
        Management management = selectOne(sqlQuery);
        //删除文件
        manageFileService.deleteByRefId(management.getId());
        //删除历史记录
        managementHistoryService.deleteByBussinessId(management.getId());
        return super.delete(sqlQuery);
    }

    @Override
    public List<FondsDescriptiveFileCountVo> getFondsCountByYear(String fondId) {
        SqlQuery sqlQuery = SqlQuery.from(Management.class, false);
        if (Constants.ORG_TYPE_DAG.equals(SecurityHolder.get().currentOrganise().getOrganiseType())) {
            sqlQuery.equal(ManagementInfo.TRANSFERSTATUS, '1');
        }
        return commonMapper.selectByQuery(sqlQuery.column(ManagementInfo.ID.count().alias("count"), ManagementInfo.VINTAGES.alias("name")).groupBy(ManagementInfo.VINTAGES.alias("name")).equal(ManagementInfo.FONDID, fondId).orderByDesc(ManagementInfo.VINTAGES).setReturnClass(FondsDescriptiveFileCountVo.class));
    }

    @Override
    public long updateByExamin(String processInstanceId, String taskId, String optType, Management management) {
        addManagementHistory(processInstanceId, taskId, management.getId(), optType, "1", "");
        return updateById(management);
    }

    @Override
    public List<Management> getManagementList(String fondId, String startYear, String endYear) {
        return commonMapper.selectByQuery(SqlQuery.from(Management.class).equal(ManagementInfo.TRANSFERSTATUS, '0').equal(ManagementInfo.FONDID, fondId).greaterThanEqual(ManagementInfo.VINTAGESSTART, startYear).lessThanEqual(ManagementInfo.VINTAGESEND));
    }

    /**
     * 启动流程前回调
     *
     * @param context
     */
    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        context.setBusinessId((String) context.getBusinessParams().get("businessId"));
        context.setProcessInstanceTitle(DateFormatUtils.format(new Date(), DATE_PATTEN) + "发起的" + getName() + "审核");
        //设置流程查看详情跳转页面
        context.addVar(ProcessConstants.PROCESS_FORM_URL_KEY, "/archive/management/edit");
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, context.getBusinessId());
        //需要把类型参数传过来，保存、修改、删除
        context.addVar("historyType", context.getBusinessParams().get("historyType"));
    }

    /**
     * 启动流程后回调
     *
     * @param context
     */
    @Override
    public void onAfterStartProcess(ProcessContext context) {
        //修改全宗卷状态为审批中
        Management management = new Management();
        management.setId(context.getBusinessId());
        management.setStatus("1");//审核中
        commonMapper.updateIgnoreNullById(management);
        ProcessTypeProvider.super.onAfterStartProcess(context);
    }

    /**
     * 环节执行完回调
     *
     * @param context
     */
    @Override
    public void onAfterCompleteTask(TaskContext context) {
        String managementId = (String) context.getBusinessParams().get("businessId");
        //添加全宗卷历史表
        addManagementHistory(context.getTaskInstance().getProcessInstanceId(), context.getTaskInstance().getId(), managementId, (String) context.getBusinessParams().get("historyType"), (String) context.getBusinessParams().get("isPass"), (String) context.getBusinessParams().get("examinationOpinion"));
        //修改正文内容
        Management management = new Management();
        management.setId(managementId);
        management.setCompilationContent((String) context.getBusinessParams().get("compilationContent"));
        commonMapper.updateIgnoreNullById(management);
        ProcessTypeProvider.super.onAfterCompleteTask(context);
    }

    /**
     * 流程办结回调
     *
     * @param context
     */
    @Override
    public void onAfterEndProcess(TaskContext context) {
        Management management = selectById((String) context.getBusinessParams().get("businessId"));
        management.setStatus((String) context.getBusinessParams().get("isPass"));//审核结果
        String isPass = (String) context.getBusinessParams().get("isPass");
        //如果是删除并且办结结果未通过则删除数据
        if ("delete".equals(context.getTaskInstance().getProcessVariables().get("historyType")) && "2".equals(isPass)) {
            // 删除数据 TODO 删除资料库内容
            delete(SqlQuery.from(Management.class).equal(ManagementInfo.ID, (String) context.getBusinessParams().get("businessId")));
        } else {
            //添加全宗卷历史表
            addManagementHistory(context.getTaskInstance().getProcessInstanceId(), context.getTaskInstance().getId(), management.getId(), (String) context.getBusinessParams().get("historyType"), (String) context.getBusinessParams().get("isPass"), (String) context.getBusinessParams().get("examinationOpinion"));
            commonMapper.updateIgnoreNullById(management);
            //通过后生成pdf
            if ("2".equals(isPass)) {
                manageFileService.htmlToPdf(management.getId(), management.getTitle(), management.getCompilationContent(), false);
            }
        }
        ProcessTypeProvider.super.onAfterEndProcess(context);
    }

    /**
     * 添加全宗卷历史表
     *
     * @param processInstanceId  流程实例
     * @param taskInstanceId     任务实例
     * @param managementId       全宗卷id
     * @param historyType        类型
     * @param isPass             是否通过
     * @param examinationOpinion 审批意见
     */
    private void addManagementHistory(String processInstanceId, String taskInstanceId, String managementId, String historyType, String isPass, String examinationOpinion) {
        ManagementHistory managementHistory = new ManagementHistory();
        BeanUtils.copyProperties(selectById(managementId), managementHistory);
        managementHistory.setId(UUIDUtils.getUUID());
        managementHistory.setProcessInstanceId(processInstanceId);
        managementHistory.setTaskInstanceId(taskInstanceId);
        managementHistory.setManagementId(managementId);
        managementHistory.setHistoryType(historyType);
        managementHistory.setIsPass(isPass);
        managementHistory.setExaminationOpinion(examinationOpinion);
        managementHistoryService.insert(managementHistory);
    }

    @Override
    public String getType() {
        return Constants.PROCESS_TYPE_FONDS_DESCRIPTIVE_FILE;
    }

    @Override
    public String getName() {
        return "全宗卷";
    }
}