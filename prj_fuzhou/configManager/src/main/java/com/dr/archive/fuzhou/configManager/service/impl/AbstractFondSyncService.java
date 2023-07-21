package com.dr.archive.fuzhou.configManager.service.impl;

import com.dr.archive.enums.CategoryType;
import com.dr.archive.enums.KindType;
import com.dr.archive.fuzhou.bsp.utils.CompareUtil;
import com.dr.archive.fuzhou.configManager.bo.CategoryInfo;
import com.dr.archive.fuzhou.configManager.bo.CategoryYear;
import com.dr.archive.fuzhou.configManager.bo.FieldMetaData;
import com.dr.archive.fuzhou.configManager.bo.FondInfo;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.impl.ColumnFieldModel;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.util.Constants;
import com.dr.archive.util.DateTimeUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FieldType;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.common.task.entity.TaskDefinition;
import com.dr.framework.common.task.entity.TaskInstance;
import com.dr.framework.common.task.service.TaskTypeProvider;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.database.Dialect;
import com.dr.framework.core.orm.database.tools.AnnotationTableReaderUtil;
import com.dr.framework.core.orm.module.EntityRelation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.entity.Permission;
import com.dr.framework.core.security.service.SecurityManager;
import com.dr.framework.sys.service.PermissionService;
import com.dr.framework.sys.service.RoleService;
import com.dr.framework.sys.service.SysDictService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 全宗门类同步抽象类，封装抽象工具方法
 * 子类控制关键逻辑
 *
 * @author dr
 */
public abstract class AbstractFondSyncService implements TaskTypeProvider, InitializingBean {
    @Autowired
    protected OrganisePersonService organisePersonService;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected FondOrganiseService fondOrganiseService;
    @Autowired
    protected CategoryConfigService categoryConfigService;

    @Autowired
    protected SysDictService sysDictService;
    @Autowired
    protected SecurityManager securityManager;
    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    CommonMapper commonMapper;

    /**
     * 根据机构编码查询机构
     *
     * @param orgCode
     * @return
     */
    protected Organise getOrganiseByCode(String orgCode) {
        if (StringUtils.hasText(orgCode)) {
            return organisePersonService.getOrganise(new OrganiseQuery.Builder().codeEqual(orgCode).statusEqual("1").getQuery());
        } else {
            return null;
        }
    }

    /**
     * 根据机构Id查询全宗
     *
     * @param organiseId
     * @return
     */
    protected Map<String, Fond> getFondMapByOrganiseId(String organiseId) {
        return fondOrganiseService.getFondListByOrganiseId(organiseId).stream().collect(Collectors.toMap(BaseEntity::getId, f -> f));
    }

    /**
     * 根据智能归档配置创建全宗
     *
     * @param fondInfo
     * @param organise
     * @return
     */
    protected Fond newFond(FondInfo fondInfo, Organise organise) {
        Fond fond = new Fond();
        fond.setId(fondInfo.getId());
        fond.setName(fondInfo.getOrgName());
        fond.setCode(fondInfo.getNumbers());
        fond.setEnabled(true);
        //需要先完成组织机构的同步，否则报错
        fond.setPartyId(organise.getId());
        fond.setOrder(organise.getOrder());
        return fond;
    }

    /**
     * 绑定机构对应的全宗权限
     *
     * @param organise
     * @param fondInfoList
     * @return
     */
    protected String bindFondPermission(Organise organise, List<FondInfo> fondInfoList) {
        if (!fondInfoList.isEmpty()) {
            String permissionId = organise.getId() + "_fond";
            String permissionCode = fondInfoList.stream().map(fondInfo -> fondInfo.getId() + ":*").collect(Collectors.joining(","));
            Permission permission = permissionService.selectById(permissionId);
            boolean isNew = false;
            if (permission == null) {
                isNew = true;
                permission = new Permission();
                permission.setId(permissionId);
                permission.setType("fond");
            }
            permission.setCode(permissionCode);
            permission.setName(organise.getOrganiseName() + "全部全宗权限");
            permission.setDescription(permission.getName() + "【自动生成】");
            permission.setOrder(organise.getOrder());
            if (isNew) {
                permissionService.insert(permission);
            } else {
                permissionService.updateById(permission);
            }
            return permissionId;
        }
        return null;
    }

    /**
     * 创建一个全宗下面所有门类的权限
     *
     * @param fondInfo
     * @param organise
     * @param currentCategorySet
     * @return
     */
    protected String bindCategoryPermission(FondInfo fondInfo, Organise organise, Set<String> currentCategorySet) {
        //权限Id
        String permissionId = fondInfo.getId() + "_category";
        String permissionCode = bindCategoryPermission(currentCategorySet);
        if (StringUtils.hasText(permissionCode)) {
            Permission permission = permissionService.selectById(permissionId);
            boolean isNew = false;
            if (permission == null) {
                isNew = true;
                permission = new Permission();
                permission.setId(permissionId);
                permission.setGroupId(fondInfo.getId());
                permission.setType("category");
            }
            permission.setCode(permissionCode);
            permission.setName(fondInfo.getOrgName() + "全部门类权限");
            permission.setDescription(permission.getName() + "【自动生成】");
            permission.setOrder(organise.getOrder());
            if (isNew) {
                permissionService.insert(permission);
            } else {
                permissionService.updateById(permission);
            }
            return permissionId;
        }
        return null;
    }

    protected String bindCategoryPermission(Set<String> cateIds) {
        return cateIds.stream().map(i -> i + ":*").collect(Collectors.joining(","));
    }

    /**
     * 根据智能归档配置中心配置的门类信息创建系统门类配置
     *
     * @param categoryInfo
     * @param fond
     * @return
     */
    protected Category newCategory(CategoryInfo categoryInfo, Fond fond) {
        Category category = new Category();
        //因为智能归档配置中心中全宗和门类是多对多关系，这里为了保留映射关系，拼接导入进来的门类信息ID为全宗Id加上他们门类的Id
        category.setId(String.join(":", fond.getId(), categoryInfo.getId()));
        category.setFondId(fond.getId());
        if ("0".equals(categoryInfo.getParentID())) {
            category.setParentId(fond.getId());
        } else {
            category.setParentId(String.join(":", fond.getId(), categoryInfo.getParentID()));
        }
        //TODO 门类类型归档系统中是写谁的
        category.setCategoryType(KindType.getValue(0));
        category.setBusinessId(fond.getId());
        //TODO 现在都是文件级别类型，后面很有可能需要修改，但是需要智能归档系统改造，分类类型应该分（项目、案卷、文件），不同分类对应的元数据应该也不同,没有纠正，先让他们入坑
        category.setArchiveType("0");
        //TODO 顺序号智能归档配置系统没有传，不管他了 category.setOrder();
        category.setCode(categoryInfo.getCode());
        category.setName(categoryInfo.getName());
        category.setDescription(categoryInfo.getRemark());
        category.setOrder(Long.valueOf(categoryInfo.getCrTime().getTime()).intValue());
        return category;
    }

    protected FormDefinition newForm(CategoryInfo categoryInfo, CategoryYear categoryYear, String formType) {
        //TODO 这里插入了年度，而且表单名称需要转换过滤掉关键字符，es
        String formCode = String.join("_", categoryYear.getSysCode(), formType).toLowerCase();
        FormDefinition formDefinition = new FormDefinition();
        //所有的表单类型都是文件类表单
        formDefinition.setFormType(String.valueOf(CategoryType.FILE.getCode()));
        formDefinition.setFormCode(formCode);

        StringBuilder sb = new StringBuilder(categoryInfo.getName());
        sb.append("(").append(categoryYear.getStartTime());
        if (StringUtils.hasText(categoryYear.getEndTime())) {
            sb.append("-").append(categoryYear.getEndTime());
        }
        sb.append(")");
        formDefinition.setFormName(sb.toString());
        formDefinition.setDescription(categoryInfo.getRemark() + "【自动表单】");
        formDefinition.setRemarks(categoryInfo.getRemark());
        return formDefinition;
    }

    /**
     * 表单字段赋值
     *
     * @param fieldMetaData
     * @param fieldNameMap
     * @return
     */
    protected FormField newFormField(FieldMetaData fieldMetaData, Function<String, String> fieldNameMap) {
        //字段编码不能为空 字段编码还有数字类型，数据乱七八糟，瞎几把搞
        FormField formField = new FormField();
        formField.setId(fieldMetaData.getId());
        formField.setLabel(fieldMetaData.getName());
        formField.setDescription(fieldMetaData.getRemark());
        String eName = fieldMetaData.geteName();
        String convertName = fieldNameMap.apply(eName);
        formField.setFieldCode(convertName);
        if (!convertName.equals(eName)) {
            //如果字段编码经过字典转换了，则存成别名
            formField.setFieldAliasStr(eName);
        }
        // 这里强制设置数据类型都是string
        formField.setFieldTypeStrEnum(FieldType.CLOB);
        formField.setFieldLength(parseLength(fieldMetaData.getTypeLength()));
        //字段排序
        if (StringUtils.hasText(fieldMetaData.getOrders())) {
            formField.setOrder(Integer.valueOf(fieldMetaData.getOrders()));
        }
        return formField;
    }

    /**
     * 绑定表单门类配置信息
     *
     * @param categoryInfo
     * @param categoryYear
     * @param formDefinitionId
     */
    protected void bindCategoryConfig(Category categoryInfo, CategoryYear categoryYear, String formDefinitionId) {
        //创建分类、表单关系
        CategoryConfig categoryConfig = new CategoryConfig();
        String id = categoryInfo.getId() + ":" + categoryYear.getId();
        long count = categoryConfigService.count(SqlQuery.from(CategoryConfig.class).equal(CategoryConfigInfo.ID, id));
        categoryConfig.setId(id);
        categoryConfig.setName(categoryInfo.getName() + "通用方案");
        categoryConfig.setCode(categoryInfo.getCode());
        categoryConfig.setBusinessId(categoryInfo.getId());
        categoryConfig.setStartYear(formatDate(categoryYear.getStartTime()));
        if (StringUtils.hasText(categoryYear.getEndTime())) {
            categoryConfig.setEndYear(formatDate(categoryYear.getEndTime()));
        }
        categoryConfig.setFileFormId(formDefinitionId);
        categoryConfig.setFileFormName(categoryInfo.getName());

        categoryConfig.setDefault(isDefault(categoryYear.getStartTime(), categoryYear.getEndTime()));
        if (count == 0) {
            commonMapper.insert(categoryConfig);
        } else {
            commonMapper.updateIgnoreNullById(categoryConfig);
        }
    }

    private boolean isDefault(String startTime, String endTime) {

        Long startTimel = DateTimeUtils.stringToMillis(startTime, "yyyy-MM-dd");
        Long nowLong = DateTimeUtils.dateToMillis(new Date());
        if (StringUtils.hasText(endTime)) {
            Long endTimel = DateTimeUtils.stringToMillis(endTime, "yyyy-MM-dd");
            return nowLong >= startTimel && nowLong < endTimel;
        } else { //结束时间为空
            return true;
        }

    }

    private int formatDate(String day) {
        if (StringUtils.hasText(day)) {
            try {
                Date date = DateUtils.parseDate(day, "yyyy-MM-dd");
                return Integer.parseInt(DateFormatUtils.format(date, "yyyyMMdd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 添加默认字段
     *
     * @param formFieldList
     * @return
     */
    protected void addDefaultMetaData(List<FieldModel> formFieldList) {
        //所有字段名称转换成大写
        Set<String> fieldNameSet = formFieldList.stream()
                .map(FieldModel::getFieldCode)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        entityRelation.getColumns().stream()
                .filter(f -> !f.getName().equals(IdEntity.ID_COLUMN_NAME))
                .filter(f -> !fieldNameSet.contains(f.getName().toUpperCase()))
                .forEach(c -> formFieldList.add(new ColumnFieldModel(c)));
    }


    /**
     * 强制转换类型为int
     *
     * @param typeLength
     * @return
     */
    private int parseLength(String typeLength) {
        if (StringUtils.hasText(typeLength) && !"0".equalsIgnoreCase(typeLength.trim())) {
            try {
                return Integer.parseInt(typeLength);
            } catch (Exception ignore) {

            }
        }
        return 255;
    }

    /**
     * 对比门类是否变化
     *
     * @param newCategory
     * @param oldCategory
     * @return
     */
    protected boolean compareCategory(Category newCategory, Category oldCategory) {
        return CompareUtil.compare(newCategory, oldCategory, Category::getName, Category::getCode, Category::getDescription, Category::getOrder);
    }

    /**
     * 对比全宗是否变化
     *
     * @param source
     * @param target
     * @return
     */
    protected boolean compareFond(Fond source, Fond target) {
        return CompareUtil.compare(source, target, Fond::getName, Fond::getCode, Fond::getOrder);
    }

    /**
     * 绑定父元素
     *
     * @param categoryInfo
     * @param allCategory
     */
    protected void bindParent(CategoryInfo categoryInfo, Map<String, CategoryInfo> allCategory) {
        if (StringUtils.hasText(categoryInfo.getParentID())) {
            CategoryInfo parent = allCategory.get(categoryInfo.getParentID());
            if (parent != null) {
                bindParent(parent, allCategory);
            }
            categoryInfo.setParent(parent);
        }
    }

    @Override
    public String type() {
        return "fondSync";
    }

    @Override
    public String name() {
        return "全宗门类同步";
    }

    /**
     * 定时任务同步全宗门类数据
     * 根据本地机构编码同步全宗
     * 应该先使用{@link  com.dr.archive.fuzhou.bsp.BspSyncService}同步组织机构，在使用本类同步全宗门类
     *
     * @param taskDefinition
     * @param taskInstance
     * @param person
     * @param map
     * @return
     */
    @Override
    public String executeTask(TaskDefinition taskDefinition, TaskInstance taskInstance, Person person, Map<String, String> map) {
        //表单同步信息 ，key是分类Id
        Map<String, String> formDefinitionMap = new HashMap<>();
        //在同步全宗
        //在同步分类和元数据
        List<Organise> organises = organisePersonService.getOrganiseList(new OrganiseQuery.Builder().build());
        Set<String> resultSet = new HashSet<>();
        for (Organise organise : organises) {
            long fondCount = dataSyncFondByOrgCode(organise.getOrganiseCode(), formDefinitionMap, new HashSet<>());
            resultSet.add(String.format("【%s】同步全宗数量为【%s】", organise.getOrganiseName(), fondCount));
        }
        return String.join("\n", resultSet);
    }

    protected abstract long dataSyncFondByOrgCode(String organiseCode, Map<String, String> formDefinitionMap, Set<String> permissionSet);

    @Autowired
    protected DataBaseService dataBaseService;
    protected EntityRelation entityRelation;

    @Override
    public void afterPropertiesSet() {
        Dialect dialect = Objects.requireNonNull(dataBaseService.getDataBaseMetaDataByModuleName(Constants.MODULE_NAME)).getDialect();
        entityRelation = new EntityRelation(false);
        //TODO 此方法强制添加我们系统属性
        AnnotationTableReaderUtil.readColumnInfo(entityRelation, AbstractArchiveEntity.class, dialect);
    }
}
