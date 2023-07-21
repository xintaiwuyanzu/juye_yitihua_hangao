package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalRules;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface AppraisalRulesService extends BaseService<AppraisalRules> {

    List<AppraisalRules> getRulesByOrgId(String orgId);

    List<AppraisalRules> getRulesByBasisId(String basis);

    void deleteByBasis(String basisId);

}
