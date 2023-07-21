package com.dr.archive.controller;

import com.dr.archive.entity.SmartSearchHistory;
import com.dr.archive.entity.SmartSearchHistoryInfo;
import com.dr.archive.service.SmartSearchHistoryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/7/1 17:56
 */
@RestController
@RequestMapping("api/smartSearchHistory")
public class SmartSearchHistoryController extends BaseServiceController<SmartSearchHistoryService, SmartSearchHistory> {
    @Autowired
    protected CommonMapper commonMapper;

    @Override
    protected SqlQuery<SmartSearchHistory> buildPageQuery(HttpServletRequest httpServletRequest, SmartSearchHistory smartSearchHistory) {
        return SqlQuery.from(SmartSearchHistory.class);
    }

    @RequestMapping("/hopTop6")
    public ResultEntity hotTop6() {
        SqlQuery<SmartSearchHistory> sqlQuery = SqlQuery.from(SmartSearchHistory.class)
                .orderByDesc(SmartSearchHistoryInfo.SEARCHNUM);
        return ResultEntity.success(commonMapper.selectLimitByQuery(sqlQuery, 0, 6));
    }
}
