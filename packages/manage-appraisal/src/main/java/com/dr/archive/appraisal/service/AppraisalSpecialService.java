package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalSpecial;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface AppraisalSpecialService extends BaseService<AppraisalSpecial> {

    void deleteByRules (String rulesId);

    List<AppraisalSpecial> getSpecialByOrgId(String rulesId);

    void deleteByBasis(String basisId);


}
