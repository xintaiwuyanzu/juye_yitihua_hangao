package com.dr.archive.utilization.search.controller;

import com.dr.archive.utilization.search.service.DbSearchService;
import com.dr.archive.utilization.search.to.DbSearchResultTo;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author caor
 * @date 2021-03-10 15:23
 */
@RestController
@RequestMapping("api/dbsearch")
public class DbSearchController {
    @Autowired
    DbSearchService dbSearchService;

    @RequestMapping("/searchByKeyword")
    public ResultEntity searchArchiveByQuery(HttpServletRequest request,
                                             String keyword,
                                             String key,
                                             String indexQuery,
                                             @RequestParam(defaultValue = "1") Integer index,
                                             @RequestParam(defaultValue = "100") Integer size, DbSearchResultTo to) {
        return ResultEntity.success(dbSearchService.getDbSearchResult(request, to, index, size));
    }
}
