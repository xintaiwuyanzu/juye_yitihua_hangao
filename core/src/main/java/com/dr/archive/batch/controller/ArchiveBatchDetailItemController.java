package com.dr.archive.batch.controller;

import com.dr.archive.batch.entity.ArchiveBatchDetailItem;
import com.dr.archive.batch.service.ArchiveBatchDetailItemService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 档案待办任务
 *
 * @author dr
 */
@RestController
@RequestMapping({"${common.api-path:/api}/batchDetailItem"})
public class ArchiveBatchDetailItemController extends BaseServiceController<ArchiveBatchDetailItemService, ArchiveBatchDetailItem> {
    @Override
    protected SqlQuery<ArchiveBatchDetailItem> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveBatchDetailItem detailItem) {
        return SqlQuery.from(ArchiveBatchDetailItem.class);
    }
}
