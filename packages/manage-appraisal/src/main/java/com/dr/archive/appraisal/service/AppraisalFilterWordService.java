package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalFilterWord;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface AppraisalFilterWordService extends BaseService<AppraisalFilterWord> {

    void deleteByGroupId(String groupId);

    List<AppraisalFilterWord> selectByGroupId(String groupId);

}
