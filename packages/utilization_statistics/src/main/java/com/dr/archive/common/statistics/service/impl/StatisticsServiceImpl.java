package com.dr.archive.common.statistics.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractArchiveBatchInfo;
import com.dr.archive.common.statistics.service.StatisticsService;
import com.dr.archive.common.statistics.utils.Constants;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondInfo;
import com.dr.archive.manage.fond.entity.FondOrganiseInfo;
import com.dr.archive.manage.task.entity.AppraisalBatchDetail;
import com.dr.archive.manage.task.entity.AppraisalBatchDetailInfo;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

/**
 * @author caiwb
 * @version 1.0.0
 * @ClassName StatisticsServiceImpl.java
 * @Description 档案统计业务层
 * @createTime 2021年08月18日
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    CommonFileService commonFileService;

    @Autowired
    FormDefinitionService formDefinitionService;

    @Autowired
    DataBaseService dataBaseService;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    FormNameGenerator formNameGenerator;

    @Autowired
    OrganisePersonService organisePersonService;

    @Autowired
    ResourceManager resourceManager;

    final ExecutorService executorService = Executors.newFixedThreadPool(10);

    final ExecutorService executorService1 = Executors.newFixedThreadPool(10);

    /*
     * @Author caiwb
     * @Description //根据起始年度和全宗id统计档案数量
     * @Date 18:16 2021/8/18
     * @Param
     * @return
     **/
    @Override
    public Map count() {

        String startYear = "1990";
        String endYear = "2023";
        String fondId = "c3a18928-be44-4cbd-89aa-68ec3858d19e";

        AtomicLong wj = new AtomicLong();
        AtomicLong aj = new AtomicLong();
        AtomicLong jh = new AtomicLong();

        List<Map> list1 = commonMapper.selectByQuery(SqlQuery.from(Category.class)
                .join(CategoryInfo.ID, CategoryConfigInfo.BUSINESSID)
                .column(CategoryConfigInfo.ARCFORMID, CategoryConfigInfo.FILEFORMID)
                .equal(CategoryInfo.FONDID, fondId).setReturnClass(Map.class));

        list1.forEach(ca -> {
            if (!StringUtils.isEmpty(ca.get("arcFormId"))) {

                FormModel formModel = formDefinitionService.selectFormDefinitionById(ca.get("arcFormId").toString());

                long count = getCount(formModel, startYear, endYear);

                if ("0".equals(formModel.getFormType())) {
                    wj.addAndGet(count);
                }
                if ("1".equals(formModel.getFormType())) {
                    aj.addAndGet(count);
                }
                if ("3".equals(formModel.getFormType())) {
                    jh.addAndGet(count);
                }
            }

            if (!StringUtils.isEmpty(ca.get("fileFormId"))) {

                FormModel formModel = formDefinitionService.selectFormDefinitionById(ca.get("fileFormId").toString());

                long count = getCount(formModel, startYear, endYear);

                if ("0".equals(formModel.getFormType())) {
                    wj.addAndGet(count);
                }
                if ("1".equals(formModel.getFormType())) {
                    aj.addAndGet(count);
                }
                if ("3".equals(formModel.getFormType())) {
                    jh.addAndGet(count);
                }
            }
        });

        Map map = new HashMap();
        map.put("wj", wj);
        map.put("aj", aj);
        map.put("jh", jh);
        return map;
    }

    //根据起始时间和全宗id查询
    @Override
    public List<Map> resource(String startTime, String endTime, String id, int flag) {
        List<Fond> fonds = new ArrayList<>();
        //如果全宗ID不为空则根据全宗id进行查询，否则查询当前登录用户下的所有全宗
        if (!StringUtils.isEmpty(id)) {
            fonds.addAll(commonMapper.selectByQuery(SqlQuery.from(Fond.class).equal(FondInfo.ID, id)));

        } else {
            fonds.addAll(commonMapper.selectByQuery(SqlQuery.from(Fond.class).join(FondInfo.ID, FondOrganiseInfo.FONDID)
                    .equal(FondOrganiseInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId())
                    .equal(FondInfo.ENABLED, StatusEntity.STATUS_ENABLE)));
        }
        List<Map> maps = new ArrayList<>();
        Set<String> set = new HashSet<>();
        //遍历全宗，根据全宗ID查询该全宗下面的所有门类分类和表单id
        for (Fond fond : fonds) {
            List<Map> list = commonMapper.selectByQuery(SqlQuery.from(Category.class)
                    .join(CategoryInfo.ID, CategoryConfigInfo.BUSINESSID)
                    .column(CategoryConfigInfo.ARCFORMID, CategoryConfigInfo.FILEFORMID)
                    .equal(CategoryInfo.FONDID, fond.getId()).setReturnClass(Map.class));
            for (Map ca : list) {
                long aj = 0;
                long wj = 0;
                long jh = 0;
                long yw = 0;
                Map count = new HashMap();
                if (!StringUtils.isEmpty(ca.get("arcFormId"))) {
                    FormModel formModel = formDefinitionService.selectFormDefinitionById(ca.get("arcFormId").toString());
                    long count1 = getCount(formModel, ca.get("code").toString(), false, startTime, endTime);
                    yw += getCount(formModel, ca.get("code").toString(), startTime, endTime);
                    if ("0".equals(formModel.getFormType())) {
                        wj += count1;
                    }
                    if ("1".equals(formModel.getFormType())) {
                        aj += count1;
                    }
                    if ("3".equals(formModel.getFormType())) {
                        jh += count1;
                    }
                }
                if (!StringUtils.isEmpty(ca.get("fileFormId"))) {
                    FormModel formModel = formDefinitionService.selectFormDefinitionById(ca.get("fileFormId").toString());
                    long count1 = getCount(formModel, ca.get("code").toString(), false, startTime, endTime);
                    yw += getCount(formModel, ca.get("code").toString(), startTime, endTime);
                    if ("0".equals(formModel.getFormType())) {
                        wj += count1;
                    }
                    if ("1".equals(formModel.getFormType())) {
                        aj += count1;
                    }
                    if ("3".equals(formModel.getFormType())) {
                        jh += count1;
                    }
                }
                count.put("wj", wj);
                count.put("jh", jh);
                count.put("aj", aj);
                count.put("yw", yw);
                count.put("lx", ca.get("categoryType"));
                set.add(ca.get("categoryType").toString());
                maps.add(count);
            }
        }
        List<Map> list = new ArrayList();
        for (String str : set) {
            long aj = 0;
            long wj = 0;
            long jh = 0;
            long yw = 0;
            Map ma = new HashMap();
            for (Map map : maps) {
                if (str.equals(map.get("lx"))) {
                    wj += Long.parseLong(map.get("wj").toString());
                    aj += Long.parseLong(map.get("aj").toString());
                    jh += Long.parseLong(map.get("jh").toString());
                    yw += Long.parseLong(map.get("yw").toString());
                }
            }
            if (flag == 0) {
                ma.put("wj", wj);
                ma.put("jh", jh);
                ma.put("aj", aj);
                ma.put("yw", yw);
                ma.put("lx", str);
            } else {
                ma.put("文件", wj);
                ma.put("件盒", jh);
                ma.put("案卷", aj);
                ma.put("原文", yw);
                if (Constants.CATEGORYTYPE_DOCUMENT.equals(str)) {
                    ma.put("类型", Constants.CATEGORYTYPE_DOCUMENT_);
                } else if (Constants.CATEGORYTYPE_PERSONNEL.equals(str)) {
                    ma.put("类型", Constants.CATEGORYTYPE_PERSONNEL_);
                } else if (Constants.CATEGORYTYPE_OTHER.equals(str)) {
                    ma.put("类型", Constants.CATEGORYTYPE_OTHER_);
                } else {
                    ma.put("类型", Constants.CATEGORYTYPE_ACCOUNTANT_);
                }
            }
            list.add(ma);
        }
        return list;
    }

    @Override
    public List<Map> report() {

        List<Fond> fonds = commonMapper.selectByQuery(SqlQuery.from(Fond.class).equal(FondInfo.ENABLED, StatusEntity.STATUS_ENABLE));

        List<Map> maps = new ArrayList<>();
        long open = 0;
        for (Fond fond : fonds) {
            List<Map> list = commonMapper.selectByQuery(SqlQuery.from(Category.class)
                    .join(CategoryInfo.ID, CategoryConfigInfo.BUSINESSID)
                    .column(CategoryConfigInfo.ARCFORMID, CategoryConfigInfo.FILEFORMID)
                    .equal(CategoryInfo.FONDID, fond.getId()).setReturnClass(Map.class));

            for (Map ca : list) {
                Map count = new HashMap();
                if (!StringUtils.isEmpty(ca.get("arcFormId"))) {
                    FormModel formModel = formDefinitionService.selectFormDefinitionById(ca.get("arcFormId").toString());
                    open += getCount(formModel);
                }
                if (!StringUtils.isEmpty(ca.get("fileFormId"))) {
                    FormModel formModel = formDefinitionService.selectFormDefinitionById(ca.get("fileFormId").toString());
                    open += getCount(formModel);
                }
            }
        }
        Map map = new HashMap();
        map.put("open", open);
        maps.add(map);
        return maps;
    }

    @Override
    public List<Map> countUtilize(String startTime, String endTime, String orgId, int flag) {

        List<Fond> fonds = new ArrayList<>();
        //如果全宗ID不为空则根据全宗id进行查询，否则查询当前登录用户下的所有全宗
        if (!StringUtils.isEmpty(orgId)) {
            fonds.addAll(commonMapper.selectByQuery(SqlQuery.from(Fond.class).equal(FondInfo.ID, orgId)));

        } else {

            fonds.addAll(commonMapper.selectByQuery(SqlQuery.from(Fond.class).join(FondInfo.ID, FondOrganiseInfo.FONDID)
                    .equal(FondOrganiseInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId())
                    .equal(FondInfo.ENABLED, StatusEntity.STATUS_ENABLE)));
        }

        List<Map> list = new ArrayList<>();

        for (Fond fond : fonds) {

            //Organise organise = organisePersonService.getOrganise(new OrganiseQuery.Builder().idEqual(departmentId).getQuery());
            //TODO 统计分析需要抽象接口
            /*Map map = new HashMap();
            List<String> strings = commonMapper.selectByQuery(SqlQuery.from(ConsultApply.class, false)
                    .column(ConsultApplyInfo.BATCHID)
                    .equal(ConsultApplyInfo.DOCORGID, fond.getId())
                    .setReturnClass(String.class));
            if (strings.size() > 0) {
                SqlQuery<FileCar> in = SqlQuery.from(FileCar.class).in(FileCarInfo.BUSSINESSID, strings);

                if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        long start = simpleDateFormat.parse(startTime).getTime();
                        long end = simpleDateFormat.parse(endTime).getTime() + 86400000;

                        in.between(FileCarInfo.CREATEDATE, start, end);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                long l = commonMapper.countByQuery(in);

                if (flag == 0) {
                    map.put("jg", fond.getName());
                    map.put("sl", l);
                } else {
                    map.put("jg", fond.getName());
                    map.put("sl", l);
                }
                list.add(map);
            }*/

        }

        return list;
    }

    @Override
    public List<Map> countAppraisal(String startTime, String endTime) {
        List<Map> list = new ArrayList<>();
        List<AbstractArchiveBatch> batches = commonMapper.selectByQuery(SqlQuery.from(AbstractArchiveBatch.class, false)
                .column(AbstractArchiveBatchInfo.BATCHTYPE, AbstractArchiveBatchInfo.BATCHNAME)
                .like(AbstractArchiveBatchInfo.BATCHNAME, "鉴定")
                .groupBy(AbstractArchiveBatchInfo.BATCHTYPE, AbstractArchiveBatchInfo.BATCHNAME));


        for (AbstractArchiveBatch batch : batches) {
            SqlQuery<AppraisalBatchDetail> from = SqlQuery.from(AppraisalBatchDetail.class);

            if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    long start = simpleDateFormat.parse(startTime).getTime();
                    long end = simpleDateFormat.parse(endTime).getTime() + 86400000;

                    from.between(AppraisalBatchDetailInfo.CREATEDATE, start, end);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            List<String> strings = commonMapper.selectByQuery(SqlQuery.from(AbstractArchiveBatch.class, false)
                    .column(AbstractArchiveBatchInfo.ID)
                    .equal(AbstractArchiveBatchInfo.BATCHTYPE, batch.getBatchType())
                    .setReturnClass(String.class));

            long l = commonMapper.countByQuery(from.in(AppraisalBatchDetailInfo.BATCHID, strings));

            Map map = new HashMap();

            map.put("batchName", batch.getBatchName());
            map.put("count", l);
            list.add(map);
        }

        return list;
    }

    @Override
    public List<Map> resourceByCateGory(String category, String year, int flag) {

        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");

        List<Map> list = new ArrayList<>();

        CountDownLatch countDownLatch = new CountDownLatch(Math.round(fondList.size()));

        for (Fond fond : fondList) {

            executorService.execute(() -> {

                SqlQuery<Category> equal = SqlQuery.from(Category.class)
                        .equal(CategoryInfo.FONDID, fond.getId())
                        .equal(CategoryInfo.PARENTID, fond.getId());

                if (!StringUtils.isEmpty(category)) {
                    equal.equal(CategoryInfo.CODE, category);
                }

                List<Category> categories = commonMapper.selectByQuery(equal);

                CountDownLatch latch = new CountDownLatch(Math.round(categories.size()));

                for (Category category1 : categories) {

                    executorService1.execute(() -> {


                        List<CategoryConfig> categoryConfigs = commonMapper.selectByQuery(SqlQuery.from(CategoryConfig.class)
                                .equal(CategoryConfigInfo.BUSINESSID, category1.getId()));

                        int wj = 0;
                        int aj = 0;
                        int yw = 0;
                        for (CategoryConfig categoryConfig : categoryConfigs) {

                            if (!StringUtils.isEmpty(categoryConfig.getArcFormId())) {
                                FormModel formModel = null;
                                try {
                                    formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getArcFormId());
                                } catch (Exception ignored) {

                                }
                                Map<String, Integer> map = countByCateGory(formModel, category1.getCode(), year);
                                aj += map.get("ml");
                                yw += map.get("yw");

                            }

                            if (!StringUtils.isEmpty(categoryConfig.getFileFormId())) {
                                FormModel formModel = null;
                                try {
                                    formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getFileFormId());
                                } catch (Exception ignored) {

                                }
                                Map<String, Integer> map = countByCateGory(formModel, category1.getCode(), year);

                                wj += map.get("ml");
                                yw += map.get("yw");
                            }
                        }
                        Map map = new HashMap();
                        if (flag == 0) {
                            map.put("name", category1.getName());
                            map.put("id", UUID.randomUUID().toString());
                            List<Map> category2 = getCategory(fond.getId(), category1.getId(), category, year, flag);
                            if (category2.size() > 0) {
                                map.put("children", category2);
                                for (Map mmap : category2) {
                                    wj += Integer.parseInt(mmap.get("wj").toString());
                                    aj += Integer.parseInt(mmap.get("aj").toString());
                                    yw += Integer.parseInt(mmap.get("yw").toString());
                                }
                            }
                            map.put("wj", wj);
                            map.put("aj", aj);
                            map.put("yw", yw);
                        } else {
                            map.put("分类", category1.getName());
                            map.put("id", UUID.randomUUID().toString());
                            List<Map> category2 = getCategory(fond.getId(), category1.getId(), category, year, flag);
                            List<Map> maps = getCategory(fond.getId(), category1.getId(), category, year, 0);
                            if (category2.size() > 0) {
                                list.addAll(category2);
                                for (Map mmap : maps) {
                                    wj += Integer.parseInt(mmap.get("wj").toString());
                                    aj += Integer.parseInt(mmap.get("aj").toString());
                                    yw += Integer.parseInt(mmap.get("yw").toString());
                                }
                            }
                            map.put("文件", wj);
                            map.put("案卷", aj);
                            map.put("原文", yw);
                        }

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
        /*try {
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }*/
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 查询移交数量，接收数量，开放鉴定数量，到期鉴定数量，按年度分组
     *
     * @param year
     * @return
     */
    @Override
    public Map filemanagement(String year) {

        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> personList = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        List<String> idList = personList.stream().map(Person::getId).collect(Collectors.toList());
        Map map = new HashMap();
        //移交的数量
        /*List<Map> list1 = commonMapper.selectByQuery(SqlQuery.from(Transfer.class, false)
                .column(TransferInfo.MLQUANTITY.sum(), TransferInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')", "year"))
                .equal(TransferInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')"), year)
                .in(TransferInfo.CREATEPERSON, idList)
                .groupBy(new Column("", "year", "year"))
                .equal(TransferInfo.SOURCEORGID, organise.getId())
                .setReturnClass(Map.class));
        map.put("yj", list1);*/
        /*List<Map> list1 = commonMapper.selectByQuery(SqlQuery.from(Transfer.class, false)
                .column(TransferInfo.ID.count("count"), TransferInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')", "year"))
                .groupBy(new Column("", "year", "year"))
                .equal(TransferInfo.STATUS, "3").setReturnClass(Map.class));
        map.put("yj", list1);*/
        //接收的数量
        /*List<Map> list2 = commonMapper.selectByQuery(SqlQuery.from(Filing.class, false)
                .column(FilingInfo.ID.count("count"), FilingInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')", "year"))
                .equal(FilingInfo.STATUS, StatusEntity.STATUS_ENABLE_STR)
                .equal(FilingInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')"), year)
                .in(FilingInfo.CREATEPERSON, idList)
                .groupBy(new Column("", "year", "year"))
                .setReturnClass(Map.class));
        map.put("js", list2);*/
        List<String> list3 = commonMapper.selectByQuery(SqlQuery.from(AbstractArchiveBatch.class, false)
                .column(AbstractArchiveBatchInfo.ID)
                .equal(AbstractArchiveBatchInfo.ORGID, organise.getId())
                .equal(AbstractArchiveBatchInfo.BATCHTYPE, "KFJD")
                .equal(AbstractArchiveBatchInfo.STATUS, StatusEntity.STATUS_ENABLE_STR)
                .setReturnClass(String.class));

        if (list3.size() > 0) {
            //开放鉴定
            List<Map> list4 = commonMapper.selectByQuery(SqlQuery.from(AppraisalBatchDetail.class, false)
                    .column(AppraisalBatchDetailInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')", "year"))
                    .column(AppraisalBatchDetailInfo.ID.count("count"))
                    .in(AppraisalBatchDetailInfo.BATCHID, list3)
                    .in(AppraisalBatchDetailInfo.CREATEPERSON, idList)
                    .equal(AppraisalBatchDetailInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')"), year)
                    //.equal(AppraisalBatchDetailInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')"), "2020")
                    .groupBy(new Column("", "year", "year"))
                    .setReturnClass(Map.class));
            map.put("kfjd", list4);
        }

        List<String> list = commonMapper.selectByQuery(SqlQuery.from(AbstractArchiveBatch.class, false)
                .column(AbstractArchiveBatchInfo.ID)
                .equal(AbstractArchiveBatchInfo.ORGID, organise.getId())
                .equal(AbstractArchiveBatchInfo.BATCHTYPE, "DQJD")
                .equal(AbstractArchiveBatchInfo.STATUS, StatusEntity.STATUS_ENABLE_STR)
                .setReturnClass(String.class));

        if (list.size() > 0) {
            //到期鉴定
            List<Map> list5 = commonMapper.selectByQuery(SqlQuery.from(AppraisalBatchDetail.class, false)
                    .column(AppraisalBatchDetailInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')", "year"))
                    .column(AppraisalBatchDetailInfo.ID.count("count"))
                    .in(AppraisalBatchDetailInfo.BATCHID, list)
                    .in(AppraisalBatchDetailInfo.CREATEPERSON, idList)
                    .equal(AppraisalBatchDetailInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')"), year)
                    //.equal(AppraisalBatchDetailInfo.CREATEDATE.function("FROM_UNIXTIME(createDate/1000,'%%Y')"), "2020")
                    .groupBy(new Column("", "year", "year"))
                    .setReturnClass(Map.class));
            map.put("dqjd", list5);
        }
        return map;
    }

    @Override
    public List<Map> workloadStatistics(String startTime, String endTime, String orgId, int flag) {

        Organise organise = SecurityHolder.get().currentOrganise();

        List<String> strings = commonMapper.selectByQuery(SqlQuery.from(AbstractArchiveBatch.class, false)
                .column(AbstractArchiveBatchInfo.ID)
                .equal(AbstractArchiveBatchInfo.BATCHTYPE, "DQJD")
                .equal(AbstractArchiveBatchInfo.ORGID, organise.getId())
                //.isNull(AbstractArchiveBatchInfo.PARENTID)
                .equal(AbstractArchiveBatchInfo.STATUS, StatusEntity.STATUS_DISABLE_STR)
                .setReturnClass(String.class));
        Map dqsl = new HashMap();

        if (flag == 0) {
            if (strings.size() > 0) {
                long dq = commonMapper.countByQuery(SqlQuery.from(AppraisalBatchDetail.class)
                        .in(AppraisalBatchDetailInfo.BATCHID, strings));
                dqsl.put("sl", dq);
            } else {
                dqsl.put("sl", 0);
            }
            dqsl.put("lx", "到期数量");
        } else {
            if (strings.size() > 0) {
                long dq = commonMapper.countByQuery(SqlQuery.from(AppraisalBatchDetail.class)
                        .in(AppraisalBatchDetailInfo.BATCHID, strings));
                dqsl.put("数量", dq);
            } else {
                dqsl.put("数量", 0);
            }
            dqsl.put("类型", "到期数量");
        }

        List<String> dqyj = commonMapper.selectByQuery(SqlQuery.from(AbstractArchiveBatch.class, false)
                .column(AbstractArchiveBatchInfo.ID)
                .equal(AbstractArchiveBatchInfo.ORGID, organise.getId())
                .equal(AbstractArchiveBatchInfo.BATCHTYPE, "DQYJ")
                .setReturnClass(String.class));

        Map jssl = new HashMap();

        /*Long js = commonMapper.selectOneByQuery(SqlQuery.from(Transfer.class, false)
                .column(TransferInfo.MLQUANTITY.sum())
                .in(TransferInfo.BATCHID, dqyj)
                .setReturnClass(Long.class));

        if (flag == 0) {
            jssl.put("lx", "接收数量");
            if (!StringUtils.isEmpty(js)) {
                jssl.put("sl", js);
            } else {
                jssl.put("sl", 0);
            }
        } else {
            jssl.put("类型", "接收数量");
            if (!StringUtils.isEmpty(js)) {
                jssl.put("数量", js);
            } else {
                jssl.put("数量", 0);
            }
        }
*/
        List<String> kfjd = commonMapper.selectByQuery(SqlQuery.from(AbstractArchiveBatch.class, false)
                .column(AbstractArchiveBatchInfo.ID)
                .equal(AbstractArchiveBatchInfo.BATCHTYPE, "KFJD")
                .equal(AbstractArchiveBatchInfo.ORGID, organise.getId())
                //.isNull(AbstractArchiveBatchInfo.PARENTID)
                .equal(AbstractArchiveBatchInfo.STATUS, StatusEntity.STATUS_DISABLE_STR)
                .setReturnClass(String.class));
        List<Map> maps = null;
        if (flag == 0) {
            maps = commonMapper.selectByQuery(SqlQuery.from(AppraisalBatchDetail.class, false)
                    .column(AppraisalBatchDetailInfo.ID.count().alias("sl"), AppraisalBatchDetailInfo.RECHECKRESULT.alias("lx"))
                    .in(AppraisalBatchDetailInfo.BATCHID, kfjd)
                    .isNotNull(AppraisalBatchDetailInfo.RECHECKRESULT)
                    .groupBy(AppraisalBatchDetailInfo.RECHECKRESULT)
                    .setReturnClass(Map.class));
        } else {
            maps = commonMapper.selectByQuery(SqlQuery.from(AppraisalBatchDetail.class, false)
                    .column(AppraisalBatchDetailInfo.ID.count().alias("数量"), AppraisalBatchDetailInfo.RECHECKRESULT.alias("类型"))
                    .in(AppraisalBatchDetailInfo.BATCHID, kfjd)
                    .isNotNull(AppraisalBatchDetailInfo.RECHECKRESULT)
                    .groupBy(AppraisalBatchDetailInfo.RECHECKRESULT)
                    .setReturnClass(Map.class));
        }
        maps.add(dqsl);
        maps.add(jssl);
        return maps;
    }

    @Override
    public List<Fond> getFond() {

        return (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");
    }

    @Override
    public List<Category> getAllCategory() {

        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");

        List<Category> list = new ArrayList<>();
        for (Fond fond : fondList) {
            SqlQuery<Category> equal = SqlQuery.from(Category.class)
                    .equal(CategoryInfo.FONDID, fond.getId())
                    .equal(CategoryInfo.PARENTID, fond.getId());
            List<Category> categories = commonMapper.selectByQuery(equal);
            list.addAll(categories);
        }
        return list;
       /* SqlQuery<Category> equal = SqlQuery.from(Category.class);
        List<Category> categories = commonMapper.selectByQuery(equal);*/
    }


    public List<Map> getCategory(String fondId, String id, String catecode, String year, int flag) {
        List<Map> list = new ArrayList<>();

        List<Category> categories = commonMapper.selectByQuery(SqlQuery.from(Category.class)
                .equal(CategoryInfo.FONDID, fondId)
                .equal(CategoryInfo.PARENTID, id));

        for (Category category : categories) {

            List<CategoryConfig> categoryConfigs = commonMapper.selectByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, category.getId()));

            int wj = 0;
            int aj = 0;
            int yw = 0;

            for (CategoryConfig categoryConfig : categoryConfigs) {

                if (!StringUtils.isEmpty(categoryConfig.getArcFormId())) {
                    FormModel formModel = null;
                    try {
                        formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getArcFormId());
                    } catch (Exception ignored) {

                    }
                    Map<String, Integer> map = countByCateGory(formModel, category.getCode(), year);
                    aj += map.get("ml");
                    yw += map.get("yw");

                }

                if (!StringUtils.isEmpty(categoryConfig.getFileFormId())) {
                    FormModel formModel = null;
                    try {
                        formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getFileFormId());
                    } catch (Exception ignored) {

                    }
                    Map<String, Integer> map = countByCateGory(formModel, category.getCode(), year);

                    wj += map.get("ml");
                    yw += map.get("yw");
                }
            }
            Map map = new HashMap();
            if (flag == 0) {
                map.put("name", category.getName());
                map.put("id", UUID.randomUUID().toString());
                List<Map> category2 = getCategory(fondId, category.getId(), catecode, year, flag);
                if (category2.size() > 0) {
                    map.put("children", category2);
                    for (Map mmap : category2) {
                        wj += Integer.parseInt(mmap.get("wj").toString());
                        aj += Integer.parseInt(mmap.get("aj").toString());
                        yw += Integer.parseInt(mmap.get("yw").toString());
                    }
                }
                map.put("wj", wj);
                map.put("aj", aj);
                map.put("yw", yw);
            } else {
                map.put("分类", category.getName());
                map.put("id", UUID.randomUUID().toString());
                List<Map> category2 = getCategory(fondId, category.getId(), catecode, year, flag);
                List<Map> maps = getCategory(fondId, category.getId(), catecode, year, 0);
                if (category2.size() > 0) {
                    list.addAll(category2);
                    for (Map mmap : maps) {
                        wj += Integer.parseInt(mmap.get("wj").toString());
                        aj += Integer.parseInt(mmap.get("aj").toString());
                        yw += Integer.parseInt(mmap.get("yw").toString());
                    }
                }
                map.put("文件", wj);
                map.put("案卷", aj);
                map.put("原文", yw);
            }
            list.add(map);

        }

        return list;
    }

    public Map<String, Integer> countByCateGory(FormModel formModel, String category, String year) {

        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);

        SqlQuery<Object> from = SqlQuery.from(tableInfo);

        if (!StringUtils.isEmpty(category)) {
            from.equal(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), category);
        }

        if (!StringUtils.isEmpty(year)) {
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

            // String format = simpleDateFormat.format(new Date(year));

            from.like(tableInfo.getColumn(ArchiveEntity.COLUMN_FILETIME), year);
        }

        List<Map> list = commonMapper.selectByQuery(from.setReturnClass(Map.class));
        int yw = 0;
        if (list.size() > 0) {

            for (Map map : list) {

                yw += commonFileService.count(map.get("id").toString(), "archive");
            }

        }
        Map<String, Integer> map = new HashMap();
        map.put("ml", list.size());
        map.put("yw", yw);
        return map;
    }

    public List<Map> recursionTree(List<TreeNode> children, String category, String year) {
        List<Map> list = new ArrayList<>();

        for (TreeNode treeNode : children) {

            Category data = (Category) treeNode.getData();

            List<CategoryConfig> categoryConfigs = commonMapper.selectByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, data.getId()));
            int wj = 0;
            int aj = 0;
            int yw = 0;

            for (CategoryConfig categoryConfig : categoryConfigs) {
                if (!StringUtils.isEmpty(categoryConfig.getArcFormId())) {
                    FormModel formModel = null;
                    try {
                        formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getArcFormId());
                    } catch (Exception ignored) {

                    }
                    if (!StringUtils.isEmpty(formModel)) {
                        Map<String, Integer> map2 = countByCateGory(formModel, category, year);

                        aj += map2.get("ml");
                        yw += map2.get("yw");
                    }

                }

                if (!StringUtils.isEmpty(categoryConfig.getFileFormId())) {
                    FormModel formModel = null;
                    try {
                        formModel = formDefinitionService.selectFormDefinitionById(categoryConfig.getFileFormId());
                    } catch (Exception ignored) {

                    }
                    if (!StringUtils.isEmpty(formModel)) {
                        Map<String, Integer> map2 = countByCateGory(formModel, category, year);

                        wj += map2.get("ml");
                        yw += map2.get("yw");
                    }
                }
            }
            Map map = new HashMap();
            if (categoryConfigs.size() > 0) {
                map.put("name", data.getName());
                map.put("id", UUID.randomUUID().toString());
                map.put("wj", wj);
                map.put("aj", aj);
                map.put("yw", yw);
            }
            if (!StringUtils.isEmpty(treeNode.getChildren())) {

                map.put("children", recursionTree(treeNode.getChildren(), category, year));

            }
            list.add(map);

        }
        return list;
    }

    /*
     * @Author caiwb
     * @Description //查询开放档案数量
     * @Date 17:08 2021/8/19
     * @Param
     * @return
     **/
    public long getCount(FormModel formModel) {
        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        long count = commonMapper.countByQuery(SqlQuery.from(tableInfo, false)
                /*.equal(tableInfo.getColumn(ArchiveEntity.COLUMN_YEAR).function("A.xml.VINTAGES + A.xml.SAVE_TERM < DATE_FORMAT(NOW(),'%Y')"))*/);

        return commonMapper.countByQuery(SqlQuery.from(tableInfo).equal(tableInfo.getColumn(ArchiveEntity.COLUMN_OPEN_SCOPE), StatusEntity.STATUS_ENABLE));
    }

    public long getCount(FormModel formModel, String startYear, String endYear) {
        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        return commonMapper.countByQuery(SqlQuery.from(tableInfo).between(tableInfo.getColumn(ArchiveEntity.COLUMN_YEAR), startYear, endYear));
    }

    public long getCount(FormModel formModel, String code, boolean yw, String startTime, String endTime) {
        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        SqlQuery<Object> from = SqlQuery.from(tableInfo).equal(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), code);
        if (yw) {
            from.equal(tableInfo.getColumn(ArchiveEntity.COLUMN_YW_HAVE), StatusEntity.STATUS_ENABLE);
        }
        if (!StringUtils.isEmpty(startTime)) {
            from.greaterThanEqual(tableInfo.getColumn(ArchiveEntity.COLUMN_FILETIME).function(" LEFT(FILETIME,4)"), startTime);
        }

        if (!StringUtils.isEmpty(endTime)) {
            from.lessThanEqual(tableInfo.getColumn(ArchiveEntity.COLUMN_FILETIME).function(" LEFT(FILETIME,4)"), endTime);
        }
        return commonMapper.countByQuery(from);
    }

    public long getCount(FormModel formModel, String code, String startTime, String endTime) {
        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        SqlQuery<Map> from = SqlQuery.from(tableInfo).equal(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), code).setReturnClass(Map.class);

        if (!StringUtils.isEmpty(startTime)) {
            from.greaterThanEqual(tableInfo.getColumn(ArchiveEntity.COLUMN_FILETIME).function(" LEFT(FILETIME,4)"), startTime);
        }

        if (!StringUtils.isEmpty(endTime)) {
            from.lessThanEqual(tableInfo.getColumn(ArchiveEntity.COLUMN_FILETIME).function(" LEFT(FILETIME,4)"), endTime);
        }

        int ywcount = commonMapper.selectByQuery(from).stream().mapToInt(map -> (int) commonFileService.count(map.get("id").toString(), "archive")).sum();
        System.out.println(ywcount);
        return ywcount;
    }

}
