package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.*;
import com.dr.archive.appraisal.service.AppraisalBasisService;
import com.dr.archive.appraisal.service.AppraisalOpenRangeService;
import com.dr.archive.appraisal.service.Archive4ToBeAppraisalService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.Constants;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.utilization.search.service.ArchiveFileContentService;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.service.CacheAbleService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.jdbc.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.twelvemonkeys.imageio.metadata.tiff.IFD;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class Archive4ToBeAppraisalServiceImpl extends CacheAbleService<Archive4ToBeAppraisal> implements Archive4ToBeAppraisalService {

    @Autowired
    FondService fondService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    CommonFileService fileService;
    @Autowired
    ArchiveFileContentService archiveFileContentService;
    @Autowired
    AppraisalBasisService appraisalBasisService;
    @Autowired
    AppraisalOpenRangeService appraisalOpenRangeService;

    //正在执行扫描的组织机构集合
    private Map<String, Object> doingScanMap = Collections.synchronizedMap(new HashMap<>());

    //正在等待扫描的组织机构集合
    private Map<String, Object> watiScanMap = Collections.synchronizedMap(new HashMap<>());

    //正在等待扫描的组织机构集合
    private Map<String, Integer> resultPriority = Collections.synchronizedMap(new HashMap<>());

    final ExecutorService executorService = Executors.newFixedThreadPool(10);

    CountDownLatch countDownLatch = new CountDownLatch(0);

    Calendar cal = null;
    int year = 0;

    /**
     * 按照表单信息查询所有档案数据，过滤所有档案是否为需要鉴定
     *
     * @throws InterruptedException
     */
    @Override
    @Async
    public void startScanArchive(SecurityHolder securityHolder) throws InterruptedException {
        resultPriority = appraisalOpenRangeService.getResultPriority();
        watiScanMap.put(securityHolder.currentOrganise().getId(), null);
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        refreshCache();
        while (true) {
            //  获取当前正在扫描的表单
            SqlQuery sqlQuery = SqlQuery.from(AppraisalFormRecord.class)
                    .equal(AppraisalFormRecordInfo.STATUS, "0")
                    .orderBy(AppraisalFormRecordInfo.FORMID);
            List<AppraisalFormRecord> appraisalFormRecordList = commonMapper.selectByQuery(sqlQuery);
            //如果档案扫描表单记录 的数据为空,则重新计算并存储待扫描的数量
            if (appraisalFormRecordList.size() > 0) {
                doScanArchive(appraisalFormRecordList.get(0), securityHolder);
            } else {
                //
                sqlQuery = SqlQuery.from(AppraisalOrgRecord.class)
                        .set(AppraisalOrgRecordInfo.STATUS, "1")
                        .set(AppraisalOrgRecordInfo.ENDTIME, System.currentTimeMillis());
                commonMapper.updateByQuery(sqlQuery);
                endDoingScan();
                //doingScanMap.clear();
                if (watiScanMap.size() > 0) {
                    doingScanMap.clear();
                    startScanMap(securityHolder);
                } else {
                    return;
                }
            }
            countDownLatch.await();
            // 时间超过六点退出当前程序，明天继续执行
//            if(cal.get(Calendar.HOUR_OF_DAY)>6){
//                return;
//            }
        }
    }

    private void doScanArchive(AppraisalFormRecord appraisalFormRecord, SecurityHolder s) {
        //等待线程池任务全部执行完后再继续
        //单表所有待鉴定数据
        long total = formDataService.countId(appraisalFormRecord.getFormId(), (sqlQuery, formRelationWrapper) -> {
            Column column = formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE);
            sqlQuery.column(column, formRelationWrapper.idColumn().count("count"));
        });
        //已经扫描完成的数据
        int finishCount = Integer.parseInt(appraisalFormRecord.getFinishCount());
        int o = (total - finishCount) > PAGE_SIZE ? PAGE_SIZE : (int) (total - finishCount);
        int pageSize = o % 100 > 0 ? (o / 100) + 1 : o / 100;
        countDownLatch = new CountDownLatch(pageSize);
        for (int index = finishCount / 100; index <= pageSize + finishCount / 100; index++) {
            int finalIndex = index;
            executorService.execute(() -> {
                SecurityHolder.set(s);
                refreshArchiveOn100(appraisalFormRecord.getFormId(), finalIndex, s);
                countDownLatch.countDown();
            });
        }
        if (o < PAGE_SIZE) {
            appraisalFormRecord.setStatus("1");
        } else {
            appraisalFormRecord.setFinishCount(finishCount + o + "");
        }
        commonMapper.updateIgnoreNullById(appraisalFormRecord);
    }

    @Override
    public long updateByAppraisalBatch(AppraisalBatch appraisalBatch) {
        SqlQuery sqlQuery = SqlQuery.from(Archive4ToBeAppraisal.class)
                .set(Archive4ToBeAppraisalInfo.BATCHID, appraisalBatch.getId())
                .equal(Archive4ToBeAppraisalInfo.APPRAISALTYPE, appraisalBatch.getAppraisalType())
                .between(Archive4ToBeAppraisalInfo.VINTAGES, appraisalBatch.getStartVintages(), appraisalBatch.getEndVintages())
                .in(Archive4ToBeAppraisalInfo.FONDCODE, appraisalBatch.getFondCodes())
                .isNull(Archive4ToBeAppraisalInfo.BATCHID);
        long upsateCount = updateBySqlQuery(sqlQuery);
        return upsateCount;
    }


    @Override
    public Map statistics(String fondCodes, String appraisalType, String startVintages, String endVintages) {
        String[] fondCodeArray = new String[fondCodes.split(",").length];
        String[] fondIds = fondCodes.split(",");
        int index = 0;
        for (String id : fondIds) {
            fondCodeArray[index] = fondService.selectById(id).getCode();
            index++;
        }
        SqlQuery sqlQuery = SqlQuery.from(Archive4ToBeAppraisal.class, false)
                .column(Archive4ToBeAppraisalInfo.FONDCODE.alias("value1"))
                .column(Archive4ToBeAppraisalInfo.VINTAGES.alias("value2"))
                .column(Archive4ToBeAppraisalInfo.ID.count().alias("count"))
                .isNull(Archive4ToBeAppraisalInfo.BATCHID)
                .in(Archive4ToBeAppraisalInfo.FONDCODE, fondCodeArray)
                .between(Archive4ToBeAppraisalInfo.VINTAGES, startVintages, endVintages)
                .equal(Archive4ToBeAppraisalInfo.APPRAISALTYPE, appraisalType)
                .groupBy(Archive4ToBeAppraisalInfo.FONDCODE, Archive4ToBeAppraisalInfo.VINTAGES)
                .orderBy(Archive4ToBeAppraisalInfo.FONDCODE)
                .orderBy(Archive4ToBeAppraisalInfo.VINTAGES)
                .setReturnClass(CommonCount.class);
        List<CommonCount> commonCountList = commonMapper.selectByQuery(sqlQuery);
        List filedList = new ArrayList();
        List<Map> resultList = new ArrayList();
        Map<String, Map> map = new HashMap();
        for (CommonCount commonCount : commonCountList) {
            String filed = commonCount.getValue2();
            if (StringUtils.isEmpty(filed)) {
                filed = "无年份";
            }
            filedList.add(filed);
            if (map.containsKey(commonCount.getValue1())) {
                map.get(commonCount.getValue1()).put(filed, commonCount.getCount());
            } else {
                Map mapOne = new HashMap();
                mapOne.put("fondCode", commonCount.getValue1());
                mapOne.put(filed, commonCount.getCount());
                map.put(commonCount.getValue1(), mapOne);
            }
        }

        for (String s : map.keySet()) {
            resultList.add(map.get(s));
        }

        Map resultMap = new HashMap();
        resultMap.put("filedList", filedList);
        resultMap.put("countList", resultList);

        return resultMap;
    }

    /**
     * 每个线程最多执行100条档案信息判断，防止内存溢出
     *
     * @param formDefinitionId
     * @param pageIndex
     */
    public void refreshArchiveOn100(String formDefinitionId, int pageIndex, SecurityHolder securityHolder) {
        List<FormData> list = formDataService.selectPageFormData(formDefinitionId, (sqlQuery, formRelationWrapper) -> {
            Column column = formRelationWrapper.getColumn("CREATEDATE");
            sqlQuery.orderBy(column);
        }, pageIndex, 100).getData();
        for (FormData formData : list) {
            //下面十三个旗的账号，不需要判断组织机构id， 分院总管才有组织机构id；
            if (securityHolder.currentOrganise().getOrganiseType().contains("dag")) {
                if (!doingScanMap.containsKey(formData.get(ArchiveEntity.COLUMN_ORGANISEID))) {
                    continue;
                }
            }
            if (StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_TITLE))) {
                //题名为空不处理
                continue;
            }
            if ("".equals(formData.getString(ArchiveEntity.COLUMN_STATUS)) || ArchiveDataManager.STATUS_SPECIAL.equals(formData.getString(ArchiveEntity.COLUMN_STATUS))) {
                //如果档案不是在暂存库或者管理库就不处理
                continue;
            }
            //到期鉴定
            Archive4ToBeAppraisal archive4ToBeAppraisal = selectOneByFormIdAndDataId(formDefinitionId, formData.getId(), Constants.ROLE_DQJD);
            if (archive4ToBeAppraisal == null) {
                archive4ToBeAppraisal = translateData(formData);
            }
            if (StringUtils.isEmpty(archive4ToBeAppraisal.getBatchId())) {
                archive4ToBeAppraisal = isToBeAppraisalExpire(archive4ToBeAppraisal, formData);
                if (Constants.ROLE_DQJD.equals(archive4ToBeAppraisal.getAppraisalType())) {
                    if (StringUtils.isEmpty(archive4ToBeAppraisal.getId())) {
                        insert(archive4ToBeAppraisal);
                    } else {
                        commonMapper.updateIgnoreNullById(archive4ToBeAppraisal);
                    }
//                    List<FileInfo> list1 = fileService.list(archive4ToBeAppraisal.getFormDataId(), "archive", "default");
//                    List<FileInfo> list2 = fileService.list(archive4ToBeAppraisal.getFormDataId(), "manageFile", "default");
//                    if (list1.size() == 0 && list2.size() == 0)
//                        commonMapper.updateByQuery(SqlQuery.from(Archive4ToBeAppraisal.class).equal(Archive4ToBeAppraisalInfo.ID, archive4ToBeAppraisal.getId()).set(Archive4ToBeAppraisalInfo.BATCHID, "1"));
                }
            }

            //开放鉴定
            Archive4ToBeAppraisal archive4ToBeAppraisal2 = selectOneByFormIdAndDataId(formDefinitionId, formData.getId(), Constants.ROLE_KFJD);
            if (archive4ToBeAppraisal2 == null) {
                archive4ToBeAppraisal2 = translateData(formData);
            }
            if (StringUtils.isEmpty(archive4ToBeAppraisal2.getBatchId())) {
                deleteMatching(archive4ToBeAppraisal2);
                archive4ToBeAppraisal2 = isToBeAppraisal4Open(archive4ToBeAppraisal2, formData, securityHolder);
                if (Constants.ROLE_KFJD.equals(archive4ToBeAppraisal2.getAppraisalType())) {
                    if (StringUtils.isEmpty(archive4ToBeAppraisal2.getId())) {
                        insert(archive4ToBeAppraisal2);
                    } else {
                        commonMapper.updateIgnoreNullById(archive4ToBeAppraisal2);
                    }
//                    List<FileInfo> list1 = fileService.list(archive4ToBeAppraisal.getFormDataId(), "archive", "default");
//                    List<FileInfo> list2 = fileService.list(archive4ToBeAppraisal.getFormDataId(), "manageFile", "default");
//                    if (list1.size() == 0 && list2.size() == 0)
//                        commonMapper.updateByQuery(SqlQuery.from(Archive4ToBeAppraisal.class).equal(Archive4ToBeAppraisalInfo.ID, archive4ToBeAppraisal.getId()).set(Archive4ToBeAppraisalInfo.BATCHID, "1"));
                }
            }
        }
    }


    /**
     * 判断档案是否需要进行开放鉴定
     *
     * @param archive4ToBeAppraisal
     * @param formData
     * @return
     */
    public Archive4ToBeAppraisal isToBeAppraisal4Open(Archive4ToBeAppraisal archive4ToBeAppraisal, FormData formData, SecurityHolder securityHolder) {
        //如果档案在无需鉴定的范围内，则直接跳过
//        if(excludeMap.containsKey(archive4ToBeAppraisal.getFondCode()+"_"+archive4ToBeAppraisal.getCategoryCode()+"_"+archive4ToBeAppraisal.getOrganiseId())){
//            return archive4ToBeAppraisal;
//        }
        int Vintages;
        try {
            Vintages = Integer.parseInt(archive4ToBeAppraisal.getVintages());
        } catch (Exception e) {
            return archive4ToBeAppraisal;
        }
        //如果档案不是25年之前的档案，则直接跳过
        if (year - Vintages < 25 && year - Vintages > 0) {
            return archive4ToBeAppraisal;
        }

        archive4ToBeAppraisal.setAppraisalType(Constants.ROLE_KFJD);
        archive4ToBeAppraisal.setAuxiliaryResult("null");
        archive4ToBeAppraisal.setMatchingRules("null");
        boolean isMatching;
        Object o;
        if (securityHolder.currentOrganise().getOrganiseType().contains("dag")) {
            o = cache.get(archive4ToBeAppraisal.getOrganiseId()).get();
        } else {
            o = null;
        }
        if (o == null) {
            return archive4ToBeAppraisal;
        }
        int priority = 999;
        for (Object one : (List<?>) o) {
            AppraisalBasis appraisalBasis = (AppraisalBasis) one;
            for (AppraisalRules appraisalRules : (List<AppraisalRules>) appraisalBasis.getRulesList()) {
                isMatching = false;
                for (AppraisalKeyWord appraisalKeyWord : (List<AppraisalKeyWord>) appraisalRules.getKeyWordList()) {
                    if (archiveDataContrastKeyWord(archive4ToBeAppraisal, appraisalKeyWord)) {
                        isMatching = true;
                    }
                }
                for (AppraisalSpecial appraisalSpecial : (List<AppraisalSpecial>) appraisalRules.getSpecialList()) {
                    if (archiveDataContrastSpecial(archive4ToBeAppraisal, appraisalSpecial, formData)) {
                        isMatching = true;
                        if (StringUtils.isEmpty(archive4ToBeAppraisal.getMatchingSpecial()) || resultPriority.get(appraisalRules.getOpenRange()) < priority) {
                            archive4ToBeAppraisal.setMatchingSpecial(appraisalSpecial.getId());
                        }
                    }
                }
                if (isMatching && resultPriority.get(appraisalRules.getOpenRange()) < priority) {
                    archive4ToBeAppraisal.setAuxiliaryResult(appraisalRules.getOpenRange());
                    archive4ToBeAppraisal.setOpenRange(appraisalRules.getOpenRange());
                    archive4ToBeAppraisal.setMatchingRules(appraisalRules.getId());
                }
            }

        }
        return archive4ToBeAppraisal;
    }

    public boolean archiveDataContrastKeyWord(Archive4ToBeAppraisal archive4ToBeAppraisal, AppraisalKeyWord appraisalKeyWord) {
        MatchingKeyWord appraisalMatching = new MatchingKeyWord();
        appraisalMatching.setFormDefinitionId(archive4ToBeAppraisal.getFormDefinitionId());
        appraisalMatching.setFormDataId(archive4ToBeAppraisal.getFormDataId());
        String content = archiveFileContentService.getFileContentsByRefId(archive4ToBeAppraisal.getFormDataId());
        String[] temp = appraisalKeyWord.getKeyWord().split(",");
        Map<String, String> tempMap = new HashMap();
        for (String key : temp) {
            String tempKey = key.replace(",", "，");
            String[] tempKeys = tempKey.split("，");
            // 对原文关键词进行过滤对比
            if (isMatching(content, tempKeys)) {
                tempMap.put("content", key);
            }
            if (isMatching(archive4ToBeAppraisal.getTitle(), tempKeys)) {
                tempMap.put("title", key);
            }
        }
        if (tempMap.size() == 0) {
            return false;
        }
        for (String key : tempMap.keySet()) {
            appraisalMatching.setId(UUIDUtils.getUUID());
            appraisalMatching.setKeywords(tempMap.get(key));
            appraisalMatching.setFiled(key);
            appraisalMatching.setAppraisalKeyWordId(appraisalKeyWord.getId());
            appraisalMatching.setAppraisalKeyWord(appraisalKeyWord.getKeyWord());
            appraisalMatching.setBasisId(appraisalKeyWord.getBasisId());
            appraisalMatching.setRulesId(appraisalKeyWord.getRulesId());
            commonMapper.insert(appraisalMatching);
        }
        return true;
    }

    public boolean isMatching(String content, String[] keys) {
        boolean flag = true;
        for (String key : keys) {
            if (!content.contains(key)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public boolean archiveDataContrastSpecial(Archive4ToBeAppraisal archive4ToBeAppraisal, AppraisalSpecial appraisalSpecial, FormData formData) {
        List<AppraisalSpecialDetail> appraisalSpecialDetails = (List<AppraisalSpecialDetail>) appraisalSpecial.getSpecialDetailList();
        for (AppraisalSpecialDetail appraisalSpecialDetail : appraisalSpecialDetails) {
            switch (appraisalSpecialDetail.getRelation()) {
                case "equal":
                    if (!appraisalSpecialDetail.getValue1().equals(formData.getString(appraisalSpecialDetail.getField()))) {
                        return false;
                    }
                    break;
                case "like":
                    if (!formData.getString(appraisalSpecialDetail.getField()).contains(appraisalSpecialDetail.getValue1())) {
                        return false;
                    }
                    break;
                case "startWith":
                    if (!formData.getString(appraisalSpecialDetail.getField()).startsWith(appraisalSpecialDetail.getValue1())) {
                        return false;
                    }
                    break;
                case "endWith":
                    if (!formData.getString(appraisalSpecialDetail.getField()).endsWith(appraisalSpecialDetail.getValue1())) {
                        return false;
                    }
                    break;
                case "notEqual":
                    if (appraisalSpecialDetail.getValue1().equals(formData.getString(appraisalSpecialDetail.getField()))) {
                        return false;
                    }
                    break;
                case "lessThan":
                    if (isNumeric2(appraisalSpecialDetail.getField())) {
                        if (Long.parseLong(formData.getString(appraisalSpecialDetail.getField())) > Long.parseLong(appraisalSpecialDetail.getValue1())) {
                            return false;
                        }
                    } else {
                        if (formData.getString(appraisalSpecialDetail.getField()).compareTo(appraisalSpecialDetail.getValue1()) > 0) {
                            return false;
                        }
                    }

                    break;
                case "greaterThan":
                    if (isNumeric2(appraisalSpecialDetail.getField())) {
                        if (Long.parseLong(formData.getString(appraisalSpecialDetail.getField())) < Long.parseLong(appraisalSpecialDetail.getValue1())) {
                            return false;
                        }
                    } else {
                        if (formData.getString(appraisalSpecialDetail.getField()).compareTo(appraisalSpecialDetail.getValue1()) < 0) {
                            return false;
                        }
                    }
                    break;
                case "between":
                    try {
                        if (isNumeric2(appraisalSpecialDetail.getField())) {
                            if (Long.parseLong(formData.getString(appraisalSpecialDetail.getField())) < Long.parseLong(appraisalSpecialDetail.getValue1())
                                    || Long.parseLong(formData.getString(appraisalSpecialDetail.getField())) > Long.parseLong(appraisalSpecialDetail.getValue2())) {
                                return false;
                            }
                        }
                    } finally {
                        break;
                    }
                default:
                    break;
            }
        }
        AppraisalMatching appraisalMatching = new AppraisalMatching();
        appraisalMatching.setAppraisalId(archive4ToBeAppraisal.getId());
        appraisalMatching.setMatchingType("special");
        appraisalMatching.setContent(appraisalSpecial.getSpecialName());
        appraisalMatching.setFormDataId(archive4ToBeAppraisal.getFormDataId());
        appraisalMatching.setFormDefinitionId(archive4ToBeAppraisal.getFormDefinitionId());
        appraisalMatching.setAppraisalSpecialId(appraisalSpecial.getId());
        appraisalMatching.setBasisID(appraisalSpecial.getBasisID());
        appraisalMatching.setRulesId(appraisalSpecial.getRulesId());
        commonMapper.insert(appraisalMatching);
        return true;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric2(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * 判断档案是否需要进行价值鉴定
     *
     * @param archive4ToBeAppraisal
     * @param formData
     * @return
     */
    public Archive4ToBeAppraisal isToBeAppraisalExpire(Archive4ToBeAppraisal archive4ToBeAppraisal, FormData formData) {
        if (!StringUtils.isEmpty(archive4ToBeAppraisal.getExpirationTime())) {
            if (year > Integer.parseInt(archive4ToBeAppraisal.getExpirationTime())) {
                archive4ToBeAppraisal.setAppraisalType(Constants.ROLE_DQJD);
                return archive4ToBeAppraisal;
            }
        }

        int Vintages;
        try {
            Vintages = Integer.parseInt(archive4ToBeAppraisal.getVintages());
        } catch (Exception e) {
            return archive4ToBeAppraisal;
        }
        if (StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_SAVE_TERM))) {
            return archive4ToBeAppraisal;
        }
        String string = formData.getString(ArchiveEntity.COLUMN_SAVE_TERM);
        if (formData.getString(ArchiveEntity.COLUMN_SAVE_TERM).equals("永久")) {
            return archive4ToBeAppraisal;
        }
        switch (string) {
            case "长期":
            case "30年":
            case "D30":
            case "30":
            case "三十":
            case "三十年":
            case "C":
                if (year - Vintages > 30) {
                    archive4ToBeAppraisal.setAppraisalType(Constants.ROLE_DQJD);
                }
                break;
            case "短期":
            case "10年":
            case "D10":
            case "10":
            case "十":
            case "Y":
            case "十年":
            case "D":
                if (year - Vintages > 10) {
                    archive4ToBeAppraisal.setAppraisalType(Constants.ROLE_DQJD);
                }
                break;
/*            default:
                // 检查保管期限是否为数字
                if (string.matches("-?\\d+")) {
                    int years = Integer.parseInt(string);
                    if (year - Vintages > years) {
                        archive4ToBeAppraisal.setAppraisalType(Constants.ROLE_DQJD);
                    }
                } else {
                    // 在此处处理非数字输入
                    throw new RuntimeException("无法匹配保管期限");
                }
                break;*/
/*            default:
                if (year - Vintages > Integer.parseInt(string)) {
                    archive4ToBeAppraisal.setAppraisalType(Constants.ROLE_DQJD);
                }
                break;*/
        }
        archive4ToBeAppraisal.setAuxiliaryResult("null");
        return archive4ToBeAppraisal;
    }

    /**
     * 将查询出来的档案表单数据转换成Archive4ToBeAppraisal对象数据
     *
     * @param formData
     * @return
     */
    public Archive4ToBeAppraisal translateData(FormData formData) {
        Archive4ToBeAppraisal archive4ToBeAppraisal = new Archive4ToBeAppraisal();
//        archive4ToBeAppraisal.setId(UUID.randomUUID().toString());
        archive4ToBeAppraisal.setStatus("0");
        archive4ToBeAppraisal.setArchiveCode(formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        archive4ToBeAppraisal.setCategoryCode(formData.getString(ArchiveEntity.COLUMN_CATEGORY_CODE));
        archive4ToBeAppraisal.setFondCode(formData.getString(ArchiveEntity.COLUMN_FOND_CODE));
        Fond fondByCode = fondService.findFondByCode(formData.getString(ArchiveEntity.COLUMN_FOND_CODE));
        archive4ToBeAppraisal.setFondId(fondByCode.getId());
        Category category = getCategory(archive4ToBeAppraisal.getCategoryCode(), fondByCode);
        archive4ToBeAppraisal.setCategoryId(ObjectUtils.isEmpty(category) ? "" : category.getId());
        archive4ToBeAppraisal.setCategoryName(ObjectUtils.isEmpty(category) ? "" : category.getName());
        archive4ToBeAppraisal.setTitle(formData.getString(ArchiveEntity.COLUMN_TITLE));
        archive4ToBeAppraisal.setFiletime(formData.getString(ArchiveEntity.COLUMN_FILETIME));
        archive4ToBeAppraisal.setOrganiseId(formData.getString(ArchiveEntity.COLUMN_ORGANISEID));
        archive4ToBeAppraisal.setFormDataId(formData.getId());
        archive4ToBeAppraisal.setFormDefinitionId(formData.getFormDefinitionId());
        archive4ToBeAppraisal.setVintages(getYearOfArchive(formData) + "");
        return archive4ToBeAppraisal;
    }
    private Category getCategory(String code, Fond fond) {
        SqlQuery<Category> group = SqlQuery.from(Category.class)
                .isNotNull(CategoryInfo.NAME)
                .equal(CategoryInfo.CODE, code)
                .equal(CategoryInfo.FONDID, fond.getId());
        List<Category> list = commonMapper.selectByQuery(group);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 在待鉴定库查询待鉴定档案信息
     *
     * @param formId
     * @param dataId
     * @return
     */
    public Archive4ToBeAppraisal selectOneByFormIdAndDataId(String formId, String dataId, String appraisalType) {
        SqlQuery sqlQuery = SqlQuery.from(Archive4ToBeAppraisal.class)
                .equal(Archive4ToBeAppraisalInfo.FORMDEFINITIONID, formId)
                .equal(Archive4ToBeAppraisalInfo.FORMDATAID, dataId)
                .equal(Archive4ToBeAppraisalInfo.APPRAISALTYPE, appraisalType);
        Archive4ToBeAppraisal archive4ToBeAppraisal = selectOne(sqlQuery);
        return archive4ToBeAppraisal;
    }

    public int getYearOfArchive(FormData formData) {
        String vintages = formData.getString(ArchiveEntity.COLUMN_YEAR);
        if (!StringUtils.isEmpty(vintages)) {
            if (StringUtils.isNumeric(vintages)) {
                return Integer.parseInt(vintages);
            }
        }
        String fileTime = formData.getString(ArchiveEntity.COLUMN_FILETIME);
        if (!StringUtils.isEmpty(fileTime)) {
            if (fileTime.length() >= 4) {
                fileTime = fileTime.substring(0, 4);
                if (StringUtils.isNumeric(fileTime)) {
                    return Integer.parseInt(fileTime);
                }
            }
        }
        return 9999;
    }

    @Override
    public Archive4ToBeAppraisal getOneByForm(String formDataId, String formDefinitionId) {
        SqlQuery sqlQuery = SqlQuery.from(Archive4ToBeAppraisal.class)
                .equal(Archive4ToBeAppraisalInfo.FORMDATAID, formDataId)
                .equal(Archive4ToBeAppraisalInfo.FORMDEFINITIONID, formDefinitionId);
        return (Archive4ToBeAppraisal) commonMapper.selectOneByQuery(sqlQuery);
    }

    @Override
    protected String getCacheName() {
        return "appraisal";
    }

    @Override
    public void removeCache(String orgId) {
        cache.evictIfPresent(orgId);
    }

    protected void refreshCache() {
        for (String key : doingScanMap.keySet()) {
            Cache.ValueWrapper existingValue = cache.get(key);
            if (existingValue == null) {
                cache.putIfAbsent(key, appraisalBasisService.getBasisByOrgId(key));
            }
        }
    }

    protected void startScanMap(SecurityHolder securityHolder) {
        AppraisalOrgRecord appraisalOrgRecord = new AppraisalOrgRecord();
        ScanArchiveHistory scanArchiveHistory = new ScanArchiveHistory();
        appraisalOrgRecord.setStartTime(System.currentTimeMillis() + "");
        for (String key : watiScanMap.keySet()) {
            appraisalOrgRecord.setOrgId(key);
            appraisalOrgRecord.setStatus("0");
            appraisalOrgRecord.setId(UUIDUtils.getUUID());
            commonMapper.insert(appraisalOrgRecord);
            scanArchiveHistory.setStartTime(System.currentTimeMillis() + "");
            scanArchiveHistory.setStatus("0");
            scanArchiveHistory.setOrgId(key);
            scanArchiveHistory.setId(UUIDUtils.getUUID());
            scanArchiveHistory.setCreatePerson(securityHolder.currentPerson().getId());
            scanArchiveHistory.setUpdatePerson(securityHolder.currentPerson().getId());
            scanArchiveHistory.setCreateDate(System.currentTimeMillis());
            scanArchiveHistory.setUpdateDate(System.currentTimeMillis());
            commonMapper.insert(scanArchiveHistory);
            doingScanMap.put(key, "");
        }
        refreshCache();
        watiScanMap.clear();
        refreshFormRecord();
    }

    /**
     * 更新表单扫描记录
     */
    protected void refreshFormRecord() {
        //TODO typeEqual("0") 查询表单类型为文件
        List<FormDefinition> formDefinitionList = (List<FormDefinition>) formDefinitionService.selectFormDefinitionByQuery(new FormDefinitionQuery().statusEnable().typeEqual("0"));
        AppraisalFormRecord appraisalFormRecord = new AppraisalFormRecord();
        for (FormDefinition formDefinition : formDefinitionList) {
            appraisalFormRecord.setFormId(formDefinition.getId());
            appraisalFormRecord.setStatus("0");
            appraisalFormRecord.setFinishCount("0");
            appraisalFormRecord.setId(UUIDUtils.getUUID());
            commonMapper.insert(appraisalFormRecord);
        }
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        SqlQuery sqlQuery = SqlQuery.from(AppraisalOrgRecord.class, false)
                .column(AppraisalOrgRecordInfo.ID)
                .equal(AppraisalOrgRecordInfo.STATUS, "0");
        List<AppraisalOrgRecord> appraisalOrgRecordList = commonMapper.selectByQuery(sqlQuery);
        if (appraisalOrgRecordList.size() > 0) {
            for (AppraisalOrgRecord appraisalOrgRecord : appraisalOrgRecordList) {
                // doingScanMap.put(appraisalOrgRecord.getOrgId(),true);
            }
        }
    }


    /**
     * 删除目前已经匹配的关键词以及专题信息
     *
     * @param archive4ToBeAppraisal
     */
    public void deleteMatching(Archive4ToBeAppraisal archive4ToBeAppraisal) {
        SqlQuery sqlQuery = SqlQuery.from(MatchingKeyWord.class)
                .equal(MatchingKeyWordInfo.FORMDATAID, archive4ToBeAppraisal.getFormDataId())
                .equal(MatchingKeyWordInfo.FORMDEFINITIONID, archive4ToBeAppraisal.getFormDefinitionId());
        commonMapper.deleteByQuery(sqlQuery);
        sqlQuery = SqlQuery.from(AppraisalMatching.class)
                .equal(AppraisalMatchingInfo.FORMDEFINITIONID, archive4ToBeAppraisal.getFormDefinitionId())
                .equal(AppraisalMatchingInfo.FORMDATAID, archive4ToBeAppraisal.getFormDataId());
        commonMapper.deleteByQuery(sqlQuery);
    }

    public void endDoingScan() {
        SqlQuery sqlQuery = SqlQuery.from(ScanArchiveHistory.class);
        for (String key : doingScanMap.keySet()) {
            commonMapper.updateByQuery(
                    sqlQuery.equal(ScanArchiveHistoryInfo.ORGID, key)
                            .set(ScanArchiveHistoryInfo.ENDTIME, System.currentTimeMillis())
                            .set(ScanArchiveHistoryInfo.STATUS, "1")
                            .equal(ScanArchiveHistoryInfo.STATUS, "0")
            );
        }
    }
}
