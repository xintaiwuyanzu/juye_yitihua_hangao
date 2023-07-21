package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalKeyWord;
import com.dr.archive.appraisal.entity.AppraisalKeyWordInfo;
import com.dr.archive.appraisal.service.AppraisalKeyWordService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisalKeyWordServiceImpl extends DefaultBaseService<AppraisalKeyWord> implements AppraisalKeyWordService {

    @Override
    public void deleteByRules(String rulesId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalKeyWord.class).equal(AppraisalKeyWordInfo.RULESID,rulesId);
        commonMapper.deleteByQuery(sqlQuery);

    }

    @Override
    public List<AppraisalKeyWord> getKeyWordByOrgId(String rulesId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalKeyWord.class)
                .equal(AppraisalKeyWordInfo.RULESID,rulesId);
        return commonMapper.selectByQuery(sqlQuery);
    }

    @Override
    public void deleteByBasis(String basisId) {
        delete(SqlQuery.from(AppraisalKeyWord.class).equal(AppraisalKeyWordInfo.BASISID,basisId));
    }
}
