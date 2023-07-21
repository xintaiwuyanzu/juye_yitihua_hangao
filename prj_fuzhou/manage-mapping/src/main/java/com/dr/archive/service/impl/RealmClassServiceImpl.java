package com.dr.archive.service.impl;

import com.dr.archive.entity.RealmClass;
import com.dr.archive.entity.RealmClassInfo;
import com.dr.archive.entity.vo.MyField;
import com.dr.archive.service.RealmClassService;
import com.dr.archive.util.JsonUtils;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: yang
 * @create: 2022-05-25 11:26
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class RealmClassServiceImpl extends DefaultBaseService<RealmClass> implements RealmClassService {

    final Logger log = LoggerFactory.getLogger(RealmClassService.class);

    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FormDataService formDataService;

    /**
     * 添加表单定义
     *
     * @param fd 表单定义数据
     */
    @Override
    public ResultEntity addForm(String fd) {
        try {
            //将json数据转成对象
            FormDefinition form = JsonUtils.jsonToObject(fd, FormDefinition.class);
            if (StringUtils.hasText(form.getFormTable()) && StringUtils.hasText(form.getFormName())) {
                //表单定义数据处理
                form.setFormTable(form.getFormTable().toLowerCase().replaceAll(" ", "_"));
                form.setFormCode(form.getFormTable());

                //继承基础属性
                Collection<FieldModel> defaultFields = getDefaultFields(form.getFormType(), form.getId(), false);
                //新增表
                FormDefinition definition = (FormDefinition) formDefinitionService.addFormDefinition(form, defaultFields, true);
                if (definition != null) {
                    //添加记录
                    RealmClass r = new RealmClass("", definition.getId(), form.getFormTable(), form.getFormType(), form.getFormName(), "", defaultFields.size(), 0);
                    insert(r);
                    return ResultEntity.success(definition);
                }
            } else {
                return ResultEntity.error("请完善信息！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultEntity.error(e.getMessage());
        }
        return ResultEntity.error("");
    }

    /**
     * 继承基础库属性
     *
     * @param type             模型库类型
     * @param formDefinitionId 表单定义id
     * @param b                是否区分对象库的类型继承属性
     */
    @Override
    public Collection<FieldModel> getDefaultFields(String type, String formDefinitionId, boolean b) {
        Collection<FieldModel> fields = new ArrayList<>();
        //不区分对象类型
        if (!b) {
            MyField name = new MyField(formDefinitionId, "NAME", "STRING", 255, "中文名", 1);
            MyField foreignName = new MyField(formDefinitionId, "FOREIGN_NAME", "STRING", 255, "外文名", 2);
            MyField alias = new MyField(formDefinitionId, "ALIAS", "STRING", 255, "别名", 3);
            MyField status = new MyField(formDefinitionId, "STATUS", "STRING", 1, "状态", 4);
            MyField remarks = new MyField(formDefinitionId, "REMARKS", "STRING", 255, "描述", 30);
            fields.add(name);
            fields.add(foreignName);
            fields.add(alias);
            fields.add(status);
            fields.add(remarks);
        }
        return fields;
    }

    /**
     * 获取对象库数据
     */
    @Override
    public Object getFormData(String formId, Integer pageIndex, Integer pageSize) {
        SqlBuilder sqlBuilder = (sqlQuery, formRelationWrapper) -> {
        };
        return formDataService.selectPageFormData(formId, sqlBuilder, pageIndex == null ? 0 : pageIndex, pageSize);
    }

    @Override
    public void updateForm(FormDefinition formDefinition) {
        //更新表单
        FormDefinition definition = (FormDefinition) formDefinitionService.updateFormDefinitionBaseInfo(formDefinition);
        RealmClass realmClass = this.selectOne(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.FORMID, definition.getId()));
        realmClass.setFormTable(definition.getFormTable());
        realmClass.setFormType(definition.getFormType());
        realmClass.setFormAlias(formDefinition.getFormName());
        //更新记录
        updateById(realmClass);
    }

    /**
     * 添加字段
     */
    @Override
    public void addField(FormField formField) {
        formField.setFieldAliasStr(formField.getFieldAliasStr().toLowerCase().replaceAll(" ", "_"));
        formField.setFieldCode(formField.getFieldAliasStr().toUpperCase());
        Collection<FieldModel> fields = new ArrayList<>();
        fields.add(formField);
        Collection<? extends FieldModel> fieldModels = formDefinitionService.addFieldsWithOutUpdateVersion(formField.getFormDefinitionId(), fields, true);
        if (!fieldModels.isEmpty()) {
            RealmClass realmClass = this.selectOne(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.FORMID, formField.getFormDefinitionId()));
            realmClass.setPropertyNum(realmClass.getPropertyNum() + 1);
            updateById(realmClass);
        }
    }
}
