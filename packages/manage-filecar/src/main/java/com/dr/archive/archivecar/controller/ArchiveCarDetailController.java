package com.dr.archive.archivecar.controller;

import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.archivecar.service.ArchiveCarDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-01-18 19:41
 * @Description:
 */
@RestController
@RequestMapping({"${common.api-path:/api}/archiveCarDetail"})
public class ArchiveCarDetailController extends BaseServiceController<ArchiveCarDetailService, ArchiveCarDetail> {
    @Override
    protected SqlQuery<ArchiveCarDetail> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveCarDetail archiveCarDetail) {
        return SqlQuery.from(ArchiveCarDetail.class)
                .equal(ArchiveCarDetailInfo.BATCHID, archiveCarDetail.getBatchId())
                .like(ArchiveCarDetailInfo.ARCHIVETAG, archiveCarDetail.getArchiveTag())
                .like(ArchiveCarDetailInfo.TITLE, archiveCarDetail.getTitle())
                .like(ArchiveCarDetailInfo.ARCHIVECODE, archiveCarDetail.getArchiveCode());
    }
}
