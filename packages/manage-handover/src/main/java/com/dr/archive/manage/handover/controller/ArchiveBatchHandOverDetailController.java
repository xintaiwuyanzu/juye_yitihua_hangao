package com.dr.archive.manage.handover.controller;

import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverDetail;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverDetailInfo;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverDetailService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 到期移交详情controller
 *
 * @author dr
 */
@RestController
@RequestMapping({"${common.api-path:/api}/manage/handover/details"})
public class ArchiveBatchHandOverDetailController extends BaseServiceController<ArchiveBatchHandOverDetailService, ArchiveBatchHandOverDetail> {
    @Autowired
    ArchiveBatchHandOverService handOverService;

    @Override
    protected SqlQuery<ArchiveBatchHandOverDetail> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveBatchHandOverDetail archiveBatchHandOverDetail) {
        SqlQuery<ArchiveBatchHandOverDetail> sqlQuery = SqlQuery.from(ArchiveBatchHandOverDetail.class);
        //批次Id
        String handoverId = httpServletRequest.getParameter("handoverId");
        if (StringUtils.hasText(handoverId)) {
            ArchiveBatchHandOver handOver = handOverService.selectById(handoverId);
            String[] libIds = handOver.getLibIds().split(",");
            sqlQuery.in(ArchiveBatchHandOverDetailInfo.BATCHID, libIds);
        }
        sqlQuery.equal(ArchiveBatchHandOverDetailInfo.BATCHID, archiveBatchHandOverDetail.getBatchId())
                .equal(ArchiveBatchHandOverDetailInfo.DETAILTYPE, archiveBatchHandOverDetail.getDetailType())
                .like(ArchiveBatchHandOverDetailInfo.TITLE, archiveBatchHandOverDetail.getTitle());
        return sqlQuery.orderBy(ArchiveBatchHandOverDetailInfo.ARCHIVECODE);
    }


    /**
     * 获得移交库数据详情
     */
    @RequestMapping("/detailData/page")
    public ResultEntity getOverDetailData(String batchId, String handoverId, String archiveCode, @RequestParam(defaultValue = "0") int pageIndex,
                                          @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE_STR) int pageSize){
        return ResultEntity.success(service.getOverDetailData(batchId, handoverId, archiveCode, pageIndex, pageSize));
    }
}
