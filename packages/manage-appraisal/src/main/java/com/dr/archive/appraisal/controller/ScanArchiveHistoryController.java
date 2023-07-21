package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.ScanArchiveHistory;
import com.dr.archive.appraisal.entity.ScanArchiveHistoryInfo;
import com.dr.archive.appraisal.service.ScanArchiveHistoryService;
import com.dr.archive.appraisal.vo.ScanArchiveHistoryVO;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 鉴定依据管理
 */
@RestController
@RequestMapping("api/scanArchiveHistory")
public class ScanArchiveHistoryController extends BaseController<ScanArchiveHistory> {

    @Autowired
    ScanArchiveHistoryService scanArchiveHistoryService;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<ScanArchiveHistory> sqlQuery, ScanArchiveHistory entity) {
        sqlQuery.equal(ScanArchiveHistoryInfo.ORGID, SecurityHolder.get().currentOrganise().getId())
                .orderByDesc(ScanArchiveHistoryInfo.CREATEDATE);
    }

    @RequestMapping("/queryScanArchiveHistory")
    public ResultEntity queryScanArchiveHistory(ScanArchiveHistory entity,
                                  @RequestParam(defaultValue = "0") int pageIndex,
                                  @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE_STR) int pageSize,
                                  @RequestParam(defaultValue = "true") boolean page){
        Page<ScanArchiveHistory> scanArchiveHistoryPage = commonService.selectPage(SqlQuery.from(ScanArchiveHistory.class)
                                            .equal(ScanArchiveHistoryInfo.ORGID, SecurityHolder.get().currentOrganise().getId())
                                            .orderByDesc(ScanArchiveHistoryInfo.CREATEDATE), pageIndex, pageSize);
        return ResultEntity.success(scanArchiveHistoryService.queryScanArchiveHistoryVo(scanArchiveHistoryPage));

    }

}
