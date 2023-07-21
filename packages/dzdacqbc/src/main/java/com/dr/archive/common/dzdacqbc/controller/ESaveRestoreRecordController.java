package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.ESaveRestoreRecord;
import com.dr.archive.common.dzdacqbc.entity.ESaveRestoreRecordInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveRestoreRecordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/restore")
public class ESaveRestoreRecordController extends BaseServiceController<ESaveRestoreRecordService, ESaveRestoreRecord> {


    @Override
    protected SqlQuery<ESaveRestoreRecord> buildPageQuery(HttpServletRequest request, ESaveRestoreRecord entity) {
        SqlQuery<ESaveRestoreRecord> sqlQuery = SqlQuery.from(ESaveRestoreRecord.class);
        if(StringUtils.hasText(entity.getArchiveCode())){
            sqlQuery.equal(ESaveRestoreRecordInfo.ARCHIVECODE, entity.getArchiveCode());
        }
        if(StringUtils.hasText(entity.getTiming())){
            sqlQuery.like(ESaveRestoreRecordInfo.TIMING, entity.getTiming());
        }
        sqlQuery.orderByDesc(ESaveRestoreRecordInfo.CREATEDATE);
        return sqlQuery;
    }
}
