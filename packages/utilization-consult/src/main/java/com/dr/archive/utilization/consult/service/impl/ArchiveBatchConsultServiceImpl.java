package com.dr.archive.utilization.consult.service.impl;

import com.dr.archive.archivecar.bo.ArchiveCarDetailTag;
import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.entity.ArchiveCarBatchInfo;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.archivecar.service.ArchiveCarBatchService;
import com.dr.archive.archivecar.service.ArchiveCarDetailService;
import com.dr.archive.archivecar.service.ArchiveCarTypeProvider;
import com.dr.archive.batch.entity.ArchiveBatchDetailComment;
import com.dr.archive.batch.entity.ArchiveBatchDetailCommentInfo;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchServiceImpl;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsultInfo;
import com.dr.archive.utilization.consult.entity.ArchiveBatchDetailConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchDetailConsultInfo;
import com.dr.archive.utilization.consult.service.ArchiveBatchConsultService;
import com.dr.archive.utilization.consult.service.ArchiveBatchDetailConsultService;
import com.dr.framework.common.exception.NeedLoginException;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 查档利用service实现类
 *
 * @author dr
 */
@Service
public class ArchiveBatchConsultServiceImpl extends AbstractArchiveBatchServiceImpl<ArchiveBatchConsult>
        implements ArchiveBatchConsultService, ProcessTypeProvider, ArchiveCarTypeProvider, InitializingBean {
    private static List<ArchiveCarDetailTag> carDetailTags;
    private final Logger logger = LoggerFactory.getLogger(ArchiveBatchConsultService.class);
    @Autowired
    ArchiveBatchDetailConsultService detailConsultService;
    @Autowired
    ArchiveCarBatchService carBatchService;
    @Autowired
    ArchiveCarDetailService carDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insert(ArchiveBatchConsult entity) {
        Organise organise = SecurityHolder.get().currentOrganise();
        if (organise == null) {
            throw new NeedLoginException("用户未登录");
        }
        if (exists(entity.getId())) {
            //如果指定id存在，则是更新数据
            super.updateById(entity);
            return 1;
        } else {
            //生成查档编号
            if(!StringUtils.isEmpty(entity.getReceiveOrgId())){
                entity.setCreateOrgId(organise.getId());
                entity.setUserName(organise.getOrganiseName());
            }
            String batchName = buildBatchName(organise, entity);
            entity.setBatchName(batchName);
            entity.setOrgId(organise.getId());

            super.insert(entity);

            //同步创建档案车
            ArchiveCarBatch carBatch = new ArchiveCarBatch();
            //档案车Id与档案查档批次相同
            carBatch.setId(entity.getId());
            carBatch.setBatchName("查档车【" + batchName + "】");
            carBatch.setBatchType(getType());
            carBatchService.insert(carBatch);
            return 2;
        }
    }

    private String buildBatchName(Organise organise, ArchiveBatchConsult entity) {
        long today = System.currentTimeMillis() - ((System.currentTimeMillis() + 8 * 60 * 60 * 1000) % (24 * 60 * 60 * 1000));
        //查询当前机构当天查档登记数量
        SqlQuery<ArchiveBatchConsult> sideSqlQuery = SqlQuery.from(ArchiveBatchConsult.class)
                .equal(ArchiveBatchConsultInfo.ORGID, organise.getId())
                .greaterThanEqual(ArchiveBatchConsultInfo.CREATEDATE, today);
        long count = 0;
        //加上线程锁，防止同时读
        synchronized (carDetailTags) {
            count = count(sideSqlQuery) + 1;
        }
        StringBuilder sb = new StringBuilder(String.valueOf(count));
        while (sb.length() < 3) {
            //补全0
            sb.insert(0, "0");
        }
        return String.join("-", DateFormatUtils.format(System.currentTimeMillis(), "YYYYMMDD"), sb, entity.getUserName());
    }

    /**
     * 流程启动回调
     *
     * @param context
     */
    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String batchId = (String) context.getBusinessParams().get("batchId");
        context.setBusinessId(batchId);
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, batchId);

        ArchiveBatchConsult batchConsult = selectById(batchId);
        long endDate = Long.parseLong((String) context.getBusinessParams().get("endDate"));
        //设置到期时间
        batchConsult.setEndDate(endDate);
        batchConsult.setDetailNum(detailConsultService.countTotal(batchId));

        updateById(batchConsult);
        //复制档案详情数据
        copyArchiveDetail(batchConsult, context);
        context.setProcessInstanceTitle(batchConsult.getBatchName() + "的查档审核");
        //设置流程查看详情跳转页面
        context.addVar(ProcessConstants.PROCESS_FORM_URL_KEY, "/utilization/consult/taskDetail");
    }

    /**
     * 复制档案详情数据
     *
     * @param batchConsult
     * @param context
     */
    private void copyArchiveDetail(ArchiveBatchConsult batchConsult, ProcessContext context) {
        //查询档案车指定标签数据
        List<ArchiveCarDetail> details = carDetailService.selectByBatchAndTag(batchConsult.getId(), (String) context.getBusinessParams().get("type"));
        //复制档案车数据到查档详情
        for (ArchiveCarDetail detail : details) {
            detailConsultService.copyDetail(detail, batchConsult);
        }
    }

    @Override
    public void onAfterEndProcess(TaskContext context) {
        //TODO 复制档案车审批通过档案到查档批次
        Object batchId = context.getTaskInstance().getProcessVariables().get("$businessId");
        List<ArchiveBatchDetailConsult> archiveBatchDetailConsults = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchDetailConsult.class).equal(ArchiveBatchDetailConsultInfo.BATCHID, batchId.toString()));
        archiveBatchDetailConsults.forEach(archiveBatchDetailConsult -> {
            ArchiveCarDetail archiveCarDetail = commonMapper.selectOneByQuery(SqlQuery.from(ArchiveCarDetail.class).equal(ArchiveCarDetailInfo.BATCHID, archiveBatchDetailConsult.getBatchId()).equal(ArchiveCarDetailInfo.FORMDATAID, archiveBatchDetailConsult.getFormDataId()));
            List<ArchiveBatchDetailComment> archiveBatchDetailComments = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchDetailComment.class).equal(ArchiveBatchDetailCommentInfo.BATCHID, archiveBatchDetailConsult.getBatchId()).equal(ArchiveBatchDetailCommentInfo.DETAILID, archiveCarDetail.getId()).orderByDesc(ArchiveBatchDetailCommentInfo.CREATEDATE));
            if (archiveBatchDetailComments.size()>0){
                archiveBatchDetailConsult.setUseFile(archiveBatchDetailComments.get(0).isUseFile());
                if (archiveBatchDetailComments.get(0).isUseFile()){
                    archiveCarDetail.setStatus("1");
                } else {
                    archiveCarDetail.setStatus("");
                }
            }else {
                archiveBatchDetailConsult.setUseFile(true);
                archiveCarDetail.setStatus("1");
            }
            commonMapper.updateById(archiveBatchDetailConsult);
            commonMapper.updateById(archiveCarDetail);
            commonMapper.updateByQuery(SqlQuery.from(ArchiveCarBatch.class).equal(ArchiveCarBatchInfo.ID,archiveCarDetail.getBatchId()).set(ArchiveCarBatchInfo.STATUS,"0"));
        });
        toEnd(batchId.toString(), "查档登记办结！");
    }

    @Override
    public String getType() {
        return "utilizationConsult";
    }

    @Override
    public String getName() {
        return "查档登记";
    }

    @Override
    public List<ArchiveCarDetailTag> getDetailTags(String personId) {
        return carDetailTags;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        List<ArchiveCarDetailTag> tagList = new ArrayList<>();
        ArchiveCarDetailTag tag = new ArchiveCarDetailTag();
        tag.setName("查看原文");
        tag.setCarType(getType());
        tag.setCode("gx");
        tagList.add(tag);

        ArchiveCarDetailTag tag1 = new ArchiveCarDetailTag();
        tag1.setName("不查看原文");
        tag1.setCarType(getType());
        tag1.setCode("ck");
        tagList.add(tag1);

        carDetailTags = Collections.unmodifiableList(tagList);

    }

    @Override
    public void toEnd(String id, String remarks) {
        ArchiveBatchConsult archiveBatchConsult = selectById(id);
        archiveBatchConsult.setRemarks(remarks);
        archiveBatchConsult.setStatus("2");
        updateById(archiveBatchConsult);
    }
}
