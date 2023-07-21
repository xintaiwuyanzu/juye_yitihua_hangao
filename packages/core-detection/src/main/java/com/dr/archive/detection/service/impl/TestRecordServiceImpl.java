package com.dr.archive.detection.service.impl;

import com.dr.archive.detection.entity.TestConfig;
import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.service.*;
import com.dr.archive.fournaturescheck.entity.FourNatureScheme;
import com.dr.archive.fournaturescheck.entity.FourNatureSchemeInfo;
import com.dr.archive.fournaturescheck.entity.FourNatureSchemeItem;
import com.dr.archive.fournaturescheck.entity.FourNatureSchemeItemInfo;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 四性检测主要逻辑实现
 *
 * @author caor
 * @date 2020-11-15 11:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestRecordServiceImpl extends AbstractTestRecordService implements TestRecordService {
    @Autowired
    TestConfigService testConfigService;
    @Autowired
    TestRecordItemService testRecordItemService;
    @Autowired
    List<ItemDetectionService> detectionServices;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestRecord testData(FormData formData, FormDefinition formDefinition, Person person, Fond fond, Category category, LinkType linkType, Map<String, Object> attr) {
        //构造四性检测上下文
        ItemDetectContext context = buildContext(formData, formDefinition, person, fond, category, linkType, attr);
        //检测小项编码
        Set<String> configCodes = new HashSet<>();
        //查询所有检测配置
        TestConfig config = testConfigService.getConfig(formDefinition, category, linkType);
        if (config != null) {
            Collections.addAll(configCodes, config.getDetectCodes().split(","));
        }
        context.setConfigCodes(configCodes);
        //根据上下文构造四性检测记录
        TestRecord record = newRecord(context);
        context.setTestRecord(record);
        //执行四性检测
        doTest(context);
        //执行完所有的检测之后，保存检测记录到数据库
        testRecordItemService.insertRecordItems(record.getId(), context.getItemList());
        //计算检测数量
        record.setTotalCount(context.getItemSize());
        //检测成功数量
        record.setSuccessCount(context.getItemList().stream().filter(i -> i.getStatus().equals(TEST_STATUS_SUCCESS)).count());
        //检测结果状态
        record.setStatus(record.getTotalCount() == record.getSuccessCount() ? TEST_STATUS_SUCCESS : TEST_STATUS_FAIL);
        //保存检测结果
        insert(record);
        return record;
    }

    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public TestRecord testData(FormData formData, FormDefinition formDefinition, Person person, Fond fond, Category category, LinkType linkType, Map<String, Object> attr) {
        //构造四性检测上下文
        ItemDetectContext context = buildContext(formData, formDefinition, person, fond, category, linkType, attr);
        //检测小项编码
        Set<String> configCodes = new HashSet<>();
        //查询所有检测配置
        TestConfig config = testConfigService.getConfig(formDefinition, category, linkType);
        if (config != null) {
            Collections.addAll(configCodes, config.getDetectCodes().split(","));
        }
        context.setConfigCodes(configCodes);
        //根据上下文构造四性检测记录
        TestRecord record = newRecord(context);
        context.setTestRecord(record);
        //执行四性检测
        doTest(context);
        //执行完所有的检测之后，保存检测记录到数据库
        testRecordItemService.insertRecordItems(record.getId(), context.getItemList());
        //计算检测数量
        record.setTotalCount(context.getItemSize());
        //检测成功数量
        record.setSuccessCount(context.getItemList().stream().filter(i -> i.getStatus().equals(TEST_STATUS_SUCCESS)).count());
        //检测结果状态
        record.setStatus(record.getTotalCount() == record.getSuccessCount() ? TEST_STATUS_SUCCESS : TEST_STATUS_FAIL);
        //保存检测结果
        insert(record);
        return record;
    }*/

    /*private void doTest(ItemDetectContext context) {
        //循环所有检测实现，捉个调用检测配置
        for (ItemDetectionService detectionService : detectionServices) {
            ItemDetectionService.ConfigType configType = detectionService.configAble(context.getLinkType(), context.getCategory(), null);
            if (configType.equals(ItemDetectionService.ConfigType.DETECT_WITH_CONFIG_CODE)) {
                if (!context.getConfigCodes().contains(detectionService.code())) {
                    continue;
                }
            }
            try {
                int testCount = context.getItemSize();
                detectionService.detection(context);
                int newCount = context.getItemSize();
                *//*if (testCount == newCount) {
                    //如果执行检测完之后检测结果数量并没有变化
                    //尝试追加检测成功结果
                    if (detectionService.autoAppendRecordWhenSuccess()) {
                        //需要自动追加检测成功记录
                        context.addRecordItem(detectionService.code(), detectionService.modeCode(), detectionService.getAppendString());
                    }
                }*//*
            } catch (Exception e) {
                logger.error("执行" + detectionService.code() + "检测失败，", e);
                //记录检测失败记录
                context.addRecordItem(detectionService.code(), detectionService.modeCode(), "检测通过", TEST_STATUS_SUCCESS);
            }
        }
    }*/

    private void doTest(ItemDetectContext context) {
        //TODO 每个环节的检测方案应该智能配置一个，先默认处理多个
        for (FourNatureScheme fourNatureScheme : commonMapper.selectByQuery(SqlQuery.from(FourNatureScheme.class).equal(FourNatureSchemeInfo.CHECKLINK, context.getLinkType().name()))) {
            for (FourNatureSchemeItem fourNatureSchemeItem : commonMapper.selectByQuery(SqlQuery.from(FourNatureSchemeItem.class).equal(FourNatureSchemeItemInfo.FOURNATURESCHEMEID, fourNatureScheme.getId()))) {
                for (ItemDetectionService detectionService : detectionServices) {
                    if (detectionService.modeCode().equals(fourNatureSchemeItem.getCheckName())) {
                        try {
                            context.setFourNatureSchemeItem(fourNatureSchemeItem);
                            detectionService.detection(context);
                        } catch (Exception e) {
                            logger.error("执行" + detectionService.code() + "检测失败，", e);
                            //记录检测失败记录
                            context.addRecordItem(detectionService.code(), detectionService.modeCode(), "检测通过", TEST_STATUS_SUCCESS);
                        }
                    }
                }
            }
        }
    }


    @Override
    public synchronized Class<TestRecord> getEntityClass() {
        return TestRecord.class;
    }
}
