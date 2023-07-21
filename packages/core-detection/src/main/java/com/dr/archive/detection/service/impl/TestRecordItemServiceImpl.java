package com.dr.archive.detection.service.impl;

import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.entity.TestRecordInfo;
import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.entity.TestRecordItems;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.model.TestSubType;
import com.dr.archive.detection.service.TestRecordItemService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.DefaultDataBaseService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author dr
 */
@Service
public class TestRecordItemServiceImpl implements TestRecordItemService, InitializingBean {
    static final Logger logger = LoggerFactory.getLogger(TestRecordItemService.class);

    @Autowired
    DefaultTestStdModelLoader defaultTestStdModelLoader;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ObjectMapper objectMapper;


    public List<TestRecordItem> testReport(TestRecordItem testRecordItem) {
        Assert.isTrue(StringUtils.hasText(testRecordItem.getRecordId()), "检测记录Id不能为空");
        TestRecordItems recordItems = commonMapper.selectById(TestRecordItems.class, testRecordItem.getRecordId());
        if (recordItems != null && StringUtils.hasText(recordItems.getContent())) {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, TestRecordItem.class);
            try {
                List<TestRecordItem> list = objectMapper.readValue(recordItems.getContent(), javaType);
                for (TestRecordItem recordItem : list) {
                    LinkType linkType = LinkType.ARCHIVING;
                    TestSubType testSubType = defaultTestStdModelLoader.getSubTypeByCode(recordItem.getModeCode(), linkType);
                    if (testSubType != null) {
                        recordItem.setTestRecordTargetType(testSubType.getDetectionType().getLabel());
                        recordItem.setTestRecordItems(testSubType.getTitle());
                        recordItem.setTestRecordSubstance(testSubType.getStdDescription());
                        recordItem.setTestRecordMethod(testSubType.getTestRecordMethod());
                    }
                }
                return list;
            } catch (JsonProcessingException e) {
                logger.error("解析四性检测结果失败：" + e.getMessage());
                return Collections.EMPTY_LIST;
            }
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void insertRecordItems(String recordId, List<TestRecordItem> recordItems) {
        recordItems.sort(TestRecordItem::compareTo);
        TestRecordItems testRecordItems = new TestRecordItems();
        testRecordItems.setId(recordId);
        try {
            //转换为json
            testRecordItems.setContent(objectMapper.writeValueAsString(
                    recordItems.stream()
                            .map(this::newMap)
                            .collect(Collectors.toList())
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        commonMapper.insert(testRecordItems);
    }

    private Map<String, Object> newMap(TestRecordItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put("createDate", item.getCreateDate());
        map.put("status", item.getStatus());
        map.put("targetCode", item.getTargetCode());
        map.put("targetName", item.getTargetName());
        map.put("targetValue", item.getTargetValue());
        map.put("itemCode", item.getItemCode());
        map.put("testResult", item.getTestResult());
        map.put("modeCode", item.getModeCode());
        return map;
    }

    //======================================================================================
    //==================下面所有的方法都只是为了迁移历史数据使用，将来要删除掉====================
    //======================================================================================
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Value("${test.merge:false}")
    boolean mergeItems = false;
    @Autowired
    DefaultDataBaseService defaultDataBaseService;


    /**
     * 临时迁移数据使用
     *
     * @throws InterruptedException
     * @deprecated
     */
    @Override
    public void afterPropertiesSet() throws InterruptedException {
        if (mergeItems) {
            Relation relation = defaultDataBaseService.getTableInfo("ARCHIVE_TEST_RECORD_ITEM", "archive");
            if (relation == null) {
                logger.warn("四性检测详情表不存在，数据已经迁移");
                return;
            }
            long total = commonMapper.count(TestRecord.class);
            logger.warn("总计检测项:" + total);
            int computedTotal = 0;
            while (computedTotal < total) {
                List<TestRecord> records = commonMapper.selectLimitByQuery(
                        SqlQuery.from(TestRecord.class, false)
                                .column(TestRecordInfo.ID)
                                .orderBy(TestRecordInfo.CREATEDATE)
                        , computedTotal, computedTotal + 1000);
                if (records.isEmpty()) {
                    break;
                }
                Set<String> recordIds = new HashSet<>(records.size());
                CountDownLatch downLatch = new CountDownLatch(records.size());
                for (TestRecord record : records) {
                    recordIds.add(record.getId());
                    executorService.execute(() -> {
                        doChange(record, relation);
                        downLatch.countDown();
                    });
                }
                downLatch.await();
                commonMapper.deleteByQuery(SqlQuery.from(relation).in(relation.getColumn("recordId"), new ArrayList<>(recordIds)));
                computedTotal += records.size();
                logger.warn("计算进度：" + computedTotal + "/" + total);
            }
        }
    }

    private void doChange(TestRecord record, Relation relation) {
        Column recordId = relation.getColumn("recordId");
        SqlQuery<TestRecordItem> sqlQuery = SqlQuery.from(relation)
                .equal(recordId, record.getId())
                .orderBy(relation.getColumn("modeCode"))
                .orderBy(relation.getColumn("createDate"))
                .setReturnClass(TestRecordItem.class);
        List<TestRecordItem> list = commonMapper.selectByQuery(sqlQuery);
        if (!list.isEmpty()) {
            List<Map<String, Object>> saveList = list.stream().map(this::newMap).collect(Collectors.toList());
            TestRecordItems testRecordItems = new TestRecordItems();
            testRecordItems.setId(record.getId());
            try {
                testRecordItems.setContent(objectMapper.writeValueAsString(saveList));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            commonMapper.insert(testRecordItems);
        }
    }
}
