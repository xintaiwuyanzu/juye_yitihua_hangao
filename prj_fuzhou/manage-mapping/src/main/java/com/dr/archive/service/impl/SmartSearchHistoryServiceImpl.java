package com.dr.archive.service.impl;

import com.dr.archive.entity.SmartSearchHistory;
import com.dr.archive.entity.SmartSearchHistoryInfo;
import com.dr.archive.service.SmartSearchHistoryService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: qiuyf
 * @date: 2022/7/1 17:46
 */
@Service
public class SmartSearchHistoryServiceImpl extends DefaultBaseService<SmartSearchHistory> implements SmartSearchHistoryService {
    /*保存检索记录,若已有该记录则检索次数加一*/
    public long saveHistory(SmartSearchHistory searchHistory) {
        List<SmartSearchHistory> list = selectList(SqlQuery.from(SmartSearchHistory.class)
                .equal(SmartSearchHistoryInfo.CLASSTYPE, searchHistory.getClassType())
                .equal(SmartSearchHistoryInfo.CONTENT, searchHistory.getContent()).orderByDesc(SmartSearchHistoryInfo.CREATEDATE));
        if (list.size() > 0) {
            SmartSearchHistory searchHistory2 = list.get(0);
            int searchNum = searchHistory2.getSearchNum() + 1;
            searchHistory2.setSearchNum(searchNum);
            searchHistory2.setLastSearchDate(System.currentTimeMillis());
            updateById(searchHistory2);
        } else {
            searchHistory.setSearchNum(1);
            insert(searchHistory);
        }
        long result = 0;
        return result;
    }
}
