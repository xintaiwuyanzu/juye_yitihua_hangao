package com.dr.archive.service;

import com.dr.archive.entity.RealmClass;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.service.BaseService;

import java.util.Collection;

/**
 * @author: yang
 * @create: 2022-05-30 10:08
 **/
public interface RealmClassService extends BaseService<RealmClass> {
    ResultEntity addForm(String formDefinition);

    void updateForm(FormDefinition formDefinition);

    void addField(FormField formField);

    Collection<FieldModel> getDefaultFields(String type, String formDefinitionId, boolean b);

    Object getFormData(String formId, Integer pageIndex, Integer pageSize);
}
