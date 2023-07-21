package com.dr.archive.controller;

import com.dr.archive.entity.Relation;
import com.dr.archive.entity.RelationInfo;
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


/**
 * @author: yang
 * @create: 2022-05-30 16:49
 **/
@RestController
@RequestMapping("/api/relation")
public class RelationController extends BaseServiceController<RelationService, Relation> {
    @Autowired
    RealmClassService realmClassService;
    @Autowired
    RelationService relationService;
    @Autowired
    FormDefinitionService formDefinitionService;

    @Override
    protected SqlQuery<Relation> buildPageQuery(HttpServletRequest httpServletRequest, Relation relation) {
        return SqlQuery.from(Relation.class)
                .like(RelationInfo.SOURCENAME, relation.getSourceName())
                .like(RelationInfo.TARGETNAME, relation.getTargetName())
                .like(RelationInfo.RELATIONNAME, relation.getRelationName());
    }

    @Override
    public ResultEntity insert(HttpServletRequest request, Relation entity) {
        FormModel sourceModel = formDefinitionService.selectFormDefinitionById(entity.getSourceFormId());
        FormModel targetModel = formDefinitionService.selectFormDefinitionById(entity.getTargetFormId());
        entity.setSourceName(sourceModel.getFormName());
        entity.setTargetName(targetModel.getFormName());
        super.insert(request, entity);
        updateRelationNum(entity);
        return ResultEntity.success();
    }

    @Override
    public ResultEntity update(HttpServletRequest request, Relation entity) {
        FormModel sourceModel = formDefinitionService.selectFormDefinitionById(entity.getSourceFormId());
        FormModel targetModel = formDefinitionService.selectFormDefinitionById(entity.getTargetFormId());
        entity.setSourceName(sourceModel.getFormName());
        entity.setTargetName(targetModel.getFormName());
        updateRelationNum(entity);
        super.update(request, entity);
        return ResultEntity.success();
    }

    /**
     * 根据源对象和目标对象类型获取关系
     */
    @RequestMapping("/getRelations")
    public ResultEntity getRelations(String source, String target) {
        return ResultEntity.success(service.selectList(SqlQuery.from(Relation.class)
                .equal(RelationInfo.SOURCEFORMID, source)
                .equal(RelationInfo.TARGETFORMID, target)
                .orNew()
                .equal(RelationInfo.SOURCEFORMID, target).equal(RelationInfo.TARGETFORMID, source)
        ));
    }

    /**
     * 更新对象的关系数量
     */
    private void updateRelationNum(Relation entity) {
        service.updateRelationNum(entity);
    }

    @RequestMapping("/getForms")
    public ResultEntity getForms() {
        return ResultEntity.success(service.getForms());
    }
}
