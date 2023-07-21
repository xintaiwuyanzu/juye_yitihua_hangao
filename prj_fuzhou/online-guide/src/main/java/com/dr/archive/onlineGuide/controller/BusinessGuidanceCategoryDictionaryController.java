package com.dr.archive.onlineGuide.controller;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceCategoryDictionary;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceCategoryDictionaryInfo;
import com.dr.archive.onlineGuide.service.BusinessGuidanceClassDictionaryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/businessGuidanceCategoryDictionary")
public class BusinessGuidanceCategoryDictionaryController extends BaseServiceController<BusinessGuidanceClassDictionaryService,BusinessGuidanceCategoryDictionary> {

    @Autowired
    BusinessGuidanceClassDictionaryService businessGuidanceClassDictionaryService;

    @Override
    protected SqlQuery<BusinessGuidanceCategoryDictionary> buildPageQuery(HttpServletRequest request, BusinessGuidanceCategoryDictionary entity) {
        SqlQuery.from(BusinessGuidanceCategoryDictionary.class);
        return null;
    }

    @PostMapping("/insertDictionary")
    public ResultEntity insertClass(String cType, String cProblem, String cResult){

      return businessGuidanceClassDictionaryService.insertClass(cType, cProblem, cResult);

    }

    @PostMapping("/queryType")
    public ResultEntity queryType(){

        return businessGuidanceClassDictionaryService.queryType(1);

    }

    @PostMapping("/queryProblem")
    public ResultEntity queryProblem(String type){

        return businessGuidanceClassDictionaryService.queryProblem(type);
    }

    @RequestMapping("/settlement")
    public ResultEntity settlement(String batchId,String classifyId,String classifyType){
        return businessGuidanceClassDictionaryService.classifyBatch(batchId,classifyId,classifyType);
    }

    @RequestMapping("/selectType")
    public ResultEntity selectTYpe(){
        return businessGuidanceClassDictionaryService.queryType(0);
    }
}
