package com.dr.archive.utilization.search.controller;

import com.dr.archive.utilization.search.entity.SearchHistory;
import com.dr.archive.utilization.search.entity.SearchHistoryInfo;
import com.dr.archive.utilization.search.service.SearchHistoryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/2/8 11:49
 */
@RestController
@RequestMapping("api/searchHistory")
public class SearchHistoryController extends BaseServiceController<SearchHistoryService, SearchHistory> {
    @Override
    protected SqlQuery<SearchHistory> buildPageQuery(HttpServletRequest request, SearchHistory entity) {
        Person person = getUserLogin(request);
        SqlQuery<SearchHistory> sqlQuery = SqlQuery.from(SearchHistory.class)
                .equal(SearchHistoryInfo.CREATEPERSON, person.getId())
                .like(SearchHistoryInfo.KEYWORDS, entity.getKeyWords())
                .orderByDesc(SearchHistoryInfo.CREATEDATE);
        return sqlQuery;
    }

    /**
     * 查询搜索历史记录
     *
     * @param person
     * @return
     */
    @RequestMapping("/getSearchHistory")
    public ResultListEntity<SearchHistory> getSearchHistory(String keyWord, @Current Person person) {
        return ResultListEntity.success(service.getSearchHistory(keyWord, person));
    }

}
