package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalBasis;
import com.dr.archive.appraisal.entity.AppraisalBasisInfo;
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
 * 鉴定依据管理
 */
@RestController
@RequestMapping("api/appraisalBasis")
public class AppraisalBasisController extends BaseServiceController<AppraisalBasisService,AppraisalBasis> {

    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;
    @Autowired
    AppraisalRulesService appraisalRulesService;
    @Autowired
    AppraisalKeyWordService appraisalKeyWordService;
    @Autowired
    AppraisalSpecialService appraisalSpecialService;

    @Override
    protected SqlQuery<AppraisalBasis> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalBasis appraisalBasis) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBasis.class).equal(AppraisalBasisInfo.ORGID, SecurityHolder.get().currentOrganise().getId())
                .orderBy(AppraisalBasisInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<AppraisalBasis> insert(HttpServletRequest request, AppraisalBasis entity) {
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        this.service.insert(entity);
        archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        return ResultEntity.success(entity);
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AppraisalBasis entity) {
        entity = service.selectById(entity.getId());
        ResultEntity<Boolean> r = super.delete(request,entity);
        if(r.getData()){
            appraisalRulesService.deleteByBasis(entity.getId());
            appraisalKeyWordService.deleteByBasis(entity.getId());
            appraisalSpecialService.deleteByBasis(entity.getId());
            archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        }
        return r;
    }
}
