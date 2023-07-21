package com.dr.archive.receive.online.archiveFiling.controller;

import com.dr.archive.receive.online.archiveFiling.entity.FilingDetail;
import com.dr.archive.receive.online.archiveFiling.entity.FilingDetailInfo;
import com.dr.archive.receive.online.archiveFiling.service.FilingService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 档案归档申请
 *
 * @author hyj
 */

@RestController
@RequestMapping("/api/filing")
public class FilingController extends BaseController<FilingDetail> {

    @Autowired
    FilingService filingService;


    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<FilingDetail> sqlQuery, FilingDetail entity) {
        sqlQuery.equal(FilingDetailInfo.FILINGID, entity.getFilingId());
    }

    /**
     * 校验指定条件的档案是否正在移交
     *
     * @param dataQuery
     * @param queryContent
     * @return
     */
    @RequestMapping("/checkFiling")
    public ResultEntity checkFiling(ArchiveDataQuery dataQuery, @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent) {
        dataQuery.parseQuery(queryContent);
        return new ResultEntity(filingService.checkFiling(dataQuery), "有档案正在调整申请中，不能重复申请", null);
    }

    /**
     * 查询该归档批次中档案详情
     *
     * @param id   任务id
     * @param type ”TASK“为代办界面；”RECORD“为记录界面
     * @return
     */
    @RequestMapping("queryArchiveDetail")
    public ResultEntity queryArchiveDetail(String id, String type) {
        List<FilingDetail> resultList = filingService.queryArchiveDetail(id, type);
        return ResultEntity.success(resultList);
    }

    /**
     * 归档审核方法
     *
     * @param type       1通过；0驳回
     * @param id         任务id
     * @param suggestion 审核意见
     * @return
     */
    @RequestMapping("examine")
    public ResultEntity examine(String type, String id, String suggestion) {
        filingService.examine(type, id, suggestion);
        return ResultEntity.success();
    }
}
