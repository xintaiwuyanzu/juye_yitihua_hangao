package com.dr.archive.controller;

import com.dr.archive.entity.DataResource;
import com.dr.archive.entity.DataResourceInfo;
import com.dr.archive.service.DataResourceService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-06-10 17:30
 **/
@RequestMapping("/api/dataResource")
@RestController
public class DataResourceController extends BaseServiceController<DataResourceService, DataResource> {
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FormDataService formDataService;

    @Override
    protected SqlQuery<DataResource> buildPageQuery(HttpServletRequest httpServletRequest, DataResource dataResource) {
        return SqlQuery.from(DataResource.class)
                .like(DataResourceInfo.FORMNAME, dataResource.getFormName())
                .equal(DataResourceInfo.STATUS, dataResource.getStatus())
                .equal(DataResourceInfo.FORMID, dataResource.getFormId());
    }

    /**
     * 统计
     */
    @RequestMapping("/loadData")
    public ResultEntity loadData() {
        List<? extends FormModel> formModels = formDefinitionService.selectFormDefinitionByQuery(new FormDefinitionQuery().typeEqual("0"));
        formModels.forEach(i -> {
            Long countId = formDataService.countId(i.getId(), (sqlQuery, formRelationWrapper) -> {
            });
            DataResource dr = new DataResource(i.getId(), i.getFormName(), countId, i.getFormCode(), "");
            DataResource selectOne = service.selectOne(SqlQuery.from(DataResource.class).equal(DataResourceInfo.FORMID, i.getId()));
            if (selectOne == null) {
                dr.setStatus("0");
                dr.setDataNum(countId);
                service.insert(dr);
            } else {
                selectOne.setDataNum(countId);
                service.updateById(selectOne);
            }
        });
        return ResultEntity.success();
    }

    /**
     * 查看元数据
     */
    @RequestMapping("/getFormDataId")
    public ResultEntity<List<FormData>> getFormDataId(String formId) {
        List<FormData> formData = formDataService.selectFormData(formId, (sqlQuery, formRelationWrapper) -> {
        });
        return ResultEntity.success(formData);
    }

    /**
     * 标注
     */
    @RequestMapping("/mark")
    public ResultEntity<FormDefinition> mark(String formId) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formId);
        //获取所有字段
        Collection<FormField> fields = formModel.getFields();
        List<FormField> formFields = new ArrayList<>(fields);
        //对字段排序
        formFields.sort(new Comparator<FormField>() {
            @Override
            public int compare(FormField t, FormField t1) {
                return t.getFieldOrder() - t1.getFieldOrder();
            }
        });
        //重新构建表单对象
        FormDefinition formDefinition = new FormDefinition(formModel);
        formDefinition.setFields(formFields);
        return ResultEntity.success(formDefinition);
    }

    /**
     * 数据标注任务
     */
    @RequestMapping("/transform")
    public ResultEntity<Long> transformPre(String result) {
        return ResultEntity.success(service.transformPre(result));
    }

    /**
     * 关系梳理任务
     */
    @RequestMapping("/saveRelationResult")
    public ResultEntity<Long> saveRelationResultPre(String result) {
        return ResultEntity.success(service.saveRelationResultPre(result));
    }
}
