package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalBasis;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface AppraisalBasisService extends BaseService<AppraisalBasis> {

    List<AppraisalBasis> getBasisByOrgId(String orgId);


}
