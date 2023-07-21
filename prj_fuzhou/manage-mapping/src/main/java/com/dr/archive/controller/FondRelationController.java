package com.dr.archive.controller;

import com.dr.archive.entity.FondRelation;
import com.dr.archive.entity.FondRelationInfo;
import com.dr.archive.entity.Relation;
import com.dr.archive.entity.RelationInfo;
import com.dr.archive.service.FondRelationService;
import com.dr.archive.service.RealmClassService;
import com.dr.archive.service.RelationService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/api/fondRelation")
public class FondRelationController extends BaseServiceController<FondRelationService, FondRelation> {


    @Override
    protected SqlQuery<FondRelation> buildPageQuery(HttpServletRequest httpServletRequest, FondRelation fondRelation) {
        return SqlQuery.from(FondRelation.class);
    }

  @RequestMapping("getRelationById")
    public ResultEntity getRelationById(String FondId){
      SqlQuery<FondRelation> sql = SqlQuery.from(FondRelation.class).equal(FondRelationInfo.FONDID, FondId);
        return ResultEntity.success(service.selectList(sql));
  }
}
