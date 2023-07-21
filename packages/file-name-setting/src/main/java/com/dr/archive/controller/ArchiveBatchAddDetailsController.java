package com.dr.archive.controller;

import com.dr.archive.entity.ArchivesBatchAddDetaile;
import com.dr.archive.entity.ArchivesBatchAddDetaileInfo;
import com.dr.archive.service.ArchivesBatchAddDetailsService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/ArchiveBatchAddDetails")
public class ArchiveBatchAddDetailsController extends BaseServiceController<ArchivesBatchAddDetailsService, ArchivesBatchAddDetaile> {

    @Override
    protected SqlQuery<ArchivesBatchAddDetaile> buildPageQuery(HttpServletRequest request, ArchivesBatchAddDetaile entity) {

        SqlQuery<ArchivesBatchAddDetaile> sqlQuery = SqlQuery.from(ArchivesBatchAddDetaile.class).orderBy(ArchivesBatchAddDetaileInfo.CREATEDATE);
        if (entity != null && StringUtils.hasText(entity.getBatchId())) {
            sqlQuery.equal(ArchivesBatchAddDetaileInfo.BATCHID, entity.getBatchId());
        }
        return sqlQuery;
    }
}
