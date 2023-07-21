package com.dr.archive.service.impl;

import com.dr.archive.entity.RealmClass;
import com.dr.archive.entity.RealmClassInfo;
import com.dr.archive.entity.Relation;
import com.dr.archive.service.RealmClassService;
import com.dr.archive.service.RelationService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-05-25 11:26
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class RelationServiceImpl extends DefaultBaseService<Relation> implements RelationService {
    @Autowired
    RealmClassService realmClassService;
    @Autowired
    FormDefinitionService formDefinitionService;

    @Override
    public Object getForms() {
        List<RealmClass> realmClassList = realmClassService.selectList(SqlQuery.from(RealmClass.class));
        ArrayList<Object> list = new ArrayList<>();
        realmClassList.forEach(i -> {
            FormModel formModel = formDefinitionService.selectFormDefinitionById(i.getFormId());
            list.add(formModel);
        });
        return list;
    }

    @Override
    public void updateRelationNum(Relation entity) {
        String[] arr = new String[]{"人", "事", "物", "地", "组织"};
        FormModel sourceModel = formDefinitionService.selectFormDefinitionById(entity.getSourceFormId());
        FormModel targetModel = formDefinitionService.selectFormDefinitionById(entity.getTargetFormId());
        List<RealmClass> realmClasses = null;
        for (String i : arr) {
            if (sourceModel.getFormType().equals(i) || targetModel.getFormType().equals(i)) {
                realmClasses = realmClassService.selectList(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.FORMALIAS, i));
                break;
            }
        }
        realmClasses.forEach(i -> {
            i.setRelationNum(i.getRelationNum() + 1);
            realmClassService.updateById(i);
        });
    }
}
