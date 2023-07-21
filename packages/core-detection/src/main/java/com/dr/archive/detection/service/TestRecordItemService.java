package com.dr.archive.detection.service;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.framework.common.service.BaseService;

import java.util.List;

/**
 * 检测小项接口
 *
 * @author dr
 */
public interface TestRecordItemService {
    List<TestRecordItem> testReport(TestRecordItem testRecordItem);

    void insertRecordItems(String recordId, List<TestRecordItem> recordItems);
}
