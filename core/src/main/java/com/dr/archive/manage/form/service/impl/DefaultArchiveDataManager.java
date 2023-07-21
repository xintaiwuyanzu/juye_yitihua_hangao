package com.dr.archive.manage.form.service.impl;

import com.dr.archive.event.ArchiveDataDeleteEvent;
import com.dr.archive.event.ArchiveDataEditEvent;
import com.dr.archive.event.ArchiveDataStatusChangeEvent;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.entity.ArchiveRepeat;
import com.dr.archive.manage.form.entity.MoveGoRecording;
import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.service.*;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.entity.ArchiveOrder;
import com.dr.archive.model.entity.ArchiveOrderInfo;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.model.query.CategoryFormQuery;
import com.dr.archive.service.internal.CodingSchemeService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.jdbc.Column;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.OrderComparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

/**
 * 默认的档案数据管理实现类
 *
 * @author dr
 */
@Service
public class DefaultArchiveDataManager implements ArchiveDataManager, InitializingBean, ApplicationContextAware {
    public static final Logger logger = LoggerFactory.getLogger(DefaultArchiveDataManager.class);
    @Autowired
    FormDataService formDataService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    CodingSchemeService codingSchemeService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FondService fondService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    ArchiveDataContextFactory contextFactory;
    Collection<ArchiveDataPlugin> archiveDataPlugins;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    DataBaseService dataBaseService;
    @Autowired
    FormNameGenerator formNameGenerator;
    @Autowired
    CategoryConfigService categoryConfigService;
    @Autowired
    MoveGoRecordingService moveGoRecordingService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    RegisterWarehousingService registerWarehousingService;
    @Autowired
    RegisterWarehousingDetailsService registerWarehousingDetailsService;
    @Autowired
    protected ArchiveDataManager archiveDataManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FormData insertFormData(FormData formData, String fondId, String categoryId) {
        ArchiveDataContext context = contextFactory.buildContext(categoryId, formData.getFormDefinitionId());
        if (!StringUtils.isEmpty(context.getCategory().getCode())) {
            formData.put(ArchiveEntity.COLUMN_CATEGORY_CODE, context.getCategory().getCode());
        }
        boolean traceAble = logger.isTraceEnabled();
        for (ArchiveDataPlugin dataPlugin : archiveDataPlugins) {
            if (!StringUtils.isEmpty(context)) {
                if (context.isEnable(dataPlugin)) {
                    if (traceAble) {
                        logger.trace("分类{}，表单{}，执行档案数据插入前拦截{}",
                                context.getCategory().getCode(),
                                context.getFormModel().getFormName(),
                                AopUtils.getTargetClass(dataPlugin).getName()
                        );
                    }
                    //这里貌似在进行生成档号？？？
                    //formData = dataPlugin.beforeInsert(formData, context);
                }
            }
        }
        if (!StringUtils.isEmpty(context)) {
            formData.put("createDate", System.currentTimeMillis());
            formData.put("updateDate", System.currentTimeMillis());
            Person person = context.getPerson();
            if (person != null) {
                if (StringUtils.isEmpty(formData.getString("createPerson"))) {
                    formData.put("createPerson", person.getId());
                }
                if (StringUtils.isEmpty(formData.getString("updateDate"))) {
                    formData.put("updatePerson", person.getId());
                }
                //机构id，档案馆查询移交档案数据有用  这个字段在移交的时候添加进去
                Organise organise = SecurityHolder.get().currentOrganise();
                if (null != organise) {
                    if ("dag".equals(organise.getOrganiseType())) {
                        formData.put(ArchiveEntity.COLUMN_ORGANISEID, organise.getId());
                    }
                }
            }
        }
        formData = formDataService.addFormData(formData, false);
        for (ArchiveDataPlugin dataPlugin : archiveDataPlugins) {
            if (!StringUtils.isEmpty(context)) {
                if (context.isEnable(dataPlugin)) {
                    if (traceAble) {
                        logger.trace("分类{}，表单{}，执行档案数据插入后拦截{}",
                                context.getCategory().getCode(),
                                context.getFormModel().getFormName(),
                                AopUtils.getTargetClass(dataPlugin).getName()
                        );
                    }
                    formData = dataPlugin.afterInsert(formData, context);
                }
            }
        }
        return formData;
    }




    /**
     * 自动生成顺序号
     * author caiwb
     * TODO 缺少档号生成规则查询用code，现在还不够动态
     *
     * @param formData
     */
    public void addArchiveCode(FormData formData) {
        //模拟档号生成规则数据
        List<Map> generation = new ArrayList<>();
        Map map = new HashMap();
        map.put("symbol", "-");//连接符号
        map.put("eName", ArchiveEntity.COLUMN_FOND_CODE);//字段
        map.put("name", "全宗号");//字段名
        map.put("digit", 4);//长度
        generation.add(map);
        Map map1 = new HashMap();
        map1.put("symbol", "-");
        map1.put("eName", ArchiveEntity.COLUMN_SAVE_TERM);
        map1.put("name", "保管期限");
        map1.put("digit", 4);
        generation.add(map1);
        Map map2 = new HashMap();
        map2.put("symbol", "-");
        map2.put("eName", ArchiveEntity.COLUMN_YEAR);
        map2.put("name", "年度");
        map2.put("digit", 4);
        generation.add(map2);
        Map map3 = new HashMap();
        map3.put("symbol", "-");
        map3.put("eName", ArchiveEntity.COLUMN_MLDM);
        map3.put("name", "门类代码");
        map3.put("digit", 4);
        generation.add(map3);
        //顺序号长度
        int numberDigit = 4;
        StringBuilder archiveCode = new StringBuilder();
        for (Map map4 : generation) {
            //判断用户是否输入对应字段
            if (formData.containsKey(map4.get("eName")) && !StringUtils.isEmpty(formData.getString(map4.get("eName").toString()))) {
                //判断输入长度是否超出，超出报错前台进行提示
                if (Integer.parseInt(map4.get("digit").toString()) < formData.getString(map4.get("eName").toString()).length()) {
                    Assert.isTrue(false, map4.get("name") + "长度超出");
                    //输入长度与配置长度相等直接拼接
                } else if (Integer.parseInt(map4.get("digit").toString()) == formData.getString(map4.get("eName").toString()).length()) {
                    archiveCode.append(formData.getString(map4.get("eName").toString())).append(map4.get("symbol"));
                    //长度不足进行补零
                } else {
                    int digit = Integer.parseInt(map4.get("digit").toString()) - formData.getString(map4.get("eName").toString()).length();
                    for (int i = 0; i < digit; i++) {
                        archiveCode.append("0");
                    }
                    archiveCode.append(formData.getString(map4.get("eName").toString())).append(map4.get("symbol"));
                }
            } else {
                //有空值直接提示
                Assert.isTrue(false, map4.get("name") + "为空，请根据档号生成规则输入");
            }
        }
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formData.getFormDefinitionId());
        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        List<Map> list = commonMapper.selectByQuery(SqlQuery.from(tableInfo)
                .like(tableInfo.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE), archiveCode.toString())
                .orderByDesc(tableInfo.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE))
                .setReturnClass(Map.class));
        //查不到档号时拼接顺序号用
        StringBuilder suffix = new StringBuilder();
        for (int i = 1; i < numberDigit; i++) {
            suffix.append("0");
        }
        try {
            if (list.size() > 0) {
                String tail = "";
                for (Map map4 : list) {
                    String[] split = map4.get(ArchiveEntity.COLUMN_ARCHIVE_CODE).toString().split("-");
                    if (split[split.length - 1].length() == numberDigit) {
                        tail = map4.get(ArchiveEntity.COLUMN_ARCHIVE_CODE).toString();
                        break;
                    }
                }
                if (!StringUtils.isEmpty(tail)) {
                    tail = tail.substring(tail.lastIndexOf("-") + 1);
                    int takeMold = 1;
                    for (int i = 1; i < numberDigit; i++) {
                        if (!StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE))) {
                            return;
                        }
                        int count = numberDigit - i;
                        takeMold = takeMold * 10;
                        StringBuilder startStr = new StringBuilder();
                        for (int j = 0; j < count; j++) {
                            startStr.append("0");
                        }
                        if (tail.startsWith(startStr.toString())) {
                            int jh = Integer.parseInt(tail.substring(numberDigit - i, numberDigit)) + 1;
                            if (jh % takeMold == 0) {
                                formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode + startStr.substring(0, startStr.length() - 1) + jh);
                            } else {
                                formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode.toString() + startStr + jh);
                            }
                            return;
                        } else {
                        }
                    }
                } else {
                    formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode.toString() + suffix + "1");
                }
            } else {
                formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode.toString() + suffix + "1");
            }
        } catch (Exception e) {
            formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode.toString() + suffix + "1");
        }
    }

    @Override
    public FormData updateFormData(FormData formData, String fondId, String categoryId) {
        ArchiveDataContext context = contextFactory.buildContext(categoryId, formData.getFormDefinitionId());
        boolean traceAble = logger.isTraceEnabled();
        for (ArchiveDataPlugin dataPlugin : archiveDataPlugins) {
            if (context.isEnable(dataPlugin)) {
                if (traceAble) {
                    logger.trace("分类{}，表单{}，执行档案数据更新前拦截{}",
                            context.getCategory().getCode(),
                            context.getFormModel().getFormName(),
                            AopUtils.getTargetClass(dataPlugin).getName()
                    );
                }
                formData = dataPlugin.beforeUpdate(formData, context);
            }
        }
        formData.put("updateDate", System.currentTimeMillis());
        Person person = context.getPerson();
        if (person != null) {
            if (StringUtils.isEmpty(formData.getString("updateDate"))) {
                formData.put("updatePerson", person.getId());
            }
        }
        formData = formDataService.updateFormDataById(formData);
        for (ArchiveDataPlugin dataPlugin : archiveDataPlugins) {
            if (context.isEnable(dataPlugin)) {
                if (traceAble) {
                    logger.trace("分类{}，表单{}，执行档案数据插入后拦截{}",
                            context.getCategory().getCode(),
                            context.getFormModel().getFormName(),
                            AopUtils.getTargetClass(dataPlugin).getName()
                    );
                }
                formData = dataPlugin.afterUpdate(formData, context);
            }
        }
        return formData;
    }

    @Override
    public FormData selectOneFormData(String formDefinitionId, String formDataId) {
        return formDataService.selectOneFormData(formDefinitionId, formDataId);
    }

    @Override
    public Page<FormData> formDataPage(String formDefinitionId, SqlBuilder sqlBuilder, int pageIndex, int pageSize) {
        return formDataService.selectPageFormData(formDefinitionId, sqlBuilder, pageIndex, pageSize);
    }

    @Override
    public Page<FormData> formDataPage(ArchiveDataQuery query, int pageIndex, int pageSize) {
        return formDataService.selectPageFormData(query.getFormDefinitionId(), newBuilder(query), pageIndex, pageSize);
    }

    /**
     * 1、删除原文附件信息
     * 2、删除目录信息
     * TODO 如果是案卷信息需要删除卷内目录信息
     *
     * @param categoryId
     * @param formDefinitionId
     * @param aId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteFormData(String categoryId, String formDefinitionId, String aId) {
        if (!StringUtils.isEmpty(categoryId)) {
            ArchiveDataContext context = contextFactory.buildContext(categoryId, formDefinitionId);
            long result = 0;
            boolean traceAble = logger.isTraceEnabled();
            for (ArchiveDataPlugin dataPlugin : archiveDataPlugins) {
                if (context.isEnable(dataPlugin)) {
                    if (traceAble) {
                        logger.trace("分类{}，表单{}，执行档案数据删除前拦截{}",
                                context.getCategory().getCode(),
                                context.getFormModel().getFormName(),
                                AopUtils.getTargetClass(dataPlugin).getName()
                        );
                    }
                    result += dataPlugin.beforeDelete(aId, context);
                }
            }
            String[] ids = aId.split(",");
            for (String id : ids) {
                result += formDataService.removeFormData(formDefinitionId, id);
                applicationEventPublisher.publishEvent(new ArchiveDataDeleteEvent(formDefinitionId, id));
            }
            for (ArchiveDataPlugin dataPlugin : archiveDataPlugins) {
                if (context.isEnable(dataPlugin)) {
                    if (traceAble) {
                        logger.trace("分类{}，表单{}，执行档案数据删除后拦截{}",
                                context.getCategory().getCode(),
                                context.getFormModel().getFormName(),
                                AopUtils.getTargetClass(dataPlugin).getName()
                        );
                    }
                    result = dataPlugin.afterDelete(aId, context);
                }
            }
            return result;
        } else {
            long result = 0;
            String[] ids = aId.split(",");
            for (String id : ids) {
                result += formDataService.removeFormData(formDefinitionId, id);
                applicationEventPublisher.publishEvent(new ArchiveDataDeleteEvent(formDefinitionId, id));
            }
            return result;
        }
    }

    @Override
    public List<FormData> findDataByQuery(ArchiveDataQuery query) {
        return formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
    }

    @Override
    public List<FormData> findDataByBuilder(String formDefinitionId, SqlBuilder builder) {
        return formDataService.selectFormData(formDefinitionId, builder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String ids, String status, String formDefinitionId) {
        String[] split = ids.split(",");
        for (String id : split) {
            FormData formData = formDataService.selectOneFormData(formDefinitionId, id);
            ArchiveDataStatusChangeEvent event = new ArchiveDataStatusChangeEvent(
                    formData.getFormDefinitionId(),
                    formData.getId(),
                    formData.getString(StatusEntity.STATUS_COLUMN_KEY),
                    status
            );
            formData.put(ArchiveEntity.COLUMN_STATUS, status);
            formDataService.updateFormDataIgnoreNullById(formData);
            applicationEventPublisher.publishEvent(event);
            //查询案卷的卷内文件
            List<FormData> jnwj = archiveDataManager.getJNFormData(formData);
            if(jnwj != null && jnwj.size() > 0){
                for (FormData jn : jnwj) {
                    jn.put(ArchiveEntity.COLUMN_STATUS, "TRASH");
                    formDataService.updateFormDataById(jn);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSubStatus(String ids, String subStatus, String formDefinitionId) {
        String[] split = ids.split(",");
        for (String id : split) {
            FormData formData = formDataService.selectOneFormData(formDefinitionId, id);
            formData.put(ArchiveEntity.COLUMN_SUB_STATUS, subStatus);
            formDataService.updateFormDataIgnoreNullById(formData);
        }
    }

    @Override
    public List<ArchiveRepeat> repeat(String fond, String category, String formId, String status, String archiveCode) {
        SqlBuilder sqlBuilder = (sqlQuery, formRelationWrapper) -> {
            Column column = formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE);
            sqlQuery.column(column, formRelationWrapper.idColumn().count("count"))
                    .groupBy(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE))
                    .equal(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_FOND_CODE), fond)
                    .equal(formRelationWrapper.getColumn(AbstractArchiveEntity.STATUS_COLUMN_KEY), status)
                    .equal(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_CATEGORY_CODE), category)
                    .equal(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE), archiveCode)
                    .setReturnClass(ArchiveRepeat.class);
        };
        List<ArchiveRepeat> list = formDataService.countSelf(formId, sqlBuilder);
        for (int i = 0; i < list.size(); i++) {
            if ("1".equals(list.get(i).getCount())) {
                list.remove(i);
                i--;
            }
        }
        return list;
    }

    @Override
    public List<ArchiveRepeat> continuity(String fond, String category, String formId, String status, String archiveCode) {
        SqlBuilder sqlBuilder = (sqlQuery, formRelationWrapper) -> {
            Column COLUMN_ARCHIVE_CODE = formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE);
            Column COLUMN_FOND_CODE = formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_FOND_CODE);
            sqlQuery.column(COLUMN_ARCHIVE_CODE, COLUMN_FOND_CODE)
                    .equal(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_FOND_CODE), fond)
                    .equal(formRelationWrapper.getColumn(AbstractArchiveEntity.STATUS_COLUMN_KEY), status)
                    .equal(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_CATEGORY_CODE), category)
                    .equal(COLUMN_ARCHIVE_CODE, archiveCode)
                    .orderBy(COLUMN_ARCHIVE_CODE);
        };
        List<FormData> list = formDataService.selectFormData(formId, sqlBuilder);
        String code = null;
        String start = "";
        int num = 0;
        List<ArchiveRepeat> result = new ArrayList<>();
        for (FormData formData : list) {
            String tempcode = formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE);
            if ("".equals(tempcode) || null == tempcode) {
                continue;
            }
            if (!tempcode.contains("-")) {
                continue;
            }
            String tempStart = tempcode.substring(0, tempcode.lastIndexOf("-") + 1);
            try {
                int tempNumber = Integer.parseInt(tempcode.substring(tempcode.lastIndexOf("-") + 1));
                if (start.equals(tempStart)) {
                    if (num + 1 != tempNumber) {
                        ArchiveRepeat archiveRepeat = new ArchiveRepeat();
                        archiveRepeat.setArchive_Code(code + "至" + tempcode);
                        result.add(archiveRepeat);
                    }
                } else {
                    start = tempStart;
                }
                num = tempNumber;
                code = tempcode;
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    /**
     * 根据查询条件查询表单数据
     *
     * @param query
     * @return
     */
    private SqlBuilder newBuilder(ArchiveDataQuery query) {
        return ((sqlQuery, wrapper) -> {
            for (ArchiveDataQuery.QueryItem item : query.getQueryItems()) {
                Column column = wrapper.getColumn(item.getKey());
                if (column == null) {
                    continue;
                }
                switch (item.getType()) {
                    case IN:
                        String[] data = item.getValue().split(",");
                        sqlQuery.in(column, data);
                        break;
                    case LIKE:
                        sqlQuery.like(column, item.getValue());
                        break;
                    case EQUAL:
                        sqlQuery.equal(column, item.getValue());
                        break;
                    case END_WITH:
                        sqlQuery.endingWith(column, item.getValue());
                        break;
                    case START_WITH:
                        sqlQuery.startingWith(column, item.getValue());
                        break;
                    default:
                        break;
                }
            }
            // System.out.println(SecurityHolder.get().currentPerson());
            //需要根据机构id判断权限 TODO 在线接收时需要处理，估计得根据机构code查询机构id
            Organise organise = SecurityHolder.get().currentOrganise();
            if (null != organise) {
                if (null != wrapper.getColumn(ArchiveEntity.COLUMN_ORGANISEID) && "dag".equals(organise.getOrganiseType())) {
                    sqlQuery.equal(wrapper.getColumn(ArchiveEntity.COLUMN_ORGANISEID), organisePersonService.getPersonDefaultOrganise(SecurityHolder.get().currentPerson().getId()).getId());
                }
            }
            String status = "";

            for (ArchiveDataQuery.QueryItem item : query.getQueryItems()) {
                if (item.getKey().equals(ArchiveEntity.STATUS_COLUMN_KEY)) {
                    status = item.getValue();
                }
            }
            List<ArchiveOrder> archiveOrders = commonMapper.selectByQuery(SqlQuery.from(ArchiveOrder.class)
                    .equal(ArchiveOrderInfo.CATEGORYID, query.getCategoryId())
                    .equal(ArchiveOrderInfo.CODE, status)
                    .equal(ArchiveOrderInfo.ARCHIVETYPE, query.getArchiveType())
                    .orderBy(ArchiveOrderInfo.ORDERBY));
            if (archiveOrders.size() > 0) {
                for (ArchiveOrder order : archiveOrders) {
                    if (order.getOrderType().equals("asc")) {
                        sqlQuery.orderBy(wrapper.getColumn(order.getFieldOrder()));
                    } else {
                        sqlQuery.orderByDesc(wrapper.getColumn(order.getFieldOrder()));
                    }
                }
            } else {
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_YEAR));
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_FILETIME));
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            }
            /*if ("ascending".equals(query.getOrderType())) {
                sqlQuery.orderBy(wrapper.getColumn(query.getOrderKey()));
            } else if ("dscending".equals(query.getOrderType())) {
                sqlQuery.orderByDesc(wrapper.getColumn(query.getOrderKey()));
            } else {
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            }*/
        });
    }


    @Override
    public void adjustFormData(String ids, String formId, String fondId, String categoryId) {
        //根据档案id集合与formId查出所有档案信息
        String[] allIds = ids.split(",");
        for (String id : allIds) {
            FormData formData = formDataService.selectOneFormData(formId, id);

            //所有档案的信息修改：全宗修改，分类修改
            Category categoryById = categoryService.selectById(categoryId);
            formData.put(ArchiveEntity.COLUMN_CATEGORY_CODE, categoryById.getCode());
            Fond fondById = fondService.selectById(fondId);
            formData.put(ArchiveEntity.COLUMN_FOND_CODE, fondById.getCode());

            //将所有档案保存在新的表单表中，重新生成档号
            CategoryFormQuery query = new CategoryFormQuery();
            query.setFondId(fondId);
            query.setCategoryId(categoryId);
            query.setCategoryCode(categoryById.getCategoryType());

            //TODO 调整功能有问题1、级联卷内目录 2、目前会挂接到案卷和卷内中
            List<FormModel> formModelList = categoryService.getCategoryForm(query);
            Assert.isTrue(formModelList.size() > 0, "该分类未配置表单方案，请先配置表单方案！");
            formModelList.forEach(formModel -> {
                String newFormId = formModel.getId();
                formData.setFormDefinitionId(newFormId);
                formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, "");
                codingSchemeService.builderArchiveCode(formData);
                formDataService.removeFormData(formId, id);
                formDataService.addFormData(formData);
            });
        }
    }

    @Override
    public void updateStatusByQuery(ArchiveDataQuery query, String status) {
        List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
        for (FormData formData : formDataList) {
            ArchiveDataStatusChangeEvent event = new ArchiveDataStatusChangeEvent(
                    formData.getFormDefinitionId(),
                    formData.getId(),
                    formData.getString(StatusEntity.STATUS_COLUMN_KEY),
                    status
            );
            formData.put(ArchiveEntity.COLUMN_STATUS, status);
            formDataService.updateFormDataIgnoreNullById(formData);
            applicationEventPublisher.publishEvent(event);
            //查询是否存在卷内文件
            getJNQuery(formData, status);
        }
    }

    /**
     * 处理进入正式库前的数据
     */
    @Override
    public void updateStatusByFormData(FormData formData, String status) {
        ArchiveDataStatusChangeEvent event = new ArchiveDataStatusChangeEvent(
                formData.getFormDefinitionId(),
                formData.getId(),
                formData.getString(StatusEntity.STATUS_COLUMN_KEY),
                status
        );
        formData.put(ArchiveEntity.COLUMN_STATUS, status);
        formDataService.updateFormDataIgnoreNullById(formData);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * 获得案卷中的卷内文件的查询条件
     * @param formData 案卷信息
     * @return
     */
    @Override
    public void getJNQuery(FormData formData, String status){
        Fond fond = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
        if("1".equals(category.getArchiveType())){//案卷类型
            CategoryConfig categoryConfig = commonMapper.selectOneByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, category.getId()));
            ArchiveDataQuery dataQuery = new ArchiveDataQuery();
            List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
            items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_AJDH, formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE), ArchiveDataQuery.QueryType.EQUAL));
            dataQuery.setFormDefinitionId(categoryConfig.getFileFormId());
            dataQuery.setQueryItems(items);
            updateStatusByQuery(dataQuery, status);
        }
    }

    @Override
    public void updateHaveYuanwen(String categoryId, String id, String formId, String haveYuanwen) {
        FormData formData = formDataService.selectOneFormData(formId, id);
        updateHaveYuanwenByFormData(categoryId, formData, haveYuanwen);
    }

    @Override
    public void updateHaveYuanwenByFormData(String categoryId, FormData formData, String haveYuanwen) {
        formData.put(ArchiveEntity.COLUMN_YW_HAVE, haveYuanwen);
        ArchiveDataEditEvent event = new ArchiveDataEditEvent(categoryId, formData, true);
        formDataService.updateFormDataIgnoreNullById(formData);
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void groupDocument(String ajFormDefinitionId, String wjFormDefinitionId, String ajId, String wjIds, String fondId, String categoryId) {
        FormData aj = selectOneFormData(ajFormDefinitionId, ajId);
        for (String wjId : wjIds.split(",")) {
            FormData wj = selectOneFormData(wjFormDefinitionId, wjId);
            wj.put(ArchiveEntity.COLUMN_AJDH, aj.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            updateFormData(wj, fondId, categoryId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disassemble(String ajFormDefinitionId, String wjFormDefinitionId, String ids, String fondId, String categoryId) {
        for (String ajId : ids.split(",")) {
            FormData aj = selectOneFormData(ajFormDefinitionId, ajId);
            ArchiveDataQuery query = new ArchiveDataQuery();
            List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
            items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_AJDH, aj.get(ArchiveEntity.COLUMN_ARCHIVE_CODE), ArchiveDataQuery.QueryType.EQUAL));
            query.setFormDefinitionId(wjFormDefinitionId);
            query.setQueryItems(items);
            List<FormData> wjs = findDataByQuery(query);
            for (FormData wj : wjs) {
                wj.put(ArchiveEntity.COLUMN_AJDH, "");
                updateFormData(wj, fondId, categoryId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertFile(String ajFormDefinitionId, String wjFormDefinitionId, String ajArchiveCode, String ids, String fondId, String categoryId) {
        ArchiveDataQuery query = new ArchiveDataQuery();
        List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
        items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_ARCHIVE_CODE, ajArchiveCode, ArchiveDataQuery.QueryType.EQUAL));
        query.setFormDefinitionId(ajFormDefinitionId);
        query.setQueryItems(items);
        List<FormData> ajs = findDataByQuery(query);
        if (ajs.size() != 1) {
            return false;
        }
        for (String wjId : ids.split(",")) {
            FormData wj = selectOneFormData(wjFormDefinitionId, wjId);
            wj.put(ArchiveEntity.COLUMN_AJDH, ajArchiveCode);
            updateFormData(wj, fondId, categoryId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFile(String formDefinitionId, String ids, String fondId, String categoryId) {
        for (String wjId : ids.split(",")) {
            FormData wj = selectOneFormData(formDefinitionId, wjId);
            wj.put(ArchiveEntity.COLUMN_AJDH, "");
            updateFormData(wj, fondId, categoryId);
        }
    }

    @Override
    public void afterPropertiesSet() {
        archiveDataPlugins = applicationContext.getBeansOfType(ArchiveDataPlugin.class)
                .values()
                .stream().sorted(OrderComparator.INSTANCE)
                .collect(Collectors.toList());
    }

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


   /* @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryByQuery(ArchiveDataQuery query, String newCategoryId, String oldCategoryId) {
        Category cateOld = categoryService.selectById(oldCategoryId);
        Category category = categoryService.selectById(newCategoryId);
        Category category1 = categoryService.selectById(category.getParentId());
        CategoryConfig categoryConfig = categoryConfigService.selectOneByCategoryId(newCategoryId);
        List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
        for (FormData formData : formDataList) {
            String formDataId = formData.getId();
            MoveGoRecording moveGoRecording = new MoveGoRecording();
            moveGoRecording.setOrganName(formData.get("ORG_CODE"));
            moveGoRecording.setWJLX(formData.get("WJLX"));
            List<FileInfo> fileInfos = commonFileService.list(formData.getId(), "archive", "default");
            formData.setId(UUID.randomUUID().toString());
            fileInfos.forEach(fileInfo -> {
                commonFileService.addFile(fileInfo.getFileHash(), formData.getId(), "archive");
            });

            //处理案卷的卷内文件类型
            if(cateOld != null && "1".equals(cateOld.getArchiveType())){
                CategoryConfig categoryConfigOld = categoryConfigService.selectOneByCategoryId(oldCategoryId);
                ArchiveDataQuery dataQuery = new ArchiveDataQuery();
                List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
                items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_AJDH, formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE), ArchiveDataQuery.QueryType.EQUAL));
                dataQuery.setFormDefinitionId(categoryConfigOld.getFileFormId());
                dataQuery.setQueryItems(items);
                updateCategoryByQuery(dataQuery, newCategoryId, "");
            }

            deleteFormData(query.getCategoryId(), formData.getFormDefinitionId(), formDataId);
            formData.put("CATE_GORY_CODE", category.getCode());
            formData.put("WJLX", category.getName());
            if(cateOld != null && "1".equals(cateOld.getArchiveType())){//案卷
                formData.setFormDefinitionId(categoryConfig.getArcFormId());
            }else{
                //卷内
                formData.setFormDefinitionId(categoryConfig.getFileFormId());
            }

            if (!StringUtils.isEmpty(category1)) {
                formData.put("ORG_CODE", category1.getName());
            }
            moveGoRecording.setOrganNameNew(category.getName());
            moveGoRecording.setWJLXNew(category.getName());
            insertFormData(formData, query.getFondId(), newCategoryId);
            ArchiveDataEditEvent event = new ArchiveDataEditEvent(query.getCategoryId(), formData, true);
            applicationEventPublisher.publishEvent(event);
            moveGoRecordingService.insert(moveGoRecording);
        }
    }*/
   @Override
   @Transactional(rollbackFor = Exception.class)
   public void updateCategoryByQuery(ArchiveDataQuery query, String newCategoryId, String oldCategoryId) {
       Category category = categoryService.selectById(newCategoryId);
       Category category1 = categoryService.selectById(category.getParentId());
       CategoryConfig categoryConfig = categoryConfigService.selectOneByCategoryId(newCategoryId);
       List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
       //通过表单id查到其表单数据
       FormDefinition formDefinition = (FormDefinition)formDefinitionService.selectFormDefinitionById(query.getFormDefinitionId());
       for (FormData formData : formDataList) {
           String formDataId = formData.getId();
           MoveGoRecording moveGoRecording = new MoveGoRecording();
           moveGoRecording.setOrganName(formData.get("ORG_CODE"));
           moveGoRecording.setWJLX(formData.get("WJLX"));
           //因为下面要删除表单数据，如果是案卷类型表单，查到它对应的卷内表单数据
           List<FormData> wjs = getWJS(formData);
           String id=UUID.randomUUID().toString();
           //查到旧档案的原文，给档案一个新的id并添加原文
           List<FileInfo> fileInfos = commonFileService.list(formData.getId(), "archive", "default");
           fileInfos.forEach(fileInfo -> {
               commonFileService.addFile(fileInfo.getFileHash(), id, "archive");
           });
           //删除旧档案及其原文
           deleteFormData(query.getCategoryId(), formData.getFormDefinitionId(), formDataId);
           formData.setId(id);
           formData.put("CATE_GORY_CODE", category.getCode());
           formData.put("WJLX", category.getName());
           //判断旧档案所在表单是否为案卷表单
           if ("1".equals(formDefinition.getFormType())){
               //设置表单id为新表单的案卷类型
               formData.setFormDefinitionId(categoryConfig.getArcFormId());
               CategoryConfig cateConfig = categoryConfigService.selectOneByCategoryId(query.getCategoryId());
               //如果有卷内文件，也作相应移动（同案卷）
               if (wjs.size()>0){
                   for (FormData wj : wjs) {
                       String id1=UUID.randomUUID().toString();
                       List<FileInfo> fileInfo = commonFileService.list(wj.getId(), "archive", "default");
                       fileInfo.forEach(file -> {
                           commonFileService.addFile(file.getFileHash(), id1, "archive");
                       });
                       deleteFormData(query.getCategoryId(), wj.getFormDefinitionId(), wj.getId());
                       wj.setId(id1);
                       wj.put("CATE_GORY_CODE", category.getCode());
                       wj.put("WJLX", category.getName());
                       wj.setFormDefinitionId(cateConfig.getFileFormId());
                       insertFormData(wj, query.getFondId(),newCategoryId);
                   }
               }
           }else {
               formData.setFormDefinitionId(categoryConfig.getFileFormId());
           }
           if (!StringUtils.isEmpty(category1)) {
               formData.put("ORG_CODE", category1.getName());
           }
           moveGoRecording.setOrganNameNew(category.getName());
           moveGoRecording.setWJLXNew(category.getName());
           //添加档案到新的表单中
           insertFormData(formData, query.getFondId(), newCategoryId);
           ArchiveDataEditEvent event = new ArchiveDataEditEvent(query.getCategoryId(), formData, true);
           applicationEventPublisher.publishEvent(event);
           moveGoRecordingService.insert(moveGoRecording);
       }
   }
    public List<FormData> getWJS(FormData formData){
        Fond fond = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
        if("1".equals(category.getArchiveType())){//案卷类型
            CategoryConfig categoryConfig = commonMapper.selectOneByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, category.getId()));
            ArchiveDataQuery dataQuery = new ArchiveDataQuery();
            List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
            items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_AJDH, formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE), ArchiveDataQuery.QueryType.EQUAL));
            dataQuery.setFormDefinitionId(categoryConfig.getFileFormId());
            dataQuery.setQueryItems(items);
            List<FormData> wjs = formDataService.selectFormData(dataQuery.getFormDefinitionId(), newBuilder(dataQuery));
            return wjs;
        }
        return null;
    }
    @Override
    public void updateStatusByQueryPlus(ArchiveDataQuery query, String status) {
        //todo 查询出退回数据，
        //移交入库批次记录
        RegisterWarehousing insert = registerWarehousingService.insertReg(status);

        //查询出表单类型，判断是文件还是
        FormModel formModel = formDefinitionService.selectFormDefinitionById(query.getFormDefinitionId());

        List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
        for (FormData formData : formDataList) {
            ArchiveDataStatusChangeEvent event = new ArchiveDataStatusChangeEvent(
                    formData.getFormDefinitionId(),
                    formData.getId(),
                    formData.getString(StatusEntity.STATUS_COLUMN_KEY),
                    status
            );
            formData.put(ArchiveEntity.COLUMN_STATUS, status);
            formDataService.updateFormDataIgnoreNullById(formData);
            applicationEventPublisher.publishEvent(event);
            if(StringUtils.hasText(insert.getId())){
                registerWarehousingDetailsService.insertFormData(formData, insert.getId(),formModel.getFormType());
            }
            //查询是否存在卷内文件
            List<FormData> wjs = getJNFormData(formData);
            //修改卷内文件的状态并添加到长期保存库
            if(wjs != null && wjs.size() > 0){
                for (FormData wj : wjs) {
                    updateStatusByFormData(wj, status);
                    //TODO 将卷内文件保存进详情中。
                    if(StringUtils.hasText(insert.getId())){
                        registerWarehousingDetailsService.insertFormData(wj, insert.getId(),"2");
                    }
                }
            }
        }
        if(StringUtils.hasText(insert.getId())){
            registerWarehousingDetailsService.enUpdateById(insert.getId(), formDataList.size());
        }
    }

    /**
     * 获得案卷内的卷内文件
     */
    @Override
    public List<FormData> getJNFormData(FormData formData){
        Fond fond = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
        if("1".equals(category.getArchiveType())){//案卷类型
            CategoryConfig categoryConfig = commonMapper.selectOneByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, category.getId()));
            ArchiveDataQuery dataQuery = new ArchiveDataQuery();
            List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
            items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_AJDH, formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE), ArchiveDataQuery.QueryType.EQUAL));
            //添加判断是否在同一个门类代码下
            items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_CATEGORY_CODE, formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), ArchiveDataQuery.QueryType.EQUAL));
            dataQuery.setFormDefinitionId(categoryConfig.getFileFormId());
            dataQuery.setQueryItems(items);
            List<FormData> jnwj = formDataService.selectFormData(dataQuery.getFormDefinitionId(), newBuilder(dataQuery));
            return jnwj;
        }
        return null;
    }
}
