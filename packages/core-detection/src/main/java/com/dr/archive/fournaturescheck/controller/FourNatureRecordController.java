package com.dr.archive.fournaturescheck.controller;

import com.dr.archive.fournaturescheck.entity.FourNatureRecord;
import com.dr.archive.fournaturescheck.entity.FourNatureRecordInfo;
import com.dr.archive.fournaturescheck.service.FourNatureRecordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/fourNatureRecord")
public class FourNatureRecordController extends BaseServiceController<FourNatureRecordService, FourNatureRecord> {
    @Override
    protected SqlQuery<FourNatureRecord> buildPageQuery(HttpServletRequest httpServletRequest, FourNatureRecord fourNatureRecord) {
        SqlQuery<FourNatureRecord> sqlQuery = SqlQuery.from(FourNatureRecord.class);
        sqlQuery.equal(FourNatureRecordInfo.BUSINESSID,fourNatureRecord.getBusinessId());
        return sqlQuery;
    }
}
