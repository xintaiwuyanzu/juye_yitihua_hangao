package com.dr.archive.manage.form.service.impl;


import com.dr.archive.enums.CategoryType;
import com.dr.archive.enums.KindType;
import com.dr.archive.event.ArchiveFormCreateEvent;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.display.entity.FieldDisplayScheme;
import com.dr.framework.common.form.display.entity.FormDisplayScheme;
import com.dr.framework.common.form.display.service.FormDisplayService;
import com.dr.framework.common.form.engine.CommandExecutor;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.form.engine.model.display.FieldDisplay;
import com.dr.framework.common.form.engine.model.display.FormDisplay;
import com.dr.framework.common.form.schema.model.JsonSchema;
import com.dr.framework.common.form.util.Constants;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultDataBaseService;
import com.dr.framework.core.orm.database.Dialect;
import com.dr.framework.core.orm.database.tools.AnnotationTableReaderUtil;
import com.dr.framework.core.orm.module.EntityRelation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/7/29 13:49
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Order(Ordered.LOWEST_PRECEDENCE - 2)
public class ArchiveFormDefinitionServiceImpl implements ArchiveFormDefinitionService, InitializingBean {
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FormDisplayService formDisplayService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    protected CommandExecutor commandExecutor;
    @Autowired
    DefaultDataBaseService dataBaseService;

    Map<Class, EntityRelation> archiveParentEntityMap;

    //TODO 需要修改
    @Override
    public FormDefinition addForm(FormDefinition formDefinition) {
        //TODO 判断表单类型
        List<FieldModel> fieldModels = getDefaultFields(formDefinition);
        FormDefinition definition = (FormDefinition) formDefinitionService.addFormDefinition(formDefinition, fieldModels, true);
        this.addFormDisplay(this.findFieldList(definition.getId()), definition);
        //创建表单时发布事件
        applicationEventPublisher.publishEvent(new ArchiveFormCreateEvent(definition));
        return formDefinition;
    }

    /**
     * 添加默认表单显示方案
     *
     * @param formFieldList  新添加的字段
     * @param formDefinition
     */
    @Override
    public void addFormDisplay(List<FormField> formFieldList, FormDefinition formDefinition) {
        FormDisplayScheme formDisplayScheme = new FormDisplayScheme();
        formDisplayScheme.setLabelWidth(100);
        String id = UUIDUtils.getUUID();
        formDisplayScheme.setId(id);
        formDisplayScheme.setName("添加");
        formDisplayScheme.setType("form");
        formDisplayScheme.setCode("form");
        formDisplayScheme.setFormDefinitionId(formDefinition.getId());
        formDisplayService.insert(formDisplayScheme);
        addFormDisplayItem(formFieldList, formDisplayScheme);
        id = UUIDUtils.getUUID();
        formDisplayScheme.setId(id);
        formDisplayScheme.setName("列表");
        formDisplayScheme.setType("list");
        formDisplayScheme.setCode("list");
        formDisplayScheme.setFormDefinitionId(formDefinition.getId());
        formDisplayService.insert(formDisplayScheme);
        addFormDisplayItem(formFieldList, formDisplayScheme);
        id = UUIDUtils.getUUID();
        formDisplayScheme.setId(id);
        formDisplayScheme.setName("查询");
        formDisplayScheme.setType("search");
        formDisplayScheme.setCode("search");
        formDisplayScheme.setFormDefinitionId(formDefinition.getId());
        formDisplayService.insert(formDisplayScheme);
        addFormDisplayItem(formFieldList, formDisplayScheme);
    }

    @Override
    public FormDefinition createTable(String formDefinitionId) {
        return (FormDefinition) formDefinitionService.checkAndGenTable(formDefinitionId);
    }

    @Autowired
    FormNameGenerator formNameGenerator;

    @Override
    public boolean tableExist(String formDefinitionId) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        return dataBaseService.tableExist(formNameGenerator.genTableName(formModel), Constants.MODULE_NAME);
    }

    /**
     * 添加默认字段显示方案
     *
     * @param formDisplayScheme
     */
    public void addFormDisplayItem(List<FormField> newFormFieldList, FormDisplayScheme formDisplayScheme) {
        Assert.isTrue(newFormFieldList.size() > 0, "新增字段不能为空！");
        //获取所有字段
        List<FormField> formFieldList = this.findFieldList(formDisplayScheme.getFormDefinitionId());
        //默认去掉的显示方案
//        String[] removeFields = {"CREATEDATE", "CREATEPERSON", "UPDATEDATE", "UPDATEPERSON", "PERSONNAME", ArchiveEntity.COLUMN_WZ, ArchiveEntity.COLUMN_SUB_STATUS, ArchiveEntity.STATUS_COLUMN_KEY.toUpperCase(), ArchiveEntity.COLUMN_NOTE, ArchiveEntity.COLUMN_SOURCE_ID, ArchiveEntity.COLUMN_SOURCE_TYPE, ArchiveEntity.COLUMN_ORG_CODE, ArchiveEntity.COLUMN_CATEGORY_CODE, ArchiveEntity.ORDER_COLUMN_NAME.toUpperCase(), ArchiveEntity.COLUMN_OPEN_SCOPE, ArchiveEntity.COLUMN_AJH, ArchiveEntity.COLUMN_HH, ArchiveEntity.COLUMN_CATALOGUE_CODE, ArchiveEntity.COLUMN_KEY_WORDS, ArchiveEntity.COLUMN_DUTY_PERSON, ArchiveEntity.COLUMN_SECURITY_LEVEL, ArchiveEntity.COLUMN_WJFS, ArchiveEntity.COLUMN_YH, ArchiveEntity.COLUMN_YW_HAVE, ArchiveEntity.COLUMN_WJLX, ArchiveEntity.COLUMN_MLDM, ArchiveEntity.COLUMN_ORGANISEID, ArchiveEntity.COLUMN_BQ, ArchiveEntity.COLUMN_IS_TRANSFER, ArchiveEntity.COLUMN_FILE_COUNTS, ArchiveEntity.COLUMN_SAVE_APPRAISAL_DATE, ArchiveEntity.COLUMN_OPEN_APPRAISAL_DATE};
        String[] addFields = {ArchiveEntity.COLUMN_TITLE, ArchiveEntity.COLUMN_ARCHIVE_CODE, ArchiveEntity.COLUMN_NOTE, ArchiveEntity.COLUMN_SAVE_TERM, ArchiveEntity.COLUMN_YEAR, ArchiveEntity.COLUMN_FILETIME};
        //获取老的表单方案,newFormFieldList中字段为新添加的但是表单id为老的
        FormDisplay oldFormDisplay = formDisplayService.getFormDisplay(newFormFieldList.get(0).getFormDefinitionId(), formDisplayScheme.getType(), formDisplayScheme.getCode());

        List<FieldDisplayScheme> oldFieldDisplaySchemeList = (List<FieldDisplayScheme>) oldFormDisplay.getFields();
        //复制上一版本的表单方案
        oldFieldDisplaySchemeList.forEach(oldFieldDisplayScheme -> {
            FieldDisplayScheme fieldDisplayScheme = new FieldDisplayScheme();
            fieldDisplayScheme.setFormDisplayId(formDisplayScheme.getId());
            fieldDisplayScheme.setOrder(oldFieldDisplayScheme.getOrder());
            fieldDisplayScheme.setCode(oldFieldDisplayScheme.getCode());
            fieldDisplayScheme.setName(oldFieldDisplayScheme.getName());
            fieldDisplayScheme.setType(oldFieldDisplayScheme.getType());
            fieldDisplayScheme.setRemarks(oldFieldDisplayScheme.getRemarks());
            fieldDisplayScheme.setLabelWidth(oldFieldDisplayScheme.getLabelWidth());
            fieldDisplayScheme.setDescription(oldFieldDisplayScheme.getDescription());
            formDisplayService.insertField(fieldDisplayScheme);
        });
        //新添加的字段初始默认值
        newFormFieldList.forEach(newFormField -> {
            if (!ArrayUtils.contains(addFields, newFormField.getFieldCode().toUpperCase())) {
            } else {
                FieldDisplayScheme fieldDisplayScheme = new FieldDisplayScheme();
                fieldDisplayScheme.setFormDisplayId(formDisplayScheme.getId());
                fieldDisplayScheme.setOrder(newFormField.getOrder());
                fieldDisplayScheme.setCode(newFormField.getFieldCode());
                fieldDisplayScheme.setName(newFormField.getLabel());
                fieldDisplayScheme.setType("input");
                fieldDisplayScheme.setLabelWidth(100);
                if ("list".equals(formDisplayScheme.getType()) && !ArchiveEntity.COLUMN_TITLE.equalsIgnoreCase(newFormField.getFieldCode())) {
                    fieldDisplayScheme.setRemarks("100");
                } else if ("form".equals(formDisplayScheme.getType()) && (ArchiveEntity.COLUMN_TITLE.equalsIgnoreCase(newFormField.getFieldCode()) || ArchiveEntity.COLUMN_NOTE.equalsIgnoreCase(newFormField.getFieldCode()))) {
                    fieldDisplayScheme.setRemarks("510");
                } else {
                    fieldDisplayScheme.setRemarks("200");
                }
                formDisplayService.insertField(fieldDisplayScheme);
            }
        });
    }

    /**
     * 获取所有档案表都有的字段
     *
     * @param formDefinition
     * @return
     */
    private List<FieldModel> getDefaultFields(FormDefinition formDefinition) {
        CategoryType categoryType = CategoryType.from(formDefinition.getFormType());
        Class clz;
        switch (categoryType) {
            case PRO:
                clz = KindType.OTHER.getProBaseClass();
                break;
            case ARC:
                clz = KindType.Document.getArcBaseClass();
                break;
            case FILE:
                clz = KindType.OTHER.getFileBaseClass();
                break;
            case BOX:
                clz = KindType.OTHER.getBoxBaseClass();
                break;
            default:
                clz = KindType.OTHER.getArcBaseClass();
                break;
        }
        EntityRelation entityRelation = archiveParentEntityMap.get(clz);
        return entityRelation.getColumns().stream().filter(f -> !f.getName().equalsIgnoreCase(IdEntity.ID_COLUMN_NAME)).map(ColumnFieldModel::new).collect(Collectors.toList());
    }

    /**
     * 表单id必须传值，不传值则无法将上一版本的方案带入下一版本
     *
     * @param formField
     * @return
     */
    @Override
    public FormField addField(FormField formField) {
        FormField newFormField = (FormField) formDefinitionService.addField(formField.getFormDefinitionId(), formField, true);
        List<FormField> fieldList = new ArrayList<>();
        fieldList.add(formField);
        addFormDisplay(fieldList, (FormDefinition) formDefinitionService.selectFormDefinitionById(newFormField.getFormDefinitionId()));
        return newFormField;
    }

    /**
     * 表单id必须传值，不传值则无法将上一版本的方案带入下一版本
     *
     * @param formFieldList
     * @return
     */
    @Override
    public List<FormField> addFieldList(List<FormField> formFieldList) {
        Assert.isTrue(formFieldList.size() > 0, "字段信息为空");
        List<FieldModel> fieldModelList = new ArrayList<>(formFieldList);
        List<FormField> newFormFieldList = (List<FormField>) formDefinitionService.addFieldsWithOutUpdateVersion(formFieldList.get(0).getFormDefinitionId(), formFieldList, true);

        // commandExecutor.execute(new FormDefinitionFieldListAddCommand(formFieldList.get(0).getFormDefinitionId(), true, true, fieldModelList));
        addFormDisplay(formFieldList, (FormDefinition) formDefinitionService.selectFormDefinitionById(newFormFieldList.get(0).getFormDefinitionId()));
        return newFormFieldList;
    }

    @Override
    public FormDefinition updateForm(FormDefinition formDefinition) {
        return (FormDefinition) formDefinitionService.updateFormDefinitionBaseInfo(formDefinition);
    }

    @Override
    public FormField updateField(FormField formField) {
        return (FormField) formDefinitionService.changeField(formField.getFormDefinitionId(), formField, true);
    }

    /**
     * 1、删除显示方案
     *
     * @param formId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteForm(String formId) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formId);
        //TODO 发布表单删除事件
        //applicationEventPublisher.publishEvent(new ArchiveFormDeleteEvent(formModel));
        List<FormDisplay> formDisplayList = findDisplayList(formId);
        formDisplayList.forEach(formDisplay -> deleteFormDisplay(formDisplay.getId()));
        return formDefinitionService.removeFormDefinitionById(formId, true);
    }

    @Override
    public FormField deleteField(String formDefinitionId, String fieldCode) {
        return (FormField) formDefinitionService.removeField(formDefinitionId, fieldCode);
    }

    @Override
    public Page<? extends FormModel> findFormPage(FormDefinitionQuery formDefinitionQuery, int index, int size) {
        return formDefinitionService.selectPageFormDefinition(formDefinitionQuery, index, size);
    }

    @Override
    public List<? extends FormModel> findFormList(FormDefinitionQuery formDefinitionQuery) {
        return formDefinitionService.selectFormDefinitionByQuery(formDefinitionQuery);
    }

    @Override
    public List<FormField> findFieldList(String formDefinitionId) {
        FormDefinition formDefinition = (FormDefinition) formDefinitionService.selectFormDefinitionById(formDefinitionId);
        return formDefinition.getFields();
    }

    @Override
    public List<FormDisplay> findDisplayList(String formDefinitionId) {
        return formDisplayService.getFormDisplay(formDefinitionId);
    }

    @Override
    public FormDisplay addDisplay(FormDisplay formDisplay) {
        return formDisplayService.insert(formDisplay, true);
    }

    @Override
    public long deleteFormDisplay(String formDisplayId) {
        return formDisplayService.deleteFormDisplay(formDisplayId, true);
    }

    @Override
    public FieldDisplay addFieldDisplay(FieldDisplay fieldDisplay, Map<String, String> metaMap) {
        fieldDisplay = formDisplayService.insertField(fieldDisplay, true);
        if (metaMap != null) {
            formDisplayService.setFieldMeta(fieldDisplay.getFormDisplayId(), fieldDisplay.getCode(), metaMap);
        }
        return fieldDisplay;
    }

    @Override
    public FieldDisplay updateFieldDisplay(FieldDisplay fieldDisplay, Map<String, String> metaMap) {
        fieldDisplay = formDisplayService.updateField(fieldDisplay, true);
        if (metaMap != null) {
            formDisplayService.setFieldMeta(fieldDisplay.getFormDisplayId(), fieldDisplay.getCode(), metaMap);
        }
        return fieldDisplay;
    }

    @Override
    public long deleteFieldDisplay(String fieldId) {
        return formDisplayService.deleteField(fieldId, true);
    }

    @Override
    public FormDisplay updateDisplay(FormDisplay formDisplay) {
        return formDisplayService.update(formDisplay, true);
    }

    @Override
    public FormDisplay selectFormDisplay(String formDisplayId) {
        return formDisplayService.getFormDisplayById(formDisplayId);
    }

    @Override
    public FormDisplay selectFormDisplay(String formDefinitionId, String formType, String formCode) {
        return formDisplayService.getFormDisplay(formDefinitionId, formType, formCode);
    }

    @Override
    public JsonSchema jsonSchema(String formDisplayId) {
        JsonSchema jsonSchema = new JsonSchema();
        FormDisplay formDisplay = formDisplayService.getFormDisplayById(formDisplayId);
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDisplay.getFormDefinitionId());
        jsonSchema.setFormDisplay(formDisplay);
        jsonSchema.setFormModel(formModel);
        return jsonSchema;
    }

    /**
     * 缓存所有门类的默认表结构
     */
    @Override
    public void afterPropertiesSet() {
        //启动时初始化默认表结构信息
        initFormTemplateRelations();
    }

    private void initFormTemplateRelations() {
        Dialect dialect = dataBaseService.getDataBaseMetaDataByModuleName(Constants.MODULE_NAME).getDialect();
        KindType[] kindTypes = KindType.values();
        archiveParentEntityMap = Collections.synchronizedMap(new HashMap<>(kindTypes.length * 3));
        for (KindType kindType : kindTypes) {
            addIfAbsent(kindType.getArcBaseClass(), dialect);
            addIfAbsent(kindType.getFileBaseClass(), dialect);
            addIfAbsent(kindType.getProBaseClass(), dialect);
            addIfAbsent(kindType.getBoxBaseClass(), dialect);
        }
        addIfAbsent(AbstractArchiveEntity.class, dialect);
    }

    private void addIfAbsent(Class clazz, Dialect dialect) {
        if (clazz != null) {
            archiveParentEntityMap.computeIfAbsent(clazz, c -> {
                EntityRelation relation = new EntityRelation(false);
                AnnotationTableReaderUtil.readColumnInfo(relation, c, dialect);
                return relation;
            });
        }
    }
}
