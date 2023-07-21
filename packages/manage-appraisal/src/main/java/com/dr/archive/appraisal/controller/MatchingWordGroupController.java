package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.MatchingWordGroup;
import com.dr.archive.appraisal.service.MatchingWordGroupService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 匹配到的关键词库
 */
@RestController
@RequestMapping("api/matchingWordGroup")
public class MatchingWordGroupController extends BaseServiceController<MatchingWordGroupService, MatchingWordGroup> {


    @Override
    protected SqlQuery<MatchingWordGroup> buildPageQuery(HttpServletRequest httpServletRequest, MatchingWordGroup matchingWordGroupOfToBeAppraisal) {
        return null;
    }

    @RequestMapping("/getAllMatchingWordGroup")
    public ResultEntity<List> getAllMatchingWordGroup(String toBeAppraisalId) {
        return ResultEntity.success(service.getAllMatchingWordGroup(toBeAppraisalId));
    }


}
