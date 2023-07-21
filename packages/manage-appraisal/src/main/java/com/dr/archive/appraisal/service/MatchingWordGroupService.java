package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.MatchingWordGroup;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

public interface MatchingWordGroupService extends BaseService<MatchingWordGroup> {


    List<Map> getAllMatchingWordGroup(String toBeAppraisalId);

    List<MatchingWordGroup> getMatchingWordGroup(String toBeAppraisalId);

}
