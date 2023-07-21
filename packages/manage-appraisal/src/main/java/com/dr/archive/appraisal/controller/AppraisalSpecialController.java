package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalSpecial;
import com.dr.archive.appraisal.entity.AppraisalSpecialInfo;
import com.dr.archive.appraisal.service.AppraisalSpecialDetailService;
import com.dr.archive.appraisal.service.AppraisalSpecialService;
import com.dr.archive.appraisal.service.Archive4ToBeAppraisalService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴定依据专题管理
 */
@RestController
@RequestMapping("api/appraisalSpecial")
public class AppraisalSpecialController extends BaseServiceController<AppraisalSpecialService, AppraisalSpecial> {

    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;
    @Autowired
    AppraisalSpecialDetailService appraisalSpecialDetailService;

    @Override
    protected SqlQuery<AppraisalSpecial> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalSpecial appraisalSpecial) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalSpecial.class)
                .like(AppraisalSpecialInfo.SPECIALREMARKS,appraisalSpecial.getSpecialRemarks())
                .equal(AppraisalSpecialInfo.RULESID,appraisalSpecial.getRulesId())
                .orderBy(AppraisalSpecialInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<AppraisalSpecial> insert(HttpServletRequest request, AppraisalSpecial entity) {
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        this.service.insert(entity);
        archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        return ResultEntity.success(entity);
    }

    @Override
    public ResultEntity<AppraisalSpecial> update(HttpServletRequest request, AppraisalSpecial entity) {
        super.update(request,entity);
        archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        return ResultEntity.success(entity);
    }


    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AppraisalSpecial entity) {
        String [] ids = entity.getId().split(",");
        String orgId = "";
        for(String id:ids){
            AppraisalSpecial e = service.selectById(id);
            orgId = e.getOrgId();
            super.delete(request, e);
            appraisalSpecialDetailService.deleteBySpecialId(id);
        }
        archive4ToBeAppraisalService.removeCache(orgId);
        return ResultEntity.success(true);
    }
}
