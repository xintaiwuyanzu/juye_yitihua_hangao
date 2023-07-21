package com.dr.archive.manage.handover.controller;

import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLib;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLibInfo;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverLibService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 到期移交库controller
 *
 * @author dr
 */
@RestController
@RequestMapping({"${common.api-path:/api}/manage/handoverLib"})
public class ArchiveBatchHandOverLibController extends BaseServiceController<ArchiveBatchHandOverLibService, ArchiveBatchHandOverLib> {
    @Override
    protected SqlQuery<ArchiveBatchHandOverLib> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveBatchHandOverLib archiveBatchHandOverLib) {
        Organise organise = BaseController.getOrganise(httpServletRequest);
        return SqlQuery.from(ArchiveBatchHandOverLib.class)
                .like(ArchiveBatchHandOverLibInfo.BATCHNAME, archiveBatchHandOverLib.getBatchName())
                .like(ArchiveBatchHandOverLibInfo.STATUS, archiveBatchHandOverLib.getStatus())
                .equal(ArchiveBatchHandOverLibInfo.ORGANISEID, organise.getId())
                .orderBy(ArchiveBatchHandOverLibInfo.STATUS)
                .orderByDesc(ArchiveBatchHandOverLibInfo.CREATEDATE);
    }
}
