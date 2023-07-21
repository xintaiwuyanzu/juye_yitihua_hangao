package com.dr.archive.controller;

import com.dr.archive.entity.RealmClass;
import com.dr.archive.entity.RealmClassInfo;
import com.dr.archive.entity.TripletData;
import com.dr.archive.entity.TripletDataInfo;
import com.dr.archive.service.RealmClassService;
import com.dr.archive.service.RelationService;
import com.dr.archive.service.TripletDataService;
import com.dr.archive.util.JsonUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FieldType;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: yang
 * @create: 2022-05-25 11:25
 **/
@RestController
@RequestMapping("/api/realm_class")
public class RealmClassController extends BaseServiceController<RealmClassService, RealmClass> {

    @Autowired
    RealmClassService realmClassService;
    @Autowired
    RelationService relationService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    TripletDataService tripletDataService;

    @Override
    protected SqlQuery<RealmClass> buildPageQuery(HttpServletRequest httpServletRequest, RealmClass realmClass) {
        reload();
        return SqlQuery.from(RealmClass.class).equal(RealmClassInfo.REALMID, realmClass.getRealmId());
    }

    /**
     * 获取所有对象
     */
    @RequestMapping("/getAllClass")
    public ResultEntity getAllClass(String realmId) {
        List<RealmClass> realmClasses = service.selectList(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.REALMID, realmId));
        return ResultEntity.success(realmClasses);
    }

    /**
     * 定义表结构
     */
    @RequestMapping("/addForm")
    public ResultEntity addForm(String formDefinition) {
        return realmClassService.addForm(formDefinition);
    }

    /**
     * 保存表
     */
    @RequestMapping("/updateForm")
    public ResultEntity updateForm(FormDefinition formDefinition) {
        realmClassService.updateForm(formDefinition);
        return ResultEntity.success();
    }

    /**
     * 获取字段类型
     */
    @RequestMapping("/getColumnTypes")
    public ResultEntity<Object> getColumnTypes() {
        return ResultEntity.success(FieldType.values());
    }

    /*
     *查找表定义数据
     */
    @RequestMapping("/getFormMsg")
    public ResultEntity<Object> getFormMsg(String formId) {
        if (StringUtils.hasText(formId)) {
            return ResultEntity.success(formDefinitionService.selectFormDefinitionById(formId));
        } else {
            List<RealmClass> classes = service.selectList(SqlQuery.from(RealmClass.class));
            ArrayList<Object> list = new ArrayList<>();
            classes.forEach(i -> {
                String id = i.getFormId();
                FormModel formModel = formDefinitionService.selectFormDefinitionById(id);
                list.add(formModel);
            });
            return ResultEntity.success(list);
        }
    }

    /**
     * 添加字段
     */
    @RequestMapping("/addField")
    public ResultEntity addField(FormField formField) {
        if (StringUtils.hasText(formField.getFormDefinitionId())) {
            FormModel formModel = formDefinitionService.selectFormDefinitionById(formField.getFormDefinitionId());
            formField.setFormDefinitionCode(formModel.getFormCode());

            try {
                realmClassService.addField(formField);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultEntity.error(e.getMessage());
            }
            return ResultEntity.success();
        } else {
            return ResultEntity.error("对象不存在，请先创建对象！");
        }
    }

    /**
     * 修改字段
     */
    @RequestMapping("/updateField")
    public ResultEntity<Object> updateField(FormField formField) {
        return ResultEntity.success(formDefinitionService.changeField(formField.getFormDefinitionId(), formField, true));
    }

    /**
     * 删除字段
     */
    @RequestMapping("/deleteField")
    public ResultEntity deleteField(String formDefinitionId, String fieldCode) {
        FieldModel field = formDefinitionService.removeField(formDefinitionId, fieldCode);
        if (field != null) {
            return ResultEntity.success(field);
        } else {
            return ResultEntity.error("删除失败！");
        }
    }

    /**
     * 获取库的默认属性
     */
    @RequestMapping("/getDefaultFields")
    public ResultEntity<Object> getDefaultFields(String type) {
        return ResultEntity.success(service.getDefaultFields(type, null, false));
    }

    /**
     * 获取对象库数据
     */
    @RequestMapping("/getFormData")
    public ResultEntity<Object> getFormData(@RequestParam("formId") String formId, Integer pageIndex, Integer pageSize) {
        return ResultEntity.success(service.getFormData(formId, pageIndex, pageSize));
    }

    /**
     * 从其他表更新当前表数据
     */
    @RequestMapping("/reload")
    public ResultEntity reload() {
        List<RealmClass> classes = service.selectList(SqlQuery.from(RealmClass.class));
        classes.forEach(i -> {
            //统计关系数量
            String classType = i.getFormAlias();
            List<TripletData> relations = tripletDataService.selectList(SqlQuery.from(TripletData.class).equal(TripletDataInfo.SOURCEFORMNAME, classType).or().equal(TripletDataInfo.TARGETFORMNAME, classType));
            i.setRelationNum(relations.size());
            //统计属性数量
            String formId = i.getFormId();
            FormModel formModel = formDefinitionService.selectFormDefinitionById(formId);
            i.setPropertyNum(formModel.getFields().size());
            //统计数据数量
            Long dataNum = formDataService.countId(i.getFormId(), (sqlQuery, formRelationWrapper) -> {
            });
            i.setDataNum(Math.toIntExact(dataNum));
            //更新
            service.updateById(i);
        });
        return ResultEntity.success();
    }

    /**
     * 搜索属性
     */
    @RequestMapping("/getFields")
    public ResultEntity getFields(String formId, String label) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formId);
        Collection<FormField> fields = formModel.getFields();
        Collection<FormField> fs = new ArrayList<>();
        if (StringUtils.hasText(label)) {
            for (FormField field : fields) {
                if (field.getLabel().contains(label)) {
                    fs.add(field);
                }
            }
        } else {
            fs = fields;
        }
        return ResultEntity.success(fs);
    }

    @Override
    public ResultEntity<RealmClass> update(HttpServletRequest request, RealmClass entity) {
        RealmClass byId = service.selectById(entity.getId());
        String realmId = byId.getRealmId();
        if (!StringUtils.hasText(realmId)) {
            byId.setRealmId(entity.getRealmId());
        } else if (!realmId.contains(entity.getRealmId())) {
            ArrayList<String> list = new ArrayList<>();
            list.add(entity.getRealmId());
            list.addAll(Arrays.asList(realmId.split(",")));
            String s = String.join(",", list);
            byId.setRealmId(s);
        }
        realmId = byId.getRealmId();
        if (StringUtils.hasText(realmId)) {
            HashMap<Object, Object> map = new HashMap<>();
            for (String s : realmId.split(",")) {
                map.put(s, entity.getRealmNode());
            }
            String json = new Gson().toJson(map);
            byId.setRealmNode(json);
        }
        return super.update(request, byId);
    }

    @RequestMapping("/releaseClass")
    public ResultEntity releaseClass(String id, String realmId) {
        RealmClass byId = service.selectById(id);
        List<String> list = new ArrayList<>(Arrays.asList(byId.getRealmId().split(",")));
        try {
            Map map = JsonUtils.jsonToObject(byId.getRealmNode(), Map.class);
            boolean b = false;
            if (list.contains(realmId)) {
                b = true;
            }
            if (b) {
                list.remove(realmId);
                map.remove(realmId);
            }
            byId.setRealmId(String.join(",", list));
            byId.setRealmNode(new Gson().toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultEntity.success(service.updateById(byId));
    }
}
