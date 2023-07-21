/*
package com.dr.archive.opinion.management.service.impl;

import com.dr.archive.batch.service.impl.AbstractArchiveBatchServiceImpl;
import com.dr.archive.opinion.management.entity.OpinionManagement;
import com.dr.archive.opinion.management.entity.OpinionManagementInfo;
import com.dr.archive.opinion.management.service.OpinionManagementService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class OpinionManagementServiceImpl extends DefaultBaseService<OpinionManagement> implements OpinionManagementService {


    @Override
    public String addOpinion(String opinion, String personId) {

        SqlQuery<OpinionManagement> equal = SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.DEFENABLE, "0")
                .or()
                .equal(OpinionManagementInfo.BUSINESSID, personId)
                .andNew()
                .equal(OpinionManagementInfo.OPINION, opinion.trim());
        long l = commonMapper.countByQuery(equal);
        if(l>0){
            return "已存在";
        }
        OpinionManagement opinionManagement = new OpinionManagement();
        opinionManagement.setOpinion(opinion);
        opinionManagement.setBusinessId(personId);
        opinionManagement.setDefEnable("1");
        opinionManagement.setId(UUIDUtils.getUUID());
        opinionManagement.setCreateDate(System.currentTimeMillis());
        long insert = commonMapper.insertIgnoreNull(opinionManagement);
        if(insert == 0){
            return "失败";
        }
        return "添加完成";
    }

    @Override
    public String addDefOpinion(String opinion,String personId) {

        SqlQuery<OpinionManagement> equal = SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.DEFENABLE, "0")
                .equal(OpinionManagementInfo.OPINION, opinion.trim());
        long l = commonMapper.countByQuery(equal);
        if(l>0){
            return "已存在";
        }
        OpinionManagement opinionManagement = new OpinionManagement();
        opinionManagement.setOpinion(opinion);
        opinionManagement.setBusinessId(personId);
        opinionManagement.setDefEnable("0");
        opinionManagement.setId(UUIDUtils.getUUID());
        opinionManagement.setCreateDate(System.currentTimeMillis());
        long insert = commonMapper.insertIgnoreNull(opinionManagement);
        if(insert == 0){
            return "失败";
        }
        return "添加完成";
    }


}
*/
