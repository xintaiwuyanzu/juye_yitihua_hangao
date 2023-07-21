package com.dr.archive.fuzhou.portal.service.impl;

import com.dr.archive.fuzhou.portal.service.PortalService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.impl.DefaultArchiveDataManager;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

@Service
public class PortalServiceImpl implements PortalService {
    @Autowired
    ResourceManager resourceManager;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    DataBaseService dataBaseService;
    @Autowired
    FormNameGenerator formNameGenerator;
    @Autowired
    CommonFileService commonFileService;
    final ExecutorService executorService = Executors.newFixedThreadPool(10);
    final ExecutorService executorService1 = Executors.newFixedThreadPool(10);
    @Value("${portal.portalSystem}")
    private String portalSystem;

    @Override
    public List<Map> resourceByCateGory(String userId) {
        List<Map> list = new ArrayList<>();
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(userId, "fond");
        CountDownLatch countDownLatch = new CountDownLatch(Math.round(fondList.size()));
        for (Fond fond : fondList) {
            executorService.execute(() -> {

                SqlQuery<Category> equal = SqlQuery.from(Category.class)
                        .equal(CategoryInfo.FONDID, fond.getId())
                        .equal(CategoryInfo.PARENTID, fond.getId());

                List<Category> categories = commonMapper.selectByQuery(equal);

                CountDownLatch latch = new CountDownLatch(Math.round(categories.size()));

                for (Category category1 : categories) {

                    executorService1.execute(() -> {

                        List<CategoryConfig> categoryConfigs = commonMapper.selectByQuery(SqlQuery.from(CategoryConfig.class)
                                .equal(CategoryConfigInfo.BUSINESSID, category1.getId()));

                        int wj = 0;
                        for (CategoryConfig categoryConfig : categoryConfigs) {

                            if (!StringUtils.isEmpty(categoryConfig.getFileFormId())) {
                                FormModel formModel = null;
                                try {
                                    formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getFileFormId());
                                } catch (Exception ignored) {

                                }
                                Map<String, Integer> map = countByCateGory(formModel, category1.getCode());

                                wj += map.get("ml");
                            }
                        }
                        Map map = new HashMap();
                        map.put("name", category1.getName());
                        List<Map> category2 = getCategory(fond.getId(), category1.getId(), 1);
                        List<Map> maps = getCategory(fond.getId(), category1.getId(), 0);
                        if (category2.size() > 0) {
                            list.addAll(category2);
                            for (Map mmap : category2) {
                                wj += Integer.parseInt(mmap.get("value").toString());
                            }
                        }
                        map.put("value", wj); //取文件数量

                        list.add(map);
                        latch.countDown();
                    });
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {

        }
        return list;
    }

    public List<Map> getCategory(String fondId, String id, int flag) {
        List<Map> list = new ArrayList<>();

        List<Category> categories = commonMapper.selectByQuery(SqlQuery.from(Category.class)
                .equal(CategoryInfo.FONDID, fondId)
                .equal(CategoryInfo.PARENTID, id));

        for (Category category : categories) {

            List<CategoryConfig> categoryConfigs = commonMapper.selectByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, category.getId()));

            int wj = 0;

            for (CategoryConfig categoryConfig : categoryConfigs) {

                if (!StringUtils.isEmpty(categoryConfig.getFileFormId())) {
                    FormModel formModel = null;
                    try {
                        formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getFileFormId());
                    } catch (Exception ignored) {

                    }
                    Map<String, Integer> map = countByCateGory(formModel, category.getCode());

                    wj += map.get("ml");
                }
            }
            Map map = new HashMap();
            if (flag == 0) {
                map.put("name", category.getName());
                map.put("id", UUID.randomUUID().toString());
                List<Map> category2 = getCategory(fondId, category.getId(), flag);
                if (category2.size() > 0) {
                    map.put("children", category2);
                    for (Map mmap : category2) {
                        wj += Integer.parseInt(mmap.get("value").toString());
                    }
                }
                map.put("value", wj);
            } else {
                map.put("name", category.getName());
                List<Map> category2 = getCategory(fondId, category.getId(), flag);
                List<Map> maps = getCategory(fondId, category.getId(), 0);
                if (category2.size() > 0) {
                    list.addAll(category2);
                    for (Map mmap : maps) {
                        wj += Integer.parseInt(mmap.get("value").toString());
                    }
                }
                map.put("value", wj);
            }
            list.add(map);

        }

        return list;
    }

    public Map<String, Integer> countByCateGory(FormModel formModel, String category) {

        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);

        SqlQuery<Object> from = SqlQuery.from(tableInfo);

        if (!StringUtils.isEmpty(category)) {
            from.equal(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), category);
        }

        List<Map> list = commonMapper.selectByQuery(from.setReturnClass(Map.class));
        Map<String, Integer> map = new HashMap();
        map.put("ml", list.size());
        return map;
    }

    @Autowired
    CategoryConfigService categoryConfigService;
    @Autowired
    DefaultArchiveDataManager defaultArchiveDataManager;
    @Autowired
    ArchiveDataManager dataManager;

    @Override
    public int openCount(String userId) {
        int openCount = 0;
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(userId, "fond");
        //根据全宗查出所有的分类

        for (Fond fond : fondList) {

            SqlQuery<Category> equal = SqlQuery.from(Category.class)
                    .equal(CategoryInfo.FONDID, fond.getId())
                    .equal(CategoryInfo.PARENTID, fond.getId());
            List<Category> categories = commonMapper.selectByQuery(equal);//第一级分类
            SqlQuery<Category> equal2 = SqlQuery.from(Category.class, false)
                    .column(CategoryInfo.ID)
                    .equal(CategoryInfo.FONDID, fond.getId());
            List<String> parentids = commonMapper.selectByQuery(equal2.setReturnClass(String.class));
            SqlQuery<Category> equal3 = SqlQuery.from(Category.class)
                    .equal(CategoryInfo.FONDID, fond.getId())
                    .in(CategoryInfo.PARENTID, parentids);
            List<Category> categories2 = commonMapper.selectByQuery(equal3);//除第一级分类 的其他分类判断依据：在该全宗下，父分类也在该全宗下
            categories.addAll(categories2);
            for (Category category : categories) {
                List<CategoryConfig> categoryConfigs = categoryConfigService.selectByCategoryId(category.getId());
                if (categoryConfigs.size() > 0) {
                    String fileFormdefineId = categoryConfigs.get(0).getFileFormId();
                    String arcFormdefineId = categoryConfigs.get(0).getArcFormId();

                    // List<FormData> formDats =defaultArchiveDataManager.findDataByQuery(fileFormdefineId);

                    ArchiveDataQuery archiveDataQuery = new ArchiveDataQuery();
                    archiveDataQuery.setCategoryId(category.getId());
                    archiveDataQuery.setFormDefinitionId(fileFormdefineId);
                    archiveDataQuery.setFondId(fond.getId());
                    List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
                    ArchiveDataQuery.QueryItem item = new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_OPEN_SCOPE, "开放", ArchiveDataQuery.QueryType.EQUAL);
                    ArchiveDataQuery.QueryItem item2 = new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_CATEGORY_CODE, category.getCode(), ArchiveDataQuery.QueryType.EQUAL);
                    items.add(item);
                    items.add(item2);
                    archiveDataQuery.setQueryItems(items);
                    List<FormData> formDats = defaultArchiveDataManager.findDataByQuery(archiveDataQuery);
                    // Page<FormData> formDataPage = dataManager.formDataPage(archiveDataQuery, 1, 1000);
                    openCount += formDats.size();
                }

            }
        }

        return openCount;
    }

    @Override
    public String getPortalSystem() {
        return portalSystem;
    }
}
