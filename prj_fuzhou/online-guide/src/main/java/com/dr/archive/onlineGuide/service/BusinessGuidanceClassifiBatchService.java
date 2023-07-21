package com.dr.archive.onlineGuide.service;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceClassifiBatch;
import com.dr.framework.common.service.BaseService;

public interface BusinessGuidanceClassifiBatchService extends BaseService<BusinessGuidanceClassifiBatch> {

     String insertClass(String batchId , String dId);

}
