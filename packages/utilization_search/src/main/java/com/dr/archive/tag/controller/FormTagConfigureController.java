package com.dr.archive.tag.controller;

import com.dr.archive.tag.entity.FormTagConfigure;
import com.dr.archive.tag.entity.FormTagConfigureInfo;
import com.dr.archive.tag.service.FormTagConfigureService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/3/28 14:53
 */
@RestController
@RequestMapping("api/formTagConfigure")
public class FormTagConfigureController extends BaseServiceController<FormTagConfigureService, FormTagConfigure> {
    @Autowired
    FormDefinitionService formDefinitionService;
    @Override
    protected SqlQuery<FormTagConfigure> buildPageQuery(HttpServletRequest request, FormTagConfigure entity) {
        SqlQuery<FormTagConfigure> sqlQuery = SqlQuery.from(FormTagConfigure.class);
        if(!StringUtils.isEmpty(entity.getFormDefinitionCode())){
            sqlQuery.like(FormTagConfigureInfo.FORMDEFINITIONCODE, entity.getFormDefinitionCode());
        }
        if(!StringUtils.isEmpty(entity.getFieldCode())){
            sqlQuery.like(FormTagConfigureInfo.FIELDCODE, entity.getFieldCode());
        }
        sqlQuery.orderByDesc(FormTagConfigureInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping({"/insert"})
    public ResultEntity<FormTagConfigure> insert(HttpServletRequest request, FormTagConfigure entity) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(entity.getFormDefinitionId());
        entity.setFormDefinitionCode(formModel.getFormCode());
        entity.setFormDefinitionName(formModel.getFormName());
        FieldModel fieldModel = formDefinitionService.selectFieldByCode(entity.getFormDefinitionId(), entity.getFieldCode());
        entity.setLabel(fieldModel.getLabel());
        this.service.insert(entity);
        return ResultEntity.success(entity);
    }
}
