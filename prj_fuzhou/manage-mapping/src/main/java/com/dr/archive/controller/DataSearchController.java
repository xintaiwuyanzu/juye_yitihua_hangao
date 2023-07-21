package com.dr.archive.controller;

import com.dr.archive.entity.SmartSearchHistory;
import com.dr.archive.service.SmartSearchHistoryService;
import com.dr.archive.service.TripletDataService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.jdbc.Column;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-06-23 09:56
 **/
@RestController
@RequestMapping("api/search")
public class DataSearchController {

    @Autowired
    FormDataService formDataService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    TripletDataService tripletDataService;
    @Autowired
    SmartSearchHistoryService smartSearchHistoryService;

    @RequestMapping("/smartSearch")
    public ResultEntity smartSearch(String classType, String content, String jsonStr, @RequestParam(value = "index", defaultValue = "0") Integer index,
                                    @RequestParam(value = "size", defaultValue = "15") Integer size) {
        FormDefinitionQuery query = new FormDefinitionQuery();
        query.codeEqual(classType);
        List<? extends FormModel> formData = formDefinitionService.selectFormDefinitionByQuery(query);
        /*FormModel form = formDefinitionService.selectFormDefinitionById(formId);*/
        if (formData.size() == 0) {
            return ResultEntity.error("暂无数据!");
        }
        FormModel form = formData.get(0);
        String formId = form.getId();
        Gson gson = new Gson();
        HashMap<String, String> condition = StringUtils.hasText(jsonStr) ? gson.fromJson(jsonStr, HashMap.class) : new HashMap<>();
        Page<FormData> formDataPage = formDataService.selectPageFormData(formId, (sqlQuery, wrapper) -> {
            String formType = form.getFormType();
            Column name = wrapper.getColumn("NAME");
            /*if (formType.equals("组织")) {
                name = wrapper.getColumn("ORG_NAME");
            } else if (formType.equals("项目") || form.getFormName().equals("项目")) {
                name = wrapper.getColumn("PRO_NAME");
            }*/
            sqlQuery.like(name, content);
            if (condition.containsKey("sex")) {
                sqlQuery.in(wrapper.getColumn("SEX"), condition.get("sex").split(","));
            }
        }, index, size);
        formDataPage.setOther(formId);
        SmartSearchHistory searchHistory = new SmartSearchHistory();
        searchHistory.setClassType(classType);
        searchHistory.setContent(content);
        searchHistory.setJsonStr(jsonStr);
        smartSearchHistoryService.saveHistory(searchHistory);
        return ResultEntity.success(formDataPage);
    }

    @RequestMapping("/detail")
    public ResultEntity detail(String formDefinitionId, String formDataId) {
        return ResultEntity.success(formDataService.selectOneFormData(formDefinitionId, formDataId));
    }


    /**
     * 根据关系数据的对象数据id先找出关系数据，再从对象库找数据
     *
     * @param formDataId 数据id
     * @return
     */
    @RequestMapping("/getDataByFormId")
    public ResultEntity getDataByFormId(String formDataId, String formId) {
        FormData formData = formDataService.selectOneFormData(formId, formDataId);
        return ResultEntity.success(formData);
    }
}
