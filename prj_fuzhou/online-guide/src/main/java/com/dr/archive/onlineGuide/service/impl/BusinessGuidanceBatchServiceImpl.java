package com.dr.archive.onlineGuide.service.impl;

import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.service.ArchiveCarBatchService;
import com.dr.archive.archivecar.service.ArchiveCarDetailService;
import com.dr.archive.archivecar.service.ArchiveCarTypeProvider;
import com.dr.archive.onlineGuide.entity.*;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchDetailService;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchService;
import com.dr.archive.onlineGuide.service.BusinessGuidanceRecordService;
import com.dr.archive.service.impl.ArchiveOrganisePersonService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-06-10 15:20
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessGuidanceBatchServiceImpl extends DefaultBaseService<BusinessGuidanceBatch> implements BusinessGuidanceBatchService, ArchiveCarTypeProvider {
    @Autowired
    ArchiveCarBatchService archiveCarBatchService;
    @Autowired
    ArchiveCarDetailService archiveCarDetailService;
    @Autowired
    BusinessGuidanceBatchDetailService businessGuidanceBatchDetailService;
    @Autowired
    BusinessGuidanceRecordService businessGuidanceRecordService;
    @Autowired
    ArchiveOrganisePersonService archiveOrganisePersonService;

    private String getBatchName() {
        return SecurityHolder.get().currentPerson().getUserName() + "于" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd") + "提交的业务指导申请";
    }

    @Override
    public BusinessGuidanceBatchDetail guidance(String formDefinitionId, String formDataId, String description) {
        Assert.isTrue(StringUtils.hasText(formDefinitionId), "formDefinitionId不存在！");
        Assert.isTrue(StringUtils.hasText(formDataId), "formDataId不存在！");
        //档案车插入批次
        String archiveCarBatchId = UUIDUtils.getUUID();
        ArchiveCarBatch archiveCarBatch = new ArchiveCarBatch();
        archiveCarBatch.setId(archiveCarBatchId);
        archiveCarBatch.setBatchName(getBatchName());
        archiveCarBatch.setBatchType("businessGuidance");
        archiveCarBatchService.insert(archiveCarBatch);
        //档案车插入详情
        ArchiveCarDetail archiveCarDetail = new ArchiveCarDetail();
        archiveCarDetail.setBatchId(archiveCarBatchId);
        archiveCarDetail.setFormDefinitionId(formDefinitionId);
        archiveCarDetail.setFormDataId(formDataId);
        archiveCarDetailService.insert(archiveCarDetail);

        //其他调用插入批次 TODO 可能有问题，因为事务，还没有把数据插入到数据库中
        BusinessGuidanceBatch businessGuidanceBatch = new BusinessGuidanceBatch();
        businessGuidanceBatch.setArchiveCarId(archiveCarBatchId);
        businessGuidanceBatch.setDescription(description);

        //绑定批次基本信息
        bindBaseData(businessGuidanceBatch, archiveCarBatchService.selectById(archiveCarBatchId));

        //添加详情
        BusinessGuidanceBatchDetail businessGuidanceBatchDetail = new BusinessGuidanceBatchDetail();
        BusinessGuidanceBatchDetail businessGuidanceBatchDetail1 = addBusinessGuidanceBatchDetail(businessGuidanceBatchDetail, businessGuidanceBatch, archiveCarDetail, description);
/*        if (!StringUtils.isEmpty(success)){
            businessGuidanceBatchDetail.setDescription(description);
        }*/
        //添加记录 先不用到时候同步数据
        addBusinessGuidanceRecord(businessGuidanceBatchDetail);
        insert(businessGuidanceBatch);
        return businessGuidanceBatchDetail1;
    }
    /*public BusinessGuidanceBatchDetail guidanceProblem(String formDefinitionId, String formDataId, String description,String success){
        Assert.isTrue(StringUtils.hasText(formDefinitionId), "formDefinitionId不存在！");
        Assert.isTrue(StringUtils.hasText(formDataId), "formDataId不存在！");
        //档案车插入批次
        String archiveCarBatchId = UUIDUtils.getUUID();
        ArchiveCarBatch archiveCarBatch = new ArchiveCarBatch();
        archiveCarBatch.setId(archiveCarBatchId);
        archiveCarBatch.setBatchName(getBatchName());
        archiveCarBatch.setBatchType("businessGuidance");
        archiveCarBatchService.insert(archiveCarBatch);
        //档案车插入详情
        ArchiveCarDetail archiveCarDetail = new ArchiveCarDetail();
        archiveCarDetail.setBatchId(archiveCarBatchId);
        archiveCarDetail.setFormDefinitionId(formDefinitionId);
        archiveCarDetail.setFormDataId(formDataId);
        archiveCarDetailService.insert(archiveCarDetail);

        //其他调用插入批次 TODO 可能有问题，因为事务，还没有把数据插入到数据库中
        BusinessGuidanceBatch businessGuidanceBatch = new BusinessGuidanceBatch();
        businessGuidanceBatch.setArchiveCarId(archiveCarBatchId);
        businessGuidanceBatch.setDescription(description);

        //绑定批次基本信息
        bindBaseData(businessGuidanceBatch, archiveCarBatchService.selectById(archiveCarBatchId));

        //添加详情
        BusinessGuidanceBatchDetail businessGuidanceBatchDetail = new BusinessGuidanceBatchDetail();
        BusinessGuidanceBatchDetail businessGuidanceBatchDetail1 = addBusinessGuidanceBatchDetail(businessGuidanceBatchDetail, businessGuidanceBatch, archiveCarDetail, description);
        //添加记录 先不用到时候同步数据
        //addBusinessGuidanceRecord(businessGuidanceBatchDetail);
        insert(businessGuidanceBatch);
        return businessGuidanceBatchDetail1;
    }*/


    @Override
    public long batchApply(String carBatchId, String description) {
        Assert.isTrue(StringUtils.hasText(carBatchId), "档案车批次Id为空！");
        ArchiveCarBatch archiveCarBatch = archiveCarBatchService.selectById(carBatchId);
        //添加到业务指导批次
        BusinessGuidanceBatch businessGuidanceBatch = new BusinessGuidanceBatch();
        businessGuidanceBatch.setDescription(description);
        businessGuidanceBatch.setArchiveCarId(carBatchId);
        //绑定批次基本信息
        bindBaseData(businessGuidanceBatch, archiveCarBatch);
        insert(businessGuidanceBatch);

        //复制档案车详情到业务指导详情
        //添加指导记录
        List<ArchiveCarDetail> carDetailList = archiveCarDetailService.selectByBatchAndTag(carBatchId, null);
        carDetailList.forEach(archiveCarDetail -> {
            BusinessGuidanceBatchDetail businessGuidanceBatchDetail = new BusinessGuidanceBatchDetail();
            addBusinessGuidanceBatchDetail(businessGuidanceBatchDetail, businessGuidanceBatch, archiveCarDetail, archiveCarDetail.getDescription());
            //添加记录
            addBusinessGuidanceRecord(businessGuidanceBatchDetail);
        });
        return carDetailList.size();
    }

    @Override
    public String judgeExistence(String formDefinitionId, String formDataId) {
        Assert.isTrue(StringUtils.hasText(formDefinitionId), "formDefinitionId不存在！");
        Assert.isTrue(StringUtils.hasText(formDataId), "formDataId不存在！");
        Person person = SecurityHolder.get().currentPerson();
        SqlQuery<BusinessGuidanceBatchDetail> equal = SqlQuery.from(BusinessGuidanceBatchDetail.class)
                .equal(BusinessGuidanceBatchDetailInfo.FORMDATAID, formDataId)
                .equal(BusinessGuidanceBatchDetailInfo.FORMDEFINITIONID, formDefinitionId)
                .equal(BusinessGuidanceBatchDetailInfo.CREATEPERSON, person.getId());
        List<BusinessGuidanceBatchDetail> businessGuidanceBatchDetails = commonMapper.selectLimitByQuery(equal, 0, 1);
        if (!businessGuidanceBatchDetails.isEmpty()) {
            String batchId = businessGuidanceBatchDetails.get(0).getBatchId();
            BusinessGuidanceBatch businessGuidanceBatch = commonMapper.selectOneByQuery(SqlQuery.from(BusinessGuidanceBatch.class)
                    .equal(BusinessGuidanceBatchInfo.ID, batchId)
                    .equal(BusinessGuidanceBatchInfo.STATUS, "0")
                    .equal(BusinessGuidanceBatchInfo.CREATEPERSON, person.getId())
                    .isNotNull(BusinessGuidanceBatchInfo.GID));
            if (!StringUtils.isEmpty(businessGuidanceBatch)) {
                return businessGuidanceBatch.getGid();
            }
        }
        return "0";
    }


    /**
     * 添加详情
     *
     * @param businessGuidanceBatchDetail 指导详情
     * @param businessGuidanceBatch       指导批次
     * @param archiveCarDetail            档案车详情
     * @param description                 问题描述
     */
    BusinessGuidanceBatchDetail addBusinessGuidanceBatchDetail(BusinessGuidanceBatchDetail businessGuidanceBatchDetail, BusinessGuidanceBatch businessGuidanceBatch, ArchiveCarDetail archiveCarDetail, String description) {
        businessGuidanceBatchDetail.setId(UUIDUtils.getUUID());
        businessGuidanceBatchDetail.setBatchId(businessGuidanceBatch.getId());
        businessGuidanceBatchDetail.setArchiveCode(archiveCarDetail.getArchiveCode());
        businessGuidanceBatchDetail.setCategoryCode(archiveCarDetail.getCategoryCode());
        businessGuidanceBatchDetail.setCategoryId(archiveCarDetail.getCategoryId());
        businessGuidanceBatchDetail.setCategoryName(archiveCarDetail.getCategoryName());
        businessGuidanceBatchDetail.setFondCode(archiveCarDetail.getFondCode());
        businessGuidanceBatchDetail.setFondName(archiveCarDetail.getFondName());
        businessGuidanceBatchDetail.setFormDataId(archiveCarDetail.getFormDataId());
        businessGuidanceBatchDetail.setFormDefinitionId(archiveCarDetail.getFormDefinitionId());
        businessGuidanceBatchDetail.setKeyWords(archiveCarDetail.getKeyWords());
        businessGuidanceBatchDetail.setSaveTerm(archiveCarDetail.getSaveTerm());
        businessGuidanceBatchDetail.setTitle(archiveCarDetail.getTitle());
        businessGuidanceBatchDetail.setYear(archiveCarDetail.getYear());
        businessGuidanceBatchDetail.setDescription(description);
        businessGuidanceBatchDetail.setStatus(StatusEntity.STATUS_DISABLE_STR);
        businessGuidanceBatchDetailService.insert(businessGuidanceBatchDetail);
        return businessGuidanceBatchDetail;
    }

    //添加指导记录
    void addBusinessGuidanceRecord(BusinessGuidanceBatchDetail businessGuidanceBatchDetail) {
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        //添加指导记录表
        BusinessGuidanceRecord businessGuidanceRecord = new BusinessGuidanceRecord();
        businessGuidanceRecord.setSendUserId(person.getId());
        businessGuidanceRecord.setSendUserName(person.getUserName());
        businessGuidanceRecord.setSendOrgId(organise.getId());
        businessGuidanceRecord.setSendOrgName(organise.getOrganiseName());

        businessGuidanceRecord.setCreateUserName(person.getUserName());
        businessGuidanceRecord.setCreateOrgId(organise.getId());
        businessGuidanceRecord.setCreateOrgName(organise.getOrganiseName());

        Assert.isTrue(StringUtils.hasText(organise.getParentId()), "当前登录不是档案室账号或未查询到该账号所属档案馆！");
        businessGuidanceRecord.setReceiveOrgId(organise.getParentId());
        businessGuidanceRecord.setReceiveOrgName(archiveOrganisePersonService.getOrganise(organise.getParentId()).getOrganiseName());
        businessGuidanceRecord.setBusinessId(businessGuidanceBatchDetail.getFormDataId());//目前只能跟档案id绑定，无法跟详情批次id绑定，因为从管理库、暂存库查看单条档案时无法带着批次或详情id
        businessGuidanceRecord.setMessage(businessGuidanceBatchDetail.getDescription());
        businessGuidanceRecord.setDetailId(businessGuidanceBatchDetail.getId());
        businessGuidanceRecord.setBatchId(businessGuidanceBatchDetail.getBatchId());
        businessGuidanceRecordService.insert(businessGuidanceRecord);
    }

    /**
     * 绑定批次基本信息
     *
     * @param businessGuidanceBatch
     * @param archiveCarBatch
     */
    private void bindBaseData(BusinessGuidanceBatch businessGuidanceBatch, ArchiveCarBatch archiveCarBatch) {
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        businessGuidanceBatch.setId(UUIDUtils.getUUID());
        businessGuidanceBatch.setBatchName(getBatchName());
        businessGuidanceBatch.setDetailNum(archiveCarBatch.getDetailNum());
        businessGuidanceBatch.setSendOrgId(organise.getId());
        businessGuidanceBatch.setSendOrgName(organise.getOrganiseName());
        businessGuidanceBatch.setSendUserId(person.getId());
        businessGuidanceBatch.setSendUserName(person.getUserName());
        Assert.isTrue(StringUtils.hasText(organise.getParentId()), "当前登录不是档案室账号或未查询到该账号所属档案馆！");
        if (organise.getParentId().equals("root")){
            businessGuidanceBatch.setReceiveOrgId(organise.getId());
            businessGuidanceBatch.setReceiveOrgName(organise.getOrganiseName());
        }else {
            businessGuidanceBatch.setReceiveOrgId(organise.getParentId());
            businessGuidanceBatch.setReceiveOrgName(archiveOrganisePersonService.getOrganise(organise.getParentId()).getOrganiseName());
        }
        businessGuidanceBatch.setStatus(StatusEntity.STATUS_DISABLE_STR);//未指导
        businessGuidanceBatch.setCreateUserName(person.getUserName());
        businessGuidanceBatch.setCreateOrgId(organise.getId());
        businessGuidanceBatch.setCreateOrgName(organise.getOrganiseName());
    }

    @Override
    public String getType() {
        return "businessGuidance";
    }

    @Override
    public String getName() {
        return "业务指导";
    }

    @Override
    public List<BusinessGuidanceBatch> total() {
        Person person = SecurityHolder.get().currentPerson();
        SqlQuery<BusinessGuidanceBatch> sqlQuery = SqlQuery.from(BusinessGuidanceBatch.class, false)
                .equal(BusinessGuidanceBatchInfo.CREATEPERSON, person.getId())
                .count(BusinessGuidanceBatchInfo.ID, "id", false, false);
        List<BusinessGuidanceBatch> totals = commonMapper.selectByQuery(sqlQuery);
        return totals;
    }
}
