package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalRecommendKeyWord;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;


public interface AppraisalRecommendKeyWordService extends BaseService<AppraisalRecommendKeyWord> {

    //关键词来源--推荐
    String SOURCE_TYPE_RECOMMEND = "recommend";
    //关键词来源--推荐
    String SOURCE_TYPE_APPRAISAL = "appraisal";

    ResultEntity adopt(AppraisalRecommendKeyWord appraisalRecommendKeyWord);

    ResultEntity countToDo();
}
