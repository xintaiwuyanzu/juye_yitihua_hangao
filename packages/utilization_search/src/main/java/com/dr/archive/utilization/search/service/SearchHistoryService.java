package com.dr.archive.utilization.search.service;

import com.dr.archive.utilization.search.entity.SearchHistory;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

import java.util.List;

/**
 * @author: lirs
 * @date: 2022/2/8 14:22
 */
public interface SearchHistoryService extends BaseService<SearchHistory> {
    /**
     * 添加查档历史数据
     *
     * @param querysListJson
     * @param isSecondRetrieval
     * @param categoryName
     * @param openScope
     * @param category
     * @param person
     */
    void addSearchHistory(String querysListJson, boolean isSecondRetrieval, String categoryName, boolean openScope, String category, Person person);

    /**
     * 根据登陆人查询所有查档历史
     * todo 有分页方法为啥要用这个？
     *
     * @param keyWord
     * @param person
     * @return
     */
    List<SearchHistory> getSearchHistory(String keyWord, Person person);
}
