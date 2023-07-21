package com.dr.archive.utilization.search.service;

import com.dr.archive.utilization.search.to.DbSearchResultTo;
import com.dr.framework.common.page.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * @author caor
 * @date 2021-03-10 15:44
 */
public interface DbSearchService {
    /**
     * @param to
     * @param start 从第几条开始（数据库分页，起始为0）
     * @param size  数量
     * @return
     */
    Page<DbSearchResultTo> getDbSearchResult(HttpServletRequest request, DbSearchResultTo to, long start, long size);
}
