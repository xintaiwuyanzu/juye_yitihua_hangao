package com.dr.archive.onlineGuide.service.impl;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetail;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetailInfo;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceRecord;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchDetailService;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchService;
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
 * @Date: 2022-06-10 15:22
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessGuidanceBatchDetailServiceImpl extends DefaultBaseService<BusinessGuidanceBatchDetail> implements BusinessGuidanceBatchDetailService {
    @Autowired
    BusinessGuidanceBatchService batchService;

    @Override
    public Long getBatchTaskQuantity(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(BusinessGuidanceBatchDetail.class)
                .equal(BusinessGuidanceBatchDetailInfo.BATCHID,batchId);
        return commonMapper.countByQuery(sqlQuery);
    }

    @Override
    public Long fastReply(String batchId, String result) {
        SqlQuery s = SqlQuery.from(BusinessGuidanceBatchDetail.class).equal(BusinessGuidanceBatchDetailInfo.BATCHID,batchId);
        List<BusinessGuidanceBatchDetail> businessGuidanceBatchDetailList = commonMapper.selectByQuery(s);
        return fastReply(businessGuidanceBatchDetailList,batchId,result);
    }

    @Override
    public Long fastReplyByIds(String batchId, String result, String ids) {
        SqlQuery s = SqlQuery.from(BusinessGuidanceBatchDetail.class)
                .equal(BusinessGuidanceBatchDetailInfo.BATCHID,batchId)
                .in(BusinessGuidanceBatchDetailInfo.ID,ids);
        List<BusinessGuidanceBatchDetail> businessGuidanceBatchDetailList = commonMapper.selectByQuery(s);
        return fastReply(businessGuidanceBatchDetailList,batchId,result);
    }

    @Override
    public Long fastReplyBySearch(String batchId, String result, BusinessGuidanceBatchDetail businessGuidanceBatchDetail) {
        SqlQuery s = SqlQuery.from(BusinessGuidanceBatchDetail.class)
                .equal(BusinessGuidanceBatchDetailInfo.BATCHID,batchId)
                .like(BusinessGuidanceBatchDetailInfo.TITLE,businessGuidanceBatchDetail.getTitle());
        List<BusinessGuidanceBatchDetail> businessGuidanceBatchDetailList = commonMapper.selectByQuery(s);
        return fastReply(businessGuidanceBatchDetailList,batchId,result);
    }

    public Long fastReply(List<BusinessGuidanceBatchDetail> list,String batchId, String result){
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        //从历史表中查询,复制发送人和接收人的信息
        BusinessGuidanceBatch oldBatch = batchService.selectById(batchId);
        BusinessGuidanceRecord entity = new BusinessGuidanceRecord();
        entity.setSendOrgId(oldBatch.getSendOrgId());
        entity.setSendOrgName(oldBatch.getSendOrgName());
        entity.setSendUserId(oldBatch.getSendUserId());
        entity.setSendUserName(oldBatch.getSendUserName());
        entity.setReceiveOrgId(organise.getId());
        entity.setReceiveOrgName(organise.getOrganiseName());
        entity.setReceiveUserId(person.getId());
        entity.setReceiveUserName(person.getUserName());
        entity.setMessage(result);
        int u=0;
        for(BusinessGuidanceBatchDetail b:list){
            if(!b.getStatus().equals("2")){
                u++;
                b.setStatus(StatusEntity.STATUS_ENABLE_STR);
                updateById(b);
                entity.setBusinessId(b.getFormDataId());
                entity.setCreateUserName(person.getUserName());
                entity.setCreateOrgId(organise.getId());
                entity.setCreateOrgName(organise.getOrganiseName());
                super.getCommonService().insert(entity);
            }
        }
        oldBatch.setStatus("1");
        batchService.updateById(oldBatch);
        return (long)u;
    }
}
