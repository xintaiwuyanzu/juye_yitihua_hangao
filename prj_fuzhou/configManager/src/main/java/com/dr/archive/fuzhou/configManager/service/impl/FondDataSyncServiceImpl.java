package com.dr.archive.fuzhou.configManager.service.impl;

import com.dr.archive.event.ArchiveFormCreateEvent;
import com.dr.archive.fuzhou.configManager.bo.*;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.fuzhou.configManager.service.FondDataSyncService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.CommandExecutor;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

/**
 * @author caor
 * @date 2021-09-09 15:53
 */
@Service
public class FondDataSyncServiceImpl extends AbstractFondSyncService implements FondDataSyncService {
    @Autowired
    ConfigManagerClient configManagerClient;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ArchiveFormDefinitionService archiveFormDefinitionService;
    @Autowired
    protected CommandExecutor commandExecutor;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    FormNameGenerator formNameGenerator;

    /**
     * 根据机构code添加全宗
     *
     * @param orgCode           机构编码 例如：11350100K17719260B
     * @param formDefinitionMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long dataSyncFondByOrgCode(String orgCode, Map<String, String> formDefinitionMap, Set<String> permissionSet) {
        Set<String> orgPermissionIds = new HashSet<>();
        //先判断组织机构是否存在
        Organise organise = getOrganiseByCode(orgCode);
        if (organise == null) {
            return 0;
        }
        //查询现在有的全宗
        Map<String, Fond> oldFondMap = getFondMapByOrganiseId(organise.getId());
        //根据组织机构编码查询全宗信息，这里一般只会有一个全宗号
        List<FondInfo> fondInfoList = configManagerClient.getMetadataConfig(orgCode);
        for (FondInfo fondInfo : fondInfoList) {
            Fond oldFond = oldFondMap.get(fondInfo.getId());
            Fond newFond = newFond(fondInfo, organise);
            if (oldFond == null) {
                //新加全宗
                fondService.insert(newFond);
            } else {
                //全宗信息有变化
                boolean change = compareFond(oldFond, newFond);
                if (change) {
                    oldFond.setName(newFond.getName());
                    oldFond.setCode(newFond.getCode());
                    oldFond.setPartyId(newFond.getPartyId());
                    fondService.updateById(oldFond);
                }
            }
            //添加分类
            String categoryPermissionId = dataSyncCategory(fondInfo, newFond, organise, formDefinitionMap);
            if (StringUtils.hasText(categoryPermissionId)) {
                orgPermissionIds.add(categoryPermissionId);
            }
        }
        //之前添加的全宗，最新的同步没有，说明全宗被禁用了
        Set<String> newFondIds = fondInfoList.stream().map(AbstractConfigEntity::getId).collect(Collectors.toSet());
        oldFondMap.forEach((k, v) -> {
            if (!newFondIds.contains(k)) {
                //本次同步没有的全宗，则禁用全宗
                v.setStatus(StatusEntity.STATUS_ENABLE_STR);
                fondService.updateById(v);
            }
        });
        //添加门类和全宗权限
        String fondPermissionId = bindFondPermission(organise, fondInfoList);
        if (StringUtils.hasText(fondPermissionId)) {
            orgPermissionIds.add(fondPermissionId);
        }
        if (!orgPermissionIds.isEmpty()) {
            String dataRoleId = organise.getId() + "_data";
            //给机构角色绑定数据权限
            securityManager.addPermissionToRole(dataRoleId, orgPermissionIds.toArray(new String[0]));
            permissionSet.addAll(orgPermissionIds);
        }
        return fondInfoList.size();
    }


    /**
     * 添加门类和分类
     *
     * @param fondInfo          分类列表
     * @param fond              全宗
     * @param organise
     * @param formDefinitionMap
     */
    protected String dataSyncCategory(FondInfo fondInfo, Fond fond, Organise organise, Map<String, String> formDefinitionMap) {
        //查询该全宗下的所有门类数据
        Map<String, Category> fondCategoryMap = categoryService.selectList(SqlQuery.from(Category.class).equal(com.dr.archive.manage.category.entity.CategoryInfo.FONDID, fond.getId())).stream().collect(Collectors.toMap(BaseEntity::getId, c -> c));
        Set<String> currentCategorySet = new HashSet<>();
        /*Collections.addAll(currentCategorySet, fondInfo.getArcTypes().split(","));*/

        List<CategoryInfo> allCategory = configManagerClient.getCategoryInfo();
        for (CategoryInfo categoryInfo : allCategory) {
            currentCategorySet.add(categoryInfo.getId());
        }
        Map<String, CategoryInfo> allCateMap = allCategory.stream().collect(Collectors.toMap(AbstractConfigEntity::getId, c -> c));
        //所有配置的门类
           /* allCategory.stream().filter(c -> currentCategorySet.contains(c.getId())).forEach(categoryInfo -> {
                bindParent(categoryInfo, allCateMap);
                doSyncCategory(fond, categoryInfo, fondCategoryMap, formDefinitionMap, currentCategorySet);
            });*/
        for (CategoryInfo categoryInfo : allCategory) {
            bindParent(categoryInfo, allCateMap);
            doSyncCategory(fond, categoryInfo, fondCategoryMap, formDefinitionMap, currentCategorySet);
        }

        //删除归档配置中心中删除的门类数据
        fondCategoryMap.keySet().stream()
                //过滤自定义的门类
                .filter(i -> i.contains(":"))
                //过滤本次同步的门类Id
                .filter(i -> !currentCategorySet.contains(i))
                //TODO 删除掉的门类应该禁用掉 不应该暴力删除
                .forEach(i -> categoryService.deleteCategory(i, false));
        if (!currentCategorySet.isEmpty()) {
            return bindCategoryPermission(fondInfo, organise, currentCategorySet);
        }
        return null;
    }


    private void doSyncCategory(Fond fond, CategoryInfo categoryInfo, Map<String, Category> fondCategoryMap, Map<String, String> formDefinitionMap, Set<String> currentCategorySet) {
        Category newCategory = newCategory(categoryInfo, fond);
        if (!currentCategorySet.contains(newCategory.getId())) {
            //本次同步的门类Id
            currentCategorySet.add(newCategory.getId());
            Category oldCategory = fondCategoryMap.get(newCategory.getId());
            if (oldCategory == null) {
                //新建门类
                categoryService.insert(newCategory);
            } else {
                //更新门类
                if (compareCategory(newCategory, oldCategory)) {
                    oldCategory.setCode(newCategory.getCode());
                    oldCategory.setName(newCategory.getName());
                    oldCategory.setDescription(newCategory.getDescription());
                    categoryService.updateById(oldCategory);
                }
            }
            if (categoryInfo.getParent() != null) {
                doSyncCategory(fond, categoryInfo.getParent(), fondCategoryMap, formDefinitionMap, currentCategorySet);
            }
            //更新元数据
            dataSyncFieldMetaData(categoryInfo, newCategory, formDefinitionMap);
        }
    }

    /**
     * 根据门类分类创建表单
     *
     * @param categoryInfo
     * @param newCategory
     * @param formDefinitionMap
     */
    @Transactional(rollbackFor = Exception.class)
    protected void dataSyncFieldMetaData(CategoryInfo categoryInfo, Category newCategory, Map<String, String> formDefinitionMap) {

        for (CategoryYear categoryYear : configManagerClient.getArchiveTypeYear(categoryInfo.getCode())) {
            String[] fileTypes = categoryYear.getType().split(",");
            String formDefinitionId = formDefinitionMap.get(categoryYear.getId());

            if (!StringUtils.hasText(formDefinitionId)) { //只取 电子档案元数据配置

                //根据门类Id查询门类的元数据配置  1案件 TODO 案卷方式需要再处理
//                    List<FieldMetaData> fieldMetaDataList = configManagerClient.getCategoryMetadata(categoryInfo.getCode(), categoryYear.getClassify(), categoryYear.getStandard()).get(0).getMetadata()
                List<FieldMetaData> fieldMetaDataList = configManagerClient.getCategoryMetadata(categoryInfo.getCode(), categoryYear.getClassify(), "1", categoryYear.getStartTime()).get(0).getMetadata()
                        //只同步基本信息元数据
                        .stream()
                        .filter(f -> f.getAscription().equals(fileTypes[0]) & f.getTypeID().equals(categoryInfo.getId()))
                        //只同步简单类型元数据
                        .filter(f -> FieldMetaData.META_TYPE_SIMPLE.equals(f.getMateType())).collect(Collectors.toList());
                FormDefinition newFormModel = newForm(categoryInfo, categoryYear, fileTypes[0]);
                //原有表单定义
                FormModel oldFormModel = formDefinitionService.selectFormDefinitionByCode(newFormModel.getFormCode());
                //表单字段
                List<FieldModel> formFieldList = buildFormFields(fieldMetaDataList, categoryInfo);
                if (oldFormModel == null) {
                    //需要添加其他参数 添加人，状态，时间等
                    addDefaultMetaData(formFieldList);
                    newFormModel = (FormDefinition) formDefinitionService.addFormDefinition(newFormModel, formFieldList, true);
                    formDefinitionId = newFormModel.getId();
                    //添加默认表单方案
                    archiveFormDefinitionService.addFormDisplay(newFormModel.getFields(), newFormModel);
                    //创建ES
                    Relation relation = dataBaseService.getTableInfo(formNameGenerator.genTableName(newFormModel), MODULE_NAME);
                    if (null != relation) {
                        applicationEventPublisher.publishEvent(new ArchiveFormCreateEvent(newFormModel));
                    }
                } else {
                    formDefinitionId = oldFormModel.getId();
                    //更新表单基本信息定义
                    formDefinitionService.updateFormDefinitionBaseInfo(newFormModel);
                    Set<String> oldFields = oldFormModel.getFields().stream().map(BaseEntity::getId).collect(Collectors.toSet());
                    //原来没有的Id，才添加字段
                    List<FieldModel> newFields = formFieldList.stream().filter(f -> !oldFields.contains(f.getId())).collect(Collectors.toList());
                    if (!newFields.isEmpty()) {
                        formDefinitionService.addFieldsWithOutUpdateVersion(oldFormModel.getId(), newFields, true);
                        //添加字段
                        //commandExecutor.execute(new FormDefinitionFieldListAddWithOutVersionCommand(oldFormModel.getId(), true, newFields));
                    }
                }

                formDefinitionMap.put(categoryYear.getId(), formDefinitionId);
            }
            bindCategoryConfig(newCategory, categoryYear, formDefinitionId);

        }
    }

    private List<FieldModel> buildFormFields(List<FieldMetaData> fieldMetaDataList, CategoryInfo categoryInfo) {
        //判断获取的元数据是否在字典项中
        List<TreeNode> sysMetaDataList = sysDictService.dict(DICT_ARCHIVE_METADATA + categoryInfo.getCode());
        Map<String, String> configKeyMap = sysMetaDataList.stream().collect(Collectors.toMap(TreeNode::getId, TreeNode::getLabel));
        Map<String, String> defaultKeyMap = sysDictService.dict(DICT_ARCHIVE_METADATA_DEFAULT).stream().collect(Collectors.toMap(TreeNode::getId, TreeNode::getLabel));
        //需要根据字典项进行转换
        //正常最多只有一个字典项，如果有多个则配置有误
        Function<String, String> fieldNameMap = (code) -> Optional.ofNullable(configKeyMap.get(code)).orElseGet(() -> defaultKeyMap.getOrDefault(code, code));
        return fieldMetaDataList.stream()
                //过滤掉没有配英文的字段
                .filter(f -> StringUtils.hasText(f.geteName()))
                //映射字段配置
                .map(fieldMetaData -> newFormField(fieldMetaData, fieldNameMap))
                .collect(Collectors.toList());
    }

}
