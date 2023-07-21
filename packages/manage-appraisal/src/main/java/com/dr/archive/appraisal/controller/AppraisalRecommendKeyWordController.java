package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalRecommendKeyWord;
import com.dr.archive.appraisal.entity.AppraisalRecommendKeyWordInfo;
import com.dr.archive.appraisal.service.AppraisalRecommendKeyWordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 档案鉴定匹配到的关键词
 */
@RestController
@RequestMapping("api/appraisalRecommendKeyWord")
public class AppraisalRecommendKeyWordController extends BaseServiceController<AppraisalRecommendKeyWordService, AppraisalRecommendKeyWord> {

    @Override
    protected SqlQuery<AppraisalRecommendKeyWord> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalRecommendKeyWord appraisalRecommendKeyWord) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalRecommendKeyWord.class)
                .equal(AppraisalRecommendKeyWordInfo.STATUS,appraisalRecommendKeyWord.getStatus())
                .equal(AppraisalRecommendKeyWordInfo.CREATEPERSON,appraisalRecommendKeyWord.getCreatePerson())
                .equal(AppraisalRecommendKeyWordInfo.BATCHID,appraisalRecommendKeyWord.getBatchId())
                .orderByDesc(AppraisalRecommendKeyWordInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping({"/adopt"})
    public ResultEntity adopt(AppraisalRecommendKeyWord appraisalRecommendKeyWord) {
        return service.adopt(appraisalRecommendKeyWord);
    }

    @RequestMapping({"/countToDo"})
    public ResultEntity countToDo() {
        return service.countToDo();
    }

}
