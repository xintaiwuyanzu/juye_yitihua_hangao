package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalBatchWordGroup;
import com.dr.archive.appraisal.entity.AppraisalBatchWordGroupInfo;
import com.dr.archive.appraisal.entity.AppraisalRules;
import com.dr.archive.appraisal.service.AppraisalBatchWordGroupService;
import com.dr.archive.appraisal.service.AppraisalRulesService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppraisalBatchWordGroupServiceImpl extends DefaultBaseService<AppraisalBatchWordGroup> implements AppraisalBatchWordGroupService {

    @Autowired
    AppraisalRulesService appraisalWordGroupService;

    @Override
    public List<AppraisalBatchWordGroup> getAppraisalBatchWordGroupByBatchId(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatchWordGroup.class)
                                    .equal(AppraisalBatchWordGroupInfo.BATCHID,batchId);
        return selectList(sqlQuery);
    }

    @Override
    public void updatePriority(AppraisalBatchWordGroup appraisalBatchWordGroup) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatchWordGroup.class)
                .set(AppraisalBatchWordGroupInfo.PRIORITY,appraisalBatchWordGroup.getPriority())
                .set(AppraisalBatchWordGroupInfo.ISAPPLY,appraisalBatchWordGroup.getIsApply())
                .equal(AppraisalBatchWordGroupInfo.ID,appraisalBatchWordGroup.getId());
        updateBySqlQuery(sqlQuery);
    }

    @Override
    public String getWordGroupIdsByBatchId(String batchId) {
        List<AppraisalBatchWordGroup> list = getAppraisalBatchWordGroupByBatchId(batchId);
        StringBuilder s = new StringBuilder();
        for(AppraisalBatchWordGroup appraisalBatchWordGroup:list){
            s.append(appraisalBatchWordGroup.getWordGroupId()).append(",");
        }
        return s.toString();
    }

    @Override
    public void updateAppraisalBatchWordGroup(String batchId, String wordGroupIds) {

        SqlQuery deleteSql = SqlQuery.from(AppraisalBatchWordGroup.class);
        deleteSql.notIn(AppraisalBatchWordGroupInfo.WORDGROUPID,wordGroupIds);
        delete(deleteSql);

        SqlQuery selectSql = SqlQuery.from(AppraisalBatchWordGroup.class);
        selectSql.in(AppraisalBatchWordGroupInfo.WORDGROUPID,wordGroupIds);
        List<AppraisalBatchWordGroup> list = selectList(selectSql);
        Map idMap = new HashMap();
        for(AppraisalBatchWordGroup appraisalBatchWordGroup:list){
            idMap.put(appraisalBatchWordGroup.getWordGroupId(),"");
        }
        String [] wordGroups = wordGroupIds.split(",");
        int index = 1;
        for(String id:wordGroups){
            if(StringUtils.isEmpty(id)){
                continue;
            }
            if(!idMap.containsKey(id)){
                AppraisalRules appraisalWordGroup = appraisalWordGroupService.selectById(id);
                AppraisalBatchWordGroup appraisalBatchWordGroup = new AppraisalBatchWordGroup();
                appraisalBatchWordGroup.setWordGroupId(appraisalWordGroup.getId());
                appraisalBatchWordGroup.setBatchId(batchId);
                appraisalBatchWordGroup.setPriority((list.size()+index)+"");
                appraisalBatchWordGroup.setIsApply("0");
                index++;
                insert(appraisalBatchWordGroup);
            }
        }
    }


}
