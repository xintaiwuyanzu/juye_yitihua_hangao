package com.dr.archive.manage.codingscheme.controller;

import com.dr.archive.enums.FilesField;
import com.dr.archive.manage.codingscheme.entity.CodingScheme;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeInfo;
import com.dr.archive.manage.codingscheme.service.CodingSchemeService;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@ResponseBody
@RestController
@RequestMapping("/api/codingscheme")
public class CodingSchemeController extends BaseServiceController<CodingSchemeService, CodingScheme> {

    @Autowired
    FormDataService formDataService;

    @Override
    protected SqlQuery<CodingScheme> buildPageQuery(HttpServletRequest httpServletRequest, CodingScheme scheme) {
        SqlQuery<CodingScheme> sqlQuery = SqlQuery.from(CodingScheme.class)
                .equal(CodingSchemeInfo.BUSINESSID, scheme.getBusinessId());
        if (!StringUtils.isEmpty(scheme.getName())) {
            sqlQuery.like(CodingSchemeInfo.NAME, scheme.getName());
        }
        return sqlQuery;
    }

    @Override
    protected SqlQuery<CodingScheme> buildDeleteQuery(HttpServletRequest request, CodingScheme entity) {
        String aids = request.getParameter("aIds");
        Assert.isTrue(!StringUtils.isEmpty(aids), "删除的Id不能为空!");
        return SqlQuery.from(CodingScheme.class, false)
                .column(CodingSchemeInfo.ID)
                .in(CodingSchemeInfo.ID, aids.split(","));
    }

    @RequestMapping("/getFieldCheck")
    public ResultEntity getFieldCheck(@RequestParam(defaultValue = "false") Boolean fond) {
        FilesField[] values = FilesField.values();
        List<Map> list = new ArrayList<>();
        for (FilesField value : values) {
            Map<String, String> map = new HashMap<>();
            if (!fond && "QUANZONG".equals(value.getCode())) {

            } else {
                map.put("code", value.getCode());
                map.put("name", value.getName());
                list.add(map);
            }

        }
        return ResultEntity.success(list);
    }

    @RequestMapping("/getFields")
    public ResultEntity getFields() {
        Field[] fields = AbstractArchiveEntity.class.getFields();
        List<Map> list = new ArrayList<>();
        for (Field field : fields) {
            Map<String, String> map = new HashMap<>();
            map.put("code", field.getName());
            map.put("name", field.getName());
            list.add(map);
        }
        return ResultEntity.success(list);
    }

    /**
     * @param findId
     * @param index
     * @param size
     * @return
     */
    @PostMapping(value = "/findSchemeByFondId")
    public ResultEntity findSchemeByFondId(String findId,
                                           @RequestParam(defaultValue = "1") String index,
                                           @RequestParam(defaultValue = "15") String size) {
        return ResultEntity.success(service.findSchemeByFondId(findId, Integer.parseInt(index), Integer.parseInt(size)));
    }

    /**
     * 获取当前年的分类的编码方案
     *
     * @param businessId
     * @return
     */
    @PostMapping(value = "/findSchemeByBusinessId")
    public ResultEntity findSchemeByBusinessId(String businessId) {
        return ResultEntity.success(service.findSchemeByBusinessId(businessId));
    }

    /**
     * 删除编码方案同步删除方案项
     *
     * @param ids
     * @return
     */
    @RequestMapping("/deleteByIds")
    public ResultEntity deleteByIds(String ids) {
        FormData formData = formDataService.selectOneFormData("27c4ea03-25a8-4c85-996c-d0d82c412009",
                "f4865503-ef08-4f1c-9778-ea8a79824b4e");
        FormData data = service.builderArchiveCode(formData);
        System.out.println(data);
        return ResultEntity.success(service.deleteByIds(ids));
    }

}
