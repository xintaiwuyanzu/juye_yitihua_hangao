package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalFilterWord;
import com.dr.archive.appraisal.entity.AppraisalFilterWordInfo;
import com.dr.archive.appraisal.service.AppraisalFilterWordService;
import com.dr.archive.appraisal.service.AppraisalRulesService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisalFilterWordServiceImpl extends DefaultBaseService<AppraisalFilterWord> implements AppraisalFilterWordService {

    @Autowired
    AppraisalRulesService appraisalRulesService;

    @Override
    public void deleteByGroupId(String groupId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalFilterWord.class)
                .equal(AppraisalFilterWordInfo.GROUPID,groupId);
        commonMapper.deleteByQuery(sqlQuery);
    }

    @Override
    public List<AppraisalFilterWord> selectByGroupId(String groupId) {
        return selectList(SqlQuery.from(AppraisalFilterWord.class).equal(AppraisalFilterWordInfo.GROUPID,groupId));
    }
}
