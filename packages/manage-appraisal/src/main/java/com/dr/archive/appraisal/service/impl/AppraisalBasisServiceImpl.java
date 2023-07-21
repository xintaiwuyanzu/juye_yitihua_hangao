package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalBasis;
import com.dr.archive.appraisal.entity.AppraisalBasisInfo;
import com.dr.archive.appraisal.service.AppraisalBasisService;
import com.dr.archive.appraisal.service.AppraisalRulesService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisalBasisServiceImpl extends DefaultBaseService<AppraisalBasis> implements AppraisalBasisService {

    @Autowired
    AppraisalRulesService appraisalRulesService;

    @Override
    public List<AppraisalBasis> getBasisByOrgId(String orgId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBasis.class)
                .equal(AppraisalBasisInfo.ORGID,orgId);
        List<AppraisalBasis> appraisalBasisList = commonMapper.selectByQuery(sqlQuery);
        for(AppraisalBasis appraisalBasis:appraisalBasisList){
            appraisalBasis.setRulesList(appraisalRulesService.getRulesByBasisId(appraisalBasis.getId()));
        }
        return appraisalBasisList;
    }

}
