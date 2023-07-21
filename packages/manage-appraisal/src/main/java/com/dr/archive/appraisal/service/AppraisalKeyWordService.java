package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalKeyWord;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface AppraisalKeyWordService extends BaseService<AppraisalKeyWord> {

    void deleteByRules (String rulesId);

    List<AppraisalKeyWord> getKeyWordByOrgId(String rulesId);

    void deleteByBasis(String basisId);


}
