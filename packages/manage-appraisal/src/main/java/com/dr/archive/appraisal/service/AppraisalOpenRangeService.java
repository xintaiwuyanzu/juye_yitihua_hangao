package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalOpenRange;
import com.dr.framework.common.service.BaseService;

import java.util.HashMap;

public interface AppraisalOpenRangeService extends BaseService<AppraisalOpenRange> {

    HashMap<String,Integer>  getResultPriority();


}
