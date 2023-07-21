package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.*;
import com.dr.archive.appraisal.service.AppraisalKeyWordService;
import com.dr.archive.appraisal.service.AppraisalRulesService;
import com.dr.archive.appraisal.service.AppraisalSpecialService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisalRulesServiceImpl extends DefaultBaseService<AppraisalRules> implements AppraisalRulesService {

    @Autowired
    AppraisalKeyWordService appraisalKeyWordService;
    @Autowired
    AppraisalSpecialService appraisalSpecialService;

    @Override
    public List<AppraisalRules> getRulesByOrgId(String orgId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalRules.class)
                .equal(AppraisalRulesInfo.ORGID,orgId);
        List<AppraisalRules> appraisalRulesList = commonMapper.selectByQuery(sqlQuery);
        for(AppraisalRules appraisalRules:appraisalRulesList){
            appraisalRules.setKeyWordList(appraisalKeyWordService.getKeyWordByOrgId(appraisalRules.getId()));
            appraisalRules.setSpecialList(appraisalSpecialService.getSpecialByOrgId(appraisalRules.getId()));
        }
        return appraisalRulesList;
    }

    @Override
    public List<AppraisalRules> getRulesByBasisId(String basis) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalRules.class)
                .equal(AppraisalRulesInfo.BASISID,basis);
        List<AppraisalRules> appraisalRulesList = commonMapper.selectByQuery(sqlQuery);
        for(AppraisalRules appraisalRules:appraisalRulesList){
            appraisalRules.setKeyWordList(appraisalKeyWordService.getKeyWordByOrgId(appraisalRules.getId()));
            appraisalRules.setSpecialList(appraisalSpecialService.getSpecialByOrgId(appraisalRules.getId()));
        }
        return appraisalRulesList;
    }

    @Override
    public void deleteByBasis(String basisId) {
        delete(SqlQuery.from(AppraisalRules.class).equal(AppraisalRulesInfo.BASISID,basisId));
    }
}
