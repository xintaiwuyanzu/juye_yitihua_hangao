package com.dr.archive.detection.controller;

import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.entity.TestRecordInfo;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/5/19 10:28
 */
@RestController
@RequestMapping("${common.api-path:/api}/testRecord")
public class TestRecordController extends BaseServiceController<TestRecordService, TestRecord> {
    @Override
    protected SqlQuery<TestRecord> buildPageQuery(HttpServletRequest request, TestRecord entity) {
        SqlQuery<TestRecord> sqlQuery = SqlQuery.from(TestRecord.class)
                .equal(TestRecordInfo.FORMDATAID, entity.getFormDataId())
                .equal(TestRecordInfo.FORMDEFINITIONID, entity.getFormDefinitionId());
        sqlQuery.orderByDesc(TestRecordInfo.CREATEDATE);
        return sqlQuery;
    }
}
