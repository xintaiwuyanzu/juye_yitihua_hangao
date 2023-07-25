package com.dr.archive.manage.form.service.impl;

import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.orm.database.TableRelationLoadEvent;
import com.dr.framework.core.orm.jdbc.Column;
import com.dr.framework.core.orm.jdbc.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * @Author: caor
 * @Date: 2023-07-25 0:03
 * @Description:
 */
@Component
public class FormRelationModify implements ApplicationListener<TableRelationLoadEvent> {
    @Autowired
    @Lazy
    FormDefinitionService formDefinitionService;
    static Field aliasField = ReflectionUtils.findField(Column.class, "alias");

    @Override
    public void onApplicationEvent(TableRelationLoadEvent event) {
        if (event.getRelation().getName().startsWith("f_")) {
            aliasField.setAccessible(true);
            String formCode = event.getRelation().getName().replace("f_", "");
            FormModel formModel = formDefinitionService.selectFormDefinitionByCode(formCode);
            Relation<Column> relation = event.getRelation();
            if (formModel != null) {
                for (Column column : relation.getColumns()) {
                    FieldModel fieldModel = getFieldByColumnName(column.getName(), formModel);
                    if (fieldModel != null) {
                        ReflectionUtils.setField(aliasField, column, fieldModel.getFieldCode());
                    }
                }
            }
        }
    }

    FieldModel getFieldByColumnName(String columnName, FormModel formModel) {
        for (FormField field : formModel.getFields()) {
            if (field.getFieldCode().equalsIgnoreCase(columnName)) {
                return field;
            }
            if (StringUtils.hasText(field.getFieldAliasStr()) && field.getFieldAliasStr().equalsIgnoreCase(columnName)) {
                return field;
            }
        }
        return null;
    }

}
