package com.dr.archive.onlineGuide.controller;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceClassifiBatch;
import com.dr.archive.onlineGuide.service.BusinessGuidanceClassifiBatchService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/businessGuidanceClassifi")
public class BusinessGuidanceClassifiBatchController extends BaseServiceController<BusinessGuidanceClassifiBatchService, BusinessGuidanceClassifiBatch> {

    @Autowired
    BusinessGuidanceClassifiBatchService businessGuidanceClassifiBatchService;

    @Override
    protected SqlQuery<BusinessGuidanceClassifiBatch> buildPageQuery(HttpServletRequest request, BusinessGuidanceClassifiBatch entity) {

        return SqlQuery.from(BusinessGuidanceClassifiBatch.class);
    }

    @RequestMapping("/settlement")
    public ResultEntity settlement(String batchId,String dId){
        String s = businessGuidanceClassifiBatchService.insertClass(batchId, dId);
        return ResultEntity.success(s);
    }
}
