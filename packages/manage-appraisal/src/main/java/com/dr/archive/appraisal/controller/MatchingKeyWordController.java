package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.MatchingKeyWord;
import com.dr.archive.appraisal.entity.MatchingKeyWordInfo;
import com.dr.archive.appraisal.service.MatchingKeyWordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 匹配到的关键词
 */
@RestController
@RequestMapping("api/matchingKeyWord")
public class MatchingKeyWordController extends BaseServiceController<MatchingKeyWordService, MatchingKeyWord> {

    @Override
    protected SqlQuery<MatchingKeyWord> buildPageQuery(HttpServletRequest httpServletRequest, MatchingKeyWord matchingKeyWord) {
        SqlQuery sqlQuery = SqlQuery.from(MatchingKeyWord.class);
        sqlQuery.equal(MatchingKeyWordInfo.APPRAISALID,matchingKeyWord.getAppraisalId())
                .equal(MatchingKeyWordInfo.FORMDATAID,matchingKeyWord.getFormDataId())
                .equal(MatchingKeyWordInfo.FORMDEFINITIONID,matchingKeyWord.getFormDefinitionId())
                .orderBy(MatchingKeyWordInfo.PRIORITY);
        return sqlQuery;
    }
}
