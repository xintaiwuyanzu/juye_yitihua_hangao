package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalRangeExclude;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;


public interface AppraisalRangeExcludeService extends BaseService<AppraisalRangeExclude> {

    List<AppraisalRangeExclude> getAppraisalRangeExcludeByOrgId(String orgId);

    Map<String,List> getAppraisalRangeExcludeMapByOrgId(String orgId);


}
