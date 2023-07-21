package com.dr.archive.onlineGuide.service;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceCategoryDictionary;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;

public interface BusinessGuidanceClassDictionaryService extends BaseService<BusinessGuidanceCategoryDictionary> {

    ResultEntity insertClass (String cType, String cProblem , String cResult);

    ResultEntity queryType(int type);

    ResultEntity queryProblem(String type);

    ResultEntity classifyBatch(String batchId, String classifyId,String classifyType);
}
