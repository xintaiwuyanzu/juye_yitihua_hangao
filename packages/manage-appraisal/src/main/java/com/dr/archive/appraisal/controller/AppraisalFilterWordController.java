package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalFilterWord;
import com.dr.archive.appraisal.entity.AppraisalFilterWordInfo;
import com.dr.archive.appraisal.service.AppraisalFilterWordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 档案鉴定过滤词
 */
@RestController
@RequestMapping("api/appraisalFilterWord")
public class AppraisalFilterWordController extends BaseServiceController<AppraisalFilterWordService, AppraisalFilterWord> {

    @Override
    protected SqlQuery<AppraisalFilterWord> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalFilterWord appraisalFilterWord) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalFilterWord.class);
        sqlQuery.equal(AppraisalFilterWordInfo.GROUPID,appraisalFilterWord.getGroupId());
        return sqlQuery;
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AppraisalFilterWord entity) {
        String [] ids = entity.getId().split(",");
        for(String id:ids){
            AppraisalFilterWord e = new AppraisalFilterWord();
            e.setId(id);
            super.delete(request, e);
        }
        return ResultEntity.success(true);
    }

}
