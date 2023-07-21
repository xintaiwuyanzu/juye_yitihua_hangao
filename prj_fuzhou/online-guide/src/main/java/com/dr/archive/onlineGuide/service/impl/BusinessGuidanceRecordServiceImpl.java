package com.dr.archive.onlineGuide.service.impl;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetail;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetailInfo;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceRecord;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchDetailService;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchService;
import com.dr.archive.onlineGuide.service.BusinessGuidanceRecordService;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @Author: caor
 * @Date: 2022-06-10 15:25
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessGuidanceRecordServiceImpl extends DefaultBaseService<BusinessGuidanceRecord> implements BusinessGuidanceRecordService {
    @Autowired
    BusinessGuidanceBatchService batchService;
    @Autowired
    BusinessGuidanceBatchDetailService detailService;

    @Override
    public long insert(BusinessGuidanceRecord entity) {
        //todo 先判断详情表中是否有未处理的该档案

        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        if (BusinessGuidanceRecord.type_reply.equals(entity.getType())) {
            //从历史表中查询,复制发送人和接收人的信息
            BusinessGuidanceBatchDetail oldDetail = detailService.selectById(entity.getDetailId());
            BusinessGuidanceBatch oldBatch = batchService.selectById(oldDetail.getBatchId());


            entity.setSendOrgId(oldBatch.getSendOrgId());
            entity.setSendOrgName(oldBatch.getSendOrgName());
            entity.setSendUserId(oldBatch.getSendUserId());
            entity.setSendUserName(oldBatch.getSendUserName());

            entity.setReceiveOrgId(organise.getId());
            entity.setReceiveOrgName(organise.getOrganiseName());
            entity.setReceiveUserId(person.getId());
            entity.setReceiveUserName(person.getUserName());
            //更新发起人记录中接收人
//            BusinessGuidanceRecord old = selectById(oldDetail.getDetailId());
//            old.setReceiveOrgId(organise.getId());
//            old.setReceiveOrgName(organise.getOrganiseName());
//            old.setReceiveUserId(person.getId());
//            old.setReceiveUserName(person.getUserName());
//            updateById(old);

            //更新详情
            List<BusinessGuidanceBatchDetail> detailList = getDetailListFormDataId(entity.getBusinessId());
            detailList.forEach(detail -> {
                detail.setStatus(StatusEntity.STATUS_ENABLE_STR);//已指导
                detailService.updateById(detail);
            });

            if(commonMapper.countByQuery(SqlQuery.from(BusinessGuidanceBatchDetail.class)
                    .equal(BusinessGuidanceBatchDetailInfo.STATUS, StatusEntity.STATUS_DISABLE_STR)
                    .equal(BusinessGuidanceBatchDetailInfo.BATCHID,oldBatch.getId())
            )==0){
                //更新批次状态
                BusinessGuidanceBatch businessGuidanceBatch = batchService.selectById(oldBatch.getId());
                businessGuidanceBatch.setStatus(StatusEntity.STATUS_ENABLE_STR);//已指导
                businessGuidanceBatch.setReceiveOrgId(organise.getId());
                businessGuidanceBatch.setReceiveOrgName(organise.getOrganiseName());
                businessGuidanceBatch.setReceiveUserId(person.getId());
                businessGuidanceBatch.setReceiveUserName(person.getUserName());
                batchService.updateById(businessGuidanceBatch);
            }
        } else {
            entity.setSendOrgId(organise.getId());
            entity.setSendOrgName(organise.getOrganiseName());
            entity.setSendUserId(person.getId());
            entity.setSendUserName(person.getUserName());
        }
        entity.setCreateUserName(person.getUserName());
        entity.setCreateOrgId(organise.getId());
        entity.setCreateOrgName(organise.getOrganiseName());
        return super.insert(entity);
    }

    /**
     * 回答问题
     * @param entity
     * @return
     */
    public long insertProblem(BusinessGuidanceRecord entity){
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        //回复问题
        if (BusinessGuidanceRecord.type_reply.equals(entity.getType())) {
            //从历史表中查询,复制发送人和接收人的信息
            BusinessGuidanceBatchDetail oldDetail = detailService.selectById(entity.getDetailId());
            BusinessGuidanceBatch oldBatch = batchService.selectById(oldDetail.getBatchId());


            entity.setSendOrgId(oldBatch.getSendOrgId());
            entity.setSendOrgName(oldBatch.getSendOrgName());
            entity.setSendUserId(oldBatch.getSendUserId());
            entity.setSendUserName(oldBatch.getSendUserName());

            entity.setReceiveOrgId(organise.getId());
            entity.setReceiveOrgName(organise.getOrganiseName());
            entity.setReceiveUserId(person.getId());
            entity.setReceiveUserName(person.getUserName());
            //更新发起人记录中接收人
//            BusinessGuidanceRecord old = selectById(oldDetail.getDetailId());
//            old.setReceiveOrgId(organise.getId());
//            old.setReceiveOrgName(organise.getOrganiseName());
//            old.setReceiveUserId(person.getId());
//            old.setReceiveUserName(person.getUserName());
//            updateById(old);

            //更新详情
            List<BusinessGuidanceBatchDetail> detailList = getDetailListFormDataId(entity.getBusinessId());
            detailList.forEach(detail -> {
                detail.setStatus(StatusEntity.STATUS_ENABLE_STR);//已指导
                detailService.updateById(detail);
            });

            if(commonMapper.countByQuery(SqlQuery.from(BusinessGuidanceBatchDetail.class)
                    .equal(BusinessGuidanceBatchDetailInfo.STATUS, StatusEntity.STATUS_DISABLE_STR)
                    .equal(BusinessGuidanceBatchDetailInfo.BATCHID,oldBatch.getId())
            )==0){
                //更新批次状态
                BusinessGuidanceBatch businessGuidanceBatch = batchService.selectById(oldBatch.getId());
                businessGuidanceBatch.setStatus(StatusEntity.STATUS_ENABLE_STR);//已指导
                businessGuidanceBatch.setReceiveOrgId(organise.getId());
                businessGuidanceBatch.setReceiveOrgName(organise.getOrganiseName());
                businessGuidanceBatch.setReceiveUserId(person.getId());
                businessGuidanceBatch.setReceiveUserName(person.getUserName());
                batchService.updateById(businessGuidanceBatch);
            }
        } else {
            entity.setSendOrgId(organise.getId());
            entity.setSendOrgName(organise.getOrganiseName());
            entity.setSendUserId(person.getId());
            entity.setSendUserName(person.getUserName());
        }
        entity.setCreateUserName(person.getUserName());
        entity.setCreateOrgId(organise.getId());
        entity.setCreateOrgName(organise.getOrganiseName());
        return super.insert(entity);
    }

    //获取未回复详情记录表中档案
    private List<BusinessGuidanceBatchDetail> getDetailListFormDataId(String formDataId) {
        return detailService.selectList(SqlQuery.from(BusinessGuidanceBatchDetail.class).equal(BusinessGuidanceBatchDetailInfo.FORMDATAID, formDataId).equal(BusinessGuidanceBatchDetailInfo.STATUS, StatusEntity.STATUS_DISABLE_STR));
    }
}
