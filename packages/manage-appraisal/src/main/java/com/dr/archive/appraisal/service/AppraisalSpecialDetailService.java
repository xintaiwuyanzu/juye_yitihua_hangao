package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalSpecialDetail;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface AppraisalSpecialDetailService extends BaseService<AppraisalSpecialDetail> {

    void deleteBySpecialId (String specialId);

    List<AppraisalSpecialDetail> getBySpecialId (String specialId);


}
