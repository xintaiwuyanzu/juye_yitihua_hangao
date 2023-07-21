package com.dr.archive.entity.vo;

import com.dr.framework.common.form.core.entity.FormField;

/**
 * 表信息
 *
 * @author: yang
 * @create: 2022-05-30 08:58
 **/
public class MyField extends FormField {
    public MyField() {
        super();
    }

    public MyField(String formDefinitionId, String fieldAlias, String fieldType, Integer length, String label, Integer order) {
        setFormDefinitionId(formDefinitionId);
        setFieldAliasStr(fieldAlias.toLowerCase().replaceAll(" ", "_"));
        setFieldCode(fieldAlias.toUpperCase());
        setFieldTypeStr(fieldType);
        setFieldLength(length);
        setLabel(label);
        setOrder(order);
    }
}
