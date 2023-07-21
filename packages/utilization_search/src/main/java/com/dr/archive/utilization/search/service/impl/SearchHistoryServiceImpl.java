package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.util.JsonUtils;
import com.dr.archive.utilization.search.entity.SearchHistory;
import com.dr.archive.utilization.search.entity.SearchHistoryInfo;
import com.dr.archive.utilization.search.service.SearchHistoryService;
import com.dr.archive.utilization.search.to.Querys;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: qiuyf
 * @date: 2022/2/8 14:22
 */
@Service
public class SearchHistoryServiceImpl extends DefaultBaseService<SearchHistory> implements SearchHistoryService {
    /**
     * 保存搜索历史
     *
     * @param querysListJson
     */
    @Override
    public void addSearchHistory(String querysListJson, boolean secondRetrieval, String categoryName, boolean openScope, String category, Person person) {
        long count = commonMapper.countByQuery(SqlQuery.from(SearchHistory.class)
                .equal(SearchHistoryInfo.QUERYSLISTJSON, querysListJson)
                .equal(SearchHistoryInfo.CREATEPERSON, person.getId()));
        if (count < 1) {
            SearchHistory sh = new SearchHistory();
            if (!secondRetrieval) {
                //非二次检索
                try {
                    List<Querys> querysList = JsonUtils.jsonToList(querysListJson, Querys.class);
                    Querys querys = querysList.get(0);
                    sh.setKeyWords(querys.getKeyWords());
                    sh.setMultiSearch(querys.isMultiSearch());
                    sh.setConditions(querys.getCondition());
                    sh.setOperato(querys.getOperato());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sh.setQuerysListJson(querysListJson);
            sh.setSecondRetrieval(secondRetrieval);
            sh.setCategoryName(categoryName);
            sh.setOpenScope(openScope);
            sh.setCategory(category);
            Organise organise = SecurityHolder.get().currentOrganise();
            sh.setOrgId(organise.getId());
            sh.setOrgName(organise.getOrganiseName());
            CommonService.bindCreateInfo(sh);
            commonMapper.insert(sh);
        }
    }

    @Override
    public List<SearchHistory> getSearchHistory(String keyWord, Person person) {
        SqlQuery<SearchHistory> query = SqlQuery.from(SearchHistory.class, false)
                .column(SearchHistoryInfo.KEYWORDS)
                .equal(SearchHistoryInfo.CREATEPERSON, person.getId())
                .equal(SearchHistoryInfo.SECONDRETRIEVAL, false)
                .equal(SearchHistoryInfo.MULTISEARCH, false)
                .groupBy(SearchHistoryInfo.KEYWORDS);
        if (StringUtils.hasText(keyWord)) {
            query.like(SearchHistoryInfo.KEYWORDS, keyWord);
        }
        return selectList(query);
    }
}
