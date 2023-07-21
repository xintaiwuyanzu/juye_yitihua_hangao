package com.dr.archive.statistics.service.impl;

import com.alibaba.excel.EasyExcel;
import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;
import com.dr.archive.appraisal.service.AppraisalStatisticsService;
import com.dr.archive.enums.CategoryType;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondInfo;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.manage.handover.service.HandOverStatics;
import com.dr.archive.manage.template.entity.CompilationTemplate;
import com.dr.archive.manage.template.service.CompilationTemplateService;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.offline.service.OfflineStaticsService;
import com.dr.archive.receive.online.service.OnlineStaticsService;
import com.dr.archive.statistics.entity.ReportGenerate;
import com.dr.archive.statistics.entity.ResourceStatistics;
import com.dr.archive.statistics.entity.ResourceStatisticsInfo;
import com.dr.archive.statistics.service.ReportGenerateService;
import com.dr.archive.statistics.service.ResourceStatisticsService;
import com.dr.archive.util.Constants;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

/**
 * @author Mr.Zhu
 * @date 2022/4/18 - 10:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceStatisticsServiceImpl extends DefaultBaseService<ResourceStatistics> implements ResourceStatisticsService {
    @Autowired
    ResourceManager resourceManager;
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryConfigService categoryConfigService;
    @Autowired
    protected OrganisePersonService organisePersonService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    ArchiveFormDefinitionService archiveFormService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    DataBaseService dataBaseService;
    @Autowired
    FormNameGenerator formNameGenerator;
    final ExecutorService executorService = Executors.newFixedThreadPool(1);
    @Autowired
    CommonFileConfig fileConfig;
    @Autowired
    ReportGenerateService generateService;
    static final Version VERSION_2_3_0 = new Version("2.3.0");
    @Autowired
    AppraisalStatisticsService appraisalService;
    @Autowired
    OfflineStaticsService offlineStaticsService;
    @Autowired
    OnlineStaticsService onlineStaticsService;
    @Autowired
    HandOverStatics handOverStatics;
    @Autowired
    CompilationTemplateService compilationTemplateService;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Autowired
    CommonFileService commonFileService;
    @Override
    public List<ResourceStatistics> getResourceStatisticsListByPeronRole(String fondCode, String categoryCode, String year) {
        return commonMapper.selectByQuery(SqlQuery.from(ResourceStatistics.class)
                .in(ResourceStatisticsInfo.FONDCODE, getCurrentFondCodes())
                .equal(ResourceStatisticsInfo.FONDCODE, fondCode)
                .equal(ResourceStatisticsInfo.CATEGORYCODE, categoryCode)
                .equal(ResourceStatisticsInfo.VINTAGES, year));
    }

    @Override
    public List<ResourceStatistics> getResouceStatisticsCount(ResourceStatistics resourceStatistics, String startYear, String endYear) {
        return commonMapper.selectByQuery(SqlQuery.from(ResourceStatistics.class, false)
                .column(ResourceStatisticsInfo.CATEGORYNAME, ResourceStatisticsInfo.CATEGORYCODE, ResourceStatisticsInfo.CREATEDATE, ResourceStatisticsInfo.UPDATEDATE,
                        ResourceStatisticsInfo.ARCARCHIVENUM.sum(), ResourceStatisticsInfo.FILEARCHIVENUM.sum(),
                        ResourceStatisticsInfo.ARCFILENUM.sum(), ResourceStatisticsInfo.FILEFILENUM.sum(),
                        ResourceStatisticsInfo.FILEFILESIZE.sum(), ResourceStatisticsInfo.ARCFILESIZE.sum())
                .in(ResourceStatisticsInfo.FONDCODE, getCurrentFondCodes())
                .equal(ResourceStatisticsInfo.FONDCODE, resourceStatistics.getFondCode())
                .equal(ResourceStatisticsInfo.CATEGORYCODE, resourceStatistics.getCategoryCode())
                .equal(ResourceStatisticsInfo.VINTAGES, resourceStatistics.getVintages())
                .greaterThanEqual(ResourceStatisticsInfo.VINTAGES, startYear)
                .lessThanEqual(ResourceStatisticsInfo.VINTAGES, endYear)
                .groupBy(ResourceStatisticsInfo.CATEGORYNAME)
                .orderByDesc(ResourceStatisticsInfo.FILEARCHIVENUM));
    }

    @Override
    public long insert(ResourceStatistics entity) {
        Assert.isTrue(!StringUtils.isEmpty(entity.getFondCode()), "全宗号不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(entity.getCategoryCode()), "分类号不能为空！");
        //绑定全宗信息
        Fond fond = fondService.findFondByCode(entity.getFondCode());
        entity.setFondId(Optional.ofNullable(entity.getId()).orElse(fond.getId()));
        entity.setFondName(Optional.ofNullable(entity.getFondName()).orElse(fond.getName()));
        //绑定门类信息
        Category category = categoryService.findCategoryByCode(entity.getCategoryCode(), entity.getFondId());
        entity.setCategoryId(Optional.ofNullable(entity.getCategoryId()).orElse(category.getId()));
//        entity.setCategoryName(Optional.ofNullable(entity.getCategoryName()).orElse(category.getName()));
        if (category.getName().equals(getOneLevelCategory(category))) {
            entity.setCategoryName(category.getName());
        } else {
            entity.setCategoryName("【" + getOneLevelCategory(category) + "】-" + category.getName());
        }
        return super.insert(entity);
    }

    /**
     * 查询一级门类名称
     *
     * @param category
     * @return
     */
    private String getOneLevelCategory(Category category) {
        Assert.isTrue(null != category, "分类不能为空！");
        if (category.getParentId().equals(category.getFondId())) {
            return category.getName();
        } else {
            return getOneLevelCategory(categoryService.selectById(category.getParentId()));
        }
    }

    List<ResourceStatistics> getResourceStatisticsByFondCodeAndCategoryCode(String fondCode, String categoryCode, String year) {
        return commonMapper.selectByQuery(SqlQuery.from(ResourceStatistics.class).equal(ResourceStatisticsInfo.FONDCODE, fondCode).equal(ResourceStatisticsInfo.CATEGORYCODE, categoryCode).equal(ResourceStatisticsInfo.VINTAGES, year));
    }

    /**
     * 是否正在运行
     */
    AtomicBoolean isRuning = new AtomicBoolean();
    /**
     * 运行完成后是否需要重新统计
     */
    AtomicBoolean needRecount = new AtomicBoolean();

    @Override
    public synchronized boolean count() {
//        if (isRuning.get()) {
//            needRecount.set(true);
//        } else {
//            doCount();
//        }
//        return isRuning.get();
        doStatistics();
        return true;
    }

    private void doCount() {
        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
        executorService.execute(() -> {
            SecurityHolder.set(securityHolder);
            isRuning.set(true);
            //这里面写统计具体逻辑
            doStatistics();
            isRuning.compareAndSet(true, false);
            if (needRecount.get()) {
                doCount();
            }
        });
    }

    /**
     * 开始统计
     * 1、查找所有表单
     * 2、删除临时表数据
     * 3、把表单数据放到临时表中
     * 4、获取所有分类
     * 5、获取一级门类 TODO 目前没有合并到一级门类，按照所有分类进行统计的
     * 6、把分类编码 改成门类编码
     * 7、向统计表中添加数据
     * 8、TODO 档案馆应该只统计移交过的，未移交过的不应该统计
     */
    void doStatistics() {
        //清空表
        delete(SqlQuery.from(ResourceStatistics.class));
        //查询出全宗 TODO 先查全宗  根据用户权限查全宗
//        List<Fond> fonds = fondService.selectList(SqlQuery.from(Fond.class).equal(FondInfo.ENABLED, true));
        List<Fond> fonds = (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");
        fonds.forEach(fond ->{
            //查询出当前全宗下门类
            List<Category> categoryList = categoryService.getCategoryByFondId(fond.getId());
            categoryList.forEach(category -> {
                CategoryConfig categoryConfig = categoryConfigService.selectOneByCategoryId(category.getId());
                if (!StringUtils.isEmpty(categoryConfig)){
                    if(String.valueOf(CategoryType.ARC.getCode()).equals(category.getArchiveType())){
                        //案卷
                        if (!StringUtils.isEmpty(categoryConfig.getArcFormId())){//案卷表单
                            getCountFile(categoryConfig.getArcFormId(), fond.getCode(), category.getCode(),"aj", categoryConfig.getFileFormId());
                        }
                    }else {
                        //文件
                        if (!StringUtils.isEmpty(categoryConfig.getFileFormId())){
                            getCountFile(categoryConfig.getFileFormId(), fond.getCode(), category.getCode(),"wj", "");
                        }
                    }
                }
            });
        });
/*
        //把表单数据放到临时表中
        if (formList.size() > 0) {
            staticMapper.deleteForm("null");
            formList.forEach(formModel -> staticMapper.statisticsForm("f_" + formModel.getFormCode()));//TODO 这里需要替换mapper方式 需要删掉
        }

        //获取所有分类
        List<Category> allCategoryList = categoryService.selectList(SqlQuery.from(Category.class));
        //门类编码去重
        List<Category> singleCategoryList = allCategoryList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Category::getCode))), ArrayList::new));
        //获取一级门类
        List<Category> categoryList = new ArrayList<>();
        singleCategoryList.forEach(category -> {
            if (category.getParentId().equals(category.getFondId())) {
                categoryList.add(category);
            }
        });
        //把分类编码 改成门类编码
        categoryList.forEach(category -> {
            List<String> categoryCodeList = categoryService.getAllChildrenCategoryByParentId(category.getId()).stream().map(Category::getCode).collect(Collectors.toList());
            if (categoryCodeList.size() > 0) {
                List<ResourceStatisticsTemp> resourceStatisticsTemps = commonMapper.selectByQuery(SqlQuery.from(ResourceStatisticsTemp.class).in(ResourceStatisticsTempInfo.CATEGORYCODE, categoryCodeList));
                if (resourceStatisticsTemps.size() > 0) {
                    resourceStatisticsTemps.forEach(resourceStatisticsTemp -> {
                        if (StringUtils.hasText(category.getCode()) && StringUtils.hasText(resourceStatisticsTemp.getCategoryCode())) {
                            staticMapper.updateForm(category.getCode(), resourceStatisticsTemp.getCategoryCode());
                        }
                    });
                }
            }
        });

        List<ResourceStatisticsTemp> subCategoryCodeList = resourceStatisticsTempService.selectList(SqlQuery.from(ResourceStatisticsTemp.class));
        subCategoryCodeList.forEach(resourceStatisticsTemp -> {
            if (StringUtils.hasText(resourceStatisticsTemp.getFondCode()) && StringUtils.hasText(resourceStatisticsTemp.getCategoryCode())) {
                //向统计表中添加数据
                insert(new ResourceStatistics(resourceStatisticsTemp.getFondCode(), resourceStatisticsTemp.getCategoryCode(), 0, 0, 0, resourceStatisticsTemp.getFileArchiveNum(), 0, 0, resourceStatisticsTemp.getVINTAGES()));
            }
        });*/
    }
    public void getCountFile(String formDefinitionId, String fondCode, String categoryCode, String code, String jnId) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        if (!"4".equals(formModel.getFormType())) {//卷内文件
            Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
            SqlQuery sqlQuery = SqlQuery.from(tableInfo, false)
                    .column(tableInfo.getColumn(ArchiveEntity.ID_COLUMN_NAME).count().alias("fileArchiveNum"),
                            tableInfo.getColumn(ArchiveEntity.COLUMN_FOND_CODE),
                            tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE),
                            tableInfo.getColumn(ArchiveEntity.COLUMN_YEAR),
                            tableInfo.getColumn(ArchiveEntity.COLUMN_ORGANISEID)
                    )
                    .equal(tableInfo.getColumn(ArchiveEntity.COLUMN_FOND_CODE), fondCode)
                    .equal(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), categoryCode);
            sqlQuery.groupBy(tableInfo.getColumn(ArchiveEntity.COLUMN_FOND_CODE))
                    .groupBy(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE))
                    .groupBy(tableInfo.getColumn(ArchiveEntity.COLUMN_ORGANISEID))
                    .groupBy(tableInfo.getColumn(ArchiveEntity.COLUMN_YEAR)).setReturnClass(Map.class);
            List<Map> list = commonMapper.selectByQuery(sqlQuery);

            list.forEach(map -> {
                long wj = 0;
                long aj = 0;
                String orgId = (String) map.get("ORGANISEID");
                if ("wj".equals(code)) {
                    wj = null == map.get("fileArchiveNum") ? 0 : Long.parseLong(map.get("fileArchiveNum") + "");
                    //获得文件原文数量和大小
                    Map fileMap =  getFile(formDefinitionId, fondCode, categoryCode, map.get("VINTAGES") + "");
                    insert(new ResourceStatistics(null == map.get("FOND_CODE") ? "0" : map.get("FOND_CODE") + "",
                            null == map.get("CATE_GORY_CODE") ? "0" : map.get("CATE_GORY_CODE") + "",
                            0, 0, 0,
                            wj,
                            null == fileMap.get("count") ? 0 : Long.parseLong(fileMap.get("count") + ""),
                            null == fileMap.get("size") ? 0 : Long.parseLong(fileMap.get("size") + ""),
                            null == map.get("VINTAGES") ? "0" : map.get("VINTAGES") + "",orgId));
                } else {
                    aj = null == map.get("fileArchiveNum") ? 0 : Long.parseLong(map.get("fileArchiveNum") + "");
                    //获得案卷原文数量和大小
                    Map ajFileMap =  getFile(formDefinitionId, fondCode, categoryCode, map.get("VINTAGES") + "");
                    Map jnMap = getJNCountByAJ(jnId, fondCode, categoryCode, (String) map.get("VINTAGES"));
                    insert(new ResourceStatistics(null == map.get("FOND_CODE") ? "0" : map.get("FOND_CODE") + "",
                            null == map.get("CATE_GORY_CODE") ? "0" : map.get("CATE_GORY_CODE") + "",
                            aj,
                            null == ajFileMap.get("count") ? 0 : Long.parseLong(ajFileMap.get("count") + ""),
                            null == ajFileMap.get("size") ? 0 : Long.parseLong(ajFileMap.get("size") + ""),
                            null == jnMap.get("jnCount") ? 0: Long.parseLong(jnMap.get("jnCount") + ""),
                            null == jnMap.get("jnFileCount") ? 0 : Long.parseLong(jnMap.get("jnFileCount") + ""),
                            null == jnMap.get("jnFileSize") ? 0 : Long.parseLong(jnMap.get("jnFileSize") + ""),
                            null == map.get("VINTAGES") ? "0" : map.get("VINTAGES") + "",orgId));
                }

            });
        }
    }

    public Map getJNCountByAJ(String jnId, String fondCode, String categoryCode, String year) {
        FormModel ajModel = formDefinitionService.selectFormDefinitionById(jnId);
        Relation tableAJ = dataBaseService.getTableInfo(formNameGenerator.genTableName(ajModel), MODULE_NAME);
        SqlQuery sqlQuery = SqlQuery.from(tableAJ, false)
                .column(tableAJ.getColumn(ArchiveEntity.ID_COLUMN_NAME))
                .isNotNull(tableAJ.getColumn(ArchiveEntity.COLUMN_AJDH))
                .equal(tableAJ.getColumn(ArchiveEntity.COLUMN_FOND_CODE), fondCode)
                .equal(tableAJ.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), categoryCode)
                .equal(tableAJ.getColumn(ArchiveEntity.COLUMN_YEAR), year).setReturnClass(HashMap.class);

        List<Map> list = commonMapper.selectByQuery(sqlQuery);
        Map jnFileMap =  getFile(jnId, fondCode, categoryCode, year);
        Map map = new HashMap();
        map.put("jnFileCount", jnFileMap.get("count"));
        map.put("jnFileSize", jnFileMap.get("size"));
        map.put("jnCount", list.size());
        return map;

    }
    public Map getFile(String formDefinitionId, String fondCode, String categoryCode, String year){

        long count = 0;
        long size = 0;

        FormModel fileModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);

        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(fileModel), MODULE_NAME);
        SqlQuery sqlQuery = SqlQuery.from(tableInfo, false)
                .column(tableInfo.getColumn(ArchiveEntity.ID_COLUMN_NAME))
                .equal(tableInfo.getColumn(ArchiveEntity.COLUMN_FOND_CODE), fondCode)
                .equal(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), categoryCode)
                .equal(tableInfo.getColumn(ArchiveEntity.COLUMN_YEAR), year).setReturnClass(HashMap.class);

        List<Map> fileList = commonMapper.selectByQuery(sqlQuery);
        for (Map map1 : fileList) {
            List<FileInfo> fileInfoList = commonFileService.list(map1.get("id") + "", "archive", "default");
            for (FileInfo file : fileInfoList) {
                count++;
                size = size + file.getFileSize();
            }
        }
        Map map = new HashMap();
        map.put("count", count);
        map.put("size", Math.round(size/1024));
        return map;
    }
    @Override
    public List<FormDefinition> getForms(String type) {
        FormDefinitionQuery formDefinitionQuery = new FormDefinitionQuery();
        formDefinitionQuery.typeEqual(type);//TODO 这里只统计的文件类型的表单数据
        return (List<FormDefinition>) archiveFormService.findFormList(formDefinitionQuery);
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {
        Organise organise = SecurityHolder.get().currentOrganise();
        String organiseType = organise.getOrganiseType();
        if (!StringUtils.hasText(organiseType)) {
            organiseType = Constants.ORG_TYPE_DAG;
        }
        File file = null;
        try {
            String fileName = null;
            if (Constants.ORG_TYPE_DAG.equals(organiseType)) {
                //下载档案馆年度报表
                file = getClassPathFile("report" + File.separator + "dang_an_guan.xlsx");
                fileName = "档案馆基本情况表（市、区、县）.xlsx";
            } else if (Constants.ORG_TYPE_DAS.equals(organiseType)) {
                //下载档案室年度报表
                file = getClassPathFile("report" + File.separator + "dang_an_shi.xlsx");
                fileName = "档案行政管理部门（市、区、县）.xlsx";
            } else if (Constants.ORG_TYPE_DAJ.equals(organiseType)) {
                //下载档案局年度报表
                file = getClassPathFile("report" + File.separator + "dang_an_shi_xingzheng.xlsx");
                fileName = "档案室基本情况表.xlsx";
            }
            downloadDoc(file, fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取resource的文件
    public File getClassPathFile(String fileLocation) throws IOException {
        File file = new File(fileConfig.getFullDirPath("resourceStatics", "temp_xls", null) + File.separator + fileLocation);
        //如果files中没有该文件，把资源路径的文件拷贝到file上，前台不能直接下载资源路径下的文件
        if (!file.exists()) {
            ClassPathResource resource = new ClassPathResource(fileLocation);
            try (InputStream inputStream = resource.getInputStream()) {
                FileUtils.copyToFile(inputStream, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    //资源统计生成报表
    @Override
    public boolean generateReport(List<ResourceStatistics> data, ResourceStatistics resourceStatistics, HttpServletRequest request) {
        //easyexcel生成excel
        //return generateByEasyExcel(data, resourceStatistics, "generate" + File.separator + "resource", File.separator + "zhiyuantongji.xlsx", "generateReport", "导出资源统计报表");

        //freemarker生成excel
        return commonGenerateReport(data, resourceStatistics, File.separator + "generate" + File.separator + "resource", "zhiyuantongji.xml", "导出资源统计报表", request);
    }

    //公共模板填充数据方法
    private boolean commonGenerateReport(Object data, ResourceStatistics resourceStatistics, String resourceLocation, String templateXml, String option, HttpServletRequest request) {
        //构造模板list数据，接受map类型
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("data", data);
        // 配置模板属性
        Template template;
        Configuration configuration = new Configuration(VERSION_2_3_0);
        configuration.setDefaultEncoding("UTF-8");

        //file：生成的excel
        String fileName = System.currentTimeMillis() + ".xls";
        File file = new File(fileConfig.getFullDirPath("resourceStatics", "generateReport", new Date()) + File.separator + fileName);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            request.setCharacterEncoding("UTF-8");
            configuration.setClassForTemplateLoading(ResourceStatisticsServiceImpl.class, resourceLocation);
            //获取模板文件
            template = configuration.getTemplate(templateXml);
            // 将填充数据填入模板文件并输出到目标文件
            template.process(dataMap, out);
            String path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("files")).replace("files", "");
            return saveGenerateRecord(resourceStatistics, path, option);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //工作量统计生成报表
    @Override
    public boolean workloadReport(HttpServletRequest request, int type, String fondCode, String vintages) {
        boolean b = false;
        String resourceLocation = File.separator + "generate" + File.separator + "workload";
        ArrayList<Object> list;
        Date date = new Date();
        ResourceStatistics resourceStatistics = new ResourceStatistics();
        resourceStatistics.setFondCode(fondCode);
        resourceStatistics.setVintages(vintages);
        if (type == 1) {
            List<ArchiveAppraisalTask> appraisalTasks = appraisalService.countByFondAndVintagesAndType("", fondCode, vintages, "");
            list = new ArrayList<>();
            for (ArchiveAppraisalTask appraisalTask : appraisalTasks) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("vintages", appraisalTask.getVintages());
                if (!map.containsKey("isTime")) {
                    map.put("isTime", 0);
                }
                if (!map.containsKey("open")) {
                    map.put("open", 0);
                }
                String appraisalType = appraisalTask.getAppraisalType();
                int num = Integer.parseInt(appraisalTask.getId());
                if (appraisalType.equals("到期鉴定数量")) {
                    map.put("open", num);
                } else if (appraisalType.equals("开放鉴定数量")) {
                    map.put("isTime", num);
                }
                list.add(map);
            }
            b = commonGenerateReport(list, resourceStatistics, resourceLocation, "dang_an_jianding.xml", "导出档案鉴定数量报表", request);
        } else {
            try {
                String templateXml;
                String optionName;
                list = new ArrayList<>();
                if (StringUtils.hasText(vintages)) {
                    String dateStr = vintages + "-01-01 00:00:00";
                    date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateStr);
                }
                Map<Object, Object> map;
                if (type == 2) {
                    map = offlineStaticsService.statics(fondCode, StringUtils.hasText(vintages) ? date.getTime() : 0);
                    templateXml = "dang_an_lixianguidang.xml";
                    optionName = "导出档案离线归档数量报表";
                } else if (type == 3) {
                    map = onlineStaticsService.statics(fondCode, StringUtils.hasText(vintages) ? date.getTime() : 0);
                    templateXml = "dang_an_zaixianguidang.xml";
                    optionName = "导出档案在线归档数量报表";
                } else {
                    map = handOverStatics.statics(fondCode, StringUtils.hasText(vintages) ? date.getTime() : 0);
                    templateXml = "dang_an_yijiao.xml";
                    optionName = "导出档案移交数量报表";
                }
                Set<Object> set = map.keySet();
                for (Object key : set) {
                    HashMap<Object, Object> hashMap = new HashMap<>();
                    hashMap.put("year", key + "");
                    hashMap.put("num", map.get(key));
                    list.add(hashMap);
                }
                b = commonGenerateReport(list, resourceStatistics, resourceLocation, templateXml, optionName, request);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ResourceStatistics> statisticByFond(ResourceStatistics query) {
        SqlQuery<ResourceStatistics> sqlQuery = SqlQuery.from(ResourceStatistics.class, false)
                .column(ResourceStatisticsInfo.FONDCODE,
                        ResourceStatisticsInfo.FONDID,
                        ResourceStatisticsInfo.FONDNAME,
                        ResourceStatisticsInfo.CATEGORYCODE,
                        ResourceStatisticsInfo.CATEGORYNAME,
                        ResourceStatisticsInfo.ORGNAME,
                        ResourceStatisticsInfo.ARCARCHIVENUM.sum(),
                        ResourceStatisticsInfo.FILEARCHIVENUM.sum(),
                        ResourceStatisticsInfo.ARCFILENUM.sum(),
                        ResourceStatisticsInfo.FILEFILENUM.sum(),
                        ResourceStatisticsInfo.ARCFILESIZE.sum(),
                        ResourceStatisticsInfo.FILEFILESIZE.sum(),
                        ResourceStatisticsInfo.FILESIZE.sum()
                )
                .like(ResourceStatisticsInfo.CATEGORYNAME, query.getCategoryName())
                .equal(ResourceStatisticsInfo.CATEGORYCODE, query.getCategoryCode())
                .equal(ResourceStatisticsInfo.VINTAGES, query.getVintages())
                .in(ResourceStatisticsInfo.FONDCODE, getCurrentFondCodes())
                .groupBy(ResourceStatisticsInfo.FONDID)
                .orderByDesc(ResourceStatisticsInfo.FILEARCHIVENUM);
        return commonMapper.selectByQuery(sqlQuery);
    }

    @Override
    public List<ResourceStatistics> statisticByCategory(ResourceStatistics query) {
        SqlQuery<ResourceStatistics> sqlQuery = SqlQuery.from(ResourceStatistics.class, false)
                .column(ResourceStatisticsInfo.FONDCODE,
                        ResourceStatisticsInfo.FONDID,
                        ResourceStatisticsInfo.FONDNAME,
                        ResourceStatisticsInfo.CATEGORYCODE,
                        ResourceStatisticsInfo.CATEGORYNAME,
                        ResourceStatisticsInfo.ORGNAME,
                        ResourceStatisticsInfo.ARCARCHIVENUM.sum(),
                        ResourceStatisticsInfo.FILEARCHIVENUM.sum(),
                        ResourceStatisticsInfo.ARCFILENUM.sum(),
                        ResourceStatisticsInfo.FILEFILENUM.sum(),
                        ResourceStatisticsInfo.ARCFILESIZE.sum(),
                        ResourceStatisticsInfo.FILEFILESIZE.sum(),
                        ResourceStatisticsInfo.FILESIZE.sum()
                )
                .equal(ResourceStatisticsInfo.FONDCODE, query.getFondCode())
                .in(ResourceStatisticsInfo.FONDCODE, getCurrentFondCodes())
                .equal(ResourceStatisticsInfo.VINTAGES, query.getVintages())
                .groupBy(ResourceStatisticsInfo.CATEGORYCODE)
                .orderByDesc(ResourceStatisticsInfo.FILEARCHIVENUM);
        List<ResourceStatistics> resourceStatistics = commonMapper.selectByQuery(sqlQuery);
        Collections.sort(resourceStatistics, (o1, o2) -> Long.compare(o2.getFileArchiveNum(), o1.getFileArchiveNum()));
        return resourceStatistics;
    }

    @Override
    public List<ResourceStatistics> statisticByCategoryAndYear(long s, long e) {
        SqlQuery<ResourceStatistics> sqlQuery = SqlQuery.from(ResourceStatistics.class, false)
                .column(ResourceStatisticsInfo.FONDCODE,
                        ResourceStatisticsInfo.FONDID,
                        ResourceStatisticsInfo.FONDNAME,
                        ResourceStatisticsInfo.CATEGORYCODE,
                        ResourceStatisticsInfo.CATEGORYNAME,
                        ResourceStatisticsInfo.ORGNAME,
                        ResourceStatisticsInfo.ARCARCHIVENUM.sum(),
                        ResourceStatisticsInfo.FILEARCHIVENUM.sum(),
                        ResourceStatisticsInfo.ARCFILENUM.sum(),
                        ResourceStatisticsInfo.FILEFILENUM.sum(),
                        ResourceStatisticsInfo.ARCFILESIZE.sum(),
                        ResourceStatisticsInfo.FILEFILESIZE.sum(),
                        ResourceStatisticsInfo.FILESIZE.sum()
                )
                .greaterThanEqual(ResourceStatisticsInfo.VINTAGES, s)
                .lessThanEqual(ResourceStatisticsInfo.VINTAGES, e)
                .groupBy(ResourceStatisticsInfo.CATEGORYCODE)
                .orderByDesc(ResourceStatisticsInfo.FILEARCHIVENUM);
        List<ResourceStatistics> resourceStatistics = commonMapper.selectByQuery(sqlQuery);
        Collections.sort(resourceStatistics, (o1, o2) -> Long.compare(o2.getFileArchiveNum(), o1.getFileArchiveNum()));
        return resourceStatistics;
    }

    @Override
    public List<ResourceStatistics> statisticByYear(ResourceStatistics query) {
        SqlQuery<ResourceStatistics> sqlQuery = SqlQuery.from(ResourceStatistics.class, false)
                .column(ResourceStatisticsInfo.FONDCODE,
                        ResourceStatisticsInfo.FONDID,
                        ResourceStatisticsInfo.FONDNAME,
                        ResourceStatisticsInfo.CATEGORYCODE,
                        ResourceStatisticsInfo.CATEGORYNAME,
                        ResourceStatisticsInfo.ORGNAME,
                        ResourceStatisticsInfo.VINTAGES,
                        ResourceStatisticsInfo.ARCARCHIVENUM.sum(),
                        ResourceStatisticsInfo.FILEARCHIVENUM.sum(),
                        ResourceStatisticsInfo.ARCFILENUM.sum(),
                        ResourceStatisticsInfo.FILEFILENUM.sum(),
                        ResourceStatisticsInfo.ARCFILESIZE.sum(),
                        ResourceStatisticsInfo.FILEFILESIZE.sum(),
                        ResourceStatisticsInfo.FILESIZE.sum()
                )
                .equal(ResourceStatisticsInfo.FONDCODE, query.getFondCode())
                .in(ResourceStatisticsInfo.FONDCODE, getCurrentFondCodes())
                .equal(ResourceStatisticsInfo.CATEGORYCODE, query.getCategoryCode())
                .groupBy(ResourceStatisticsInfo.VINTAGES)
                .orderBy(ResourceStatisticsInfo.VINTAGES);
        return commonMapper.selectByQuery(sqlQuery);
    }

    //保存导出记录
    private boolean saveGenerateRecord(ResourceStatistics statistics, String path, String option) {
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        ReportGenerate report = new ReportGenerate();
        report.setPersonId(person.getUserCode());
        report.setPersonName(person.getUserName());

        if (StringUtils.hasText(statistics.getFondCode())) {
            Fond fond = fondService.findFondByCode(statistics.getFondCode());
            report.setFondCode(fond.getCode());
            report.setFondName(fond.getName());
        }

        List<Fond> fonds = fondOrganiseService.getFondListByOrganiseId(organise.getId());
        if (fonds.size()>0){
            if (!"admin".equals(person.getUserCode())) {
                Fond fond = fonds.get(0);
                report.setFondName(fond.getName());
                report.setFondCode(fond.getCode());
            }
        }else{
            report.setFondName(organise.getOrganiseName());
        }



        report.setOrganizeName(organise.getOrganiseName());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        report.setReportAnnual(String.valueOf(year));
        report.setReportPath(path);
        report.setOptionName(option);
        return generateService.insert(report) > 0;
    }

    //下载报表
    public void downloadDoc(File file, String fileName, HttpServletResponse response) {
        if (file == null) return;
        if (!StringUtils.hasText(fileName)) {
            fileName = file.getName();
        }
        //获取文件完整路径
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            response.reset();
            response.setHeader("Content-Type", "multipart/form-data");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            OutputStream out = response.getOutputStream();
            out.write(bytes);
            fis.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //easyexcel导出excel
    public boolean generateByEasyExcel(List data, ResourceStatistics resourceStatistics, String templateFilePath, String fileName, String mineType, String option) {
        try {
            String templatePath = new ClassPathResource(templateFilePath + fileName).getFile().getAbsolutePath();
            // 一下子全部放到内存里面 并填充
            String generateFilePath = fileConfig.getFullDirPath("resourceStatics", mineType, new Date()) + fileName;
            // 这里 会填充到第一个sheet， 然后文件流会自动关闭
            EasyExcel.write(generateFilePath).withTemplate(templatePath).sheet().doFill(data);
            return saveGenerateRecord(resourceStatistics, generateFilePath.substring(generateFilePath.indexOf("files")).replace("files", ""), option);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected List<Fond> getCurrentFonds() {
        SecurityHolder securityHolder = SecurityHolder.get();
        Assert.notNull(securityHolder, "用户未登录！");
        Assert.notNull(securityHolder.currentPerson(), "用户未登录！");
        return (List<Fond>) resourceManager.getPersonResources(securityHolder.currentPerson().getId(), FondService.RESOURCE_TYPE_FOND);
    }

    protected String[] getCurrentFondCodes() {
        return getCurrentFonds().stream().map(f -> f.getCode()).toArray(String[]::new);
    }


    @Override
    public String getStatisticByCategory(String templateId, Management management) {
        Assert.isTrue(StringUtils.hasText(templateId), "模板编码不能为空！");
        Assert.isTrue(StringUtils.hasText(management.getVintagesStart()) && StringUtils.hasText(management.getVintagesEnd()), "起止年度不能为空！");
        //获取模板
        CompilationTemplate compilationTemplate = compilationTemplateService.selectById(templateId);
//        Assert.isTrue(management1.size() > 0, "未配置该类型模板！");
        //获取某年度馆藏档案类型的数量
        Map<String, Object> dataMap = new HashMap<>();
        //
        List<ResourceStatistics> resourceStatistics = statisticByCategoryAndYear(Long.parseLong(management.getVintagesStart()), Long.parseLong(management.getVintagesEnd()));
        String result = "";
        //
        String compilationContent = compilationTemplate.getCompilationContent();
        dataMap.put("compilationTitle", management.getTitle());
        dataMap.put("vintagesStart", management.getVintagesStart());
        dataMap.put("vintagesEnd", management.getVintagesEnd());

        if (resourceStatistics.size() > 0) {
            dataMap.put("archiveCarDetailList", resourceStatistics);
            if (StringUtils.isEmpty(compilationContent)) {
                return "模板未存在内容";
            }
        }

        result = compilationTemplateService.getHtml(dataMap, compilationContent);

        //获取某年度档案利用情况
        //获取某年度档案接收档案状况
        return result;
    }


}
