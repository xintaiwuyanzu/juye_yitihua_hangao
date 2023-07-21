package com.dr.archive.service;

import com.dr.archive.entity.SmartSearchHistory;
import com.dr.framework.common.service.BaseService;

/**
 * @author: qiuyf
 * @date: 2022/7/1 17:45
 */
public interface SmartSearchHistoryService extends BaseService<SmartSearchHistory> {
    long saveHistory(SmartSearchHistory searchHistory);
}
