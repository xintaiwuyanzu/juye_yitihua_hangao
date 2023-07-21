package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.*;
import com.dr.archive.appraisal.service.*;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 关键词词库管理
 */
@RestController
@RequestMapping("api/appraisalRules")
public class AppraisalRulesController extends BaseServiceController<AppraisalRulesService, AppraisalRules> {

    @Autowired
    AppraisalKeyWordService appraisalKeyWordService;
    @Autowired
    AppraisalSpecialService appraisalSpecialService;
    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;


    @Override
    protected SqlQuery<AppraisalRules> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalRules appraisalRules) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalRules.class)
                .equal(AppraisalRulesInfo.BASISID,appraisalRules.getBasisId())
                .equal(AppraisalRulesInfo.ORGID, SecurityHolder.get().currentOrganise().getId())
                .orderBy(AppraisalRulesInfo.PRIORITY);
        return sqlQuery;
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AppraisalRules entity) {
        entity = service.selectById(entity.getId());
        ResultEntity<Boolean> r = super.delete(request,entity);
        if(r.getData()){
            appraisalKeyWordService.deleteByRules(entity.getId());
            appraisalSpecialService.deleteByRules(entity.getId());
            archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        }
        return r;
    }

    @Override
    public ResultEntity<AppraisalRules> insert(HttpServletRequest request, AppraisalRules entity) {
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        this.service.insert(entity);
        archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        return ResultEntity.success(entity);
    }

    @RequestMapping({"/getWordGroupByBatchId"})
    public ResultEntity getWordGroupByBatchId(String batchId) {
        return ResultEntity.success();
    }

}
