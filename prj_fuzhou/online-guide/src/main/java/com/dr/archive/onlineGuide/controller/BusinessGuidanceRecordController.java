package com.dr.archive.onlineGuide.controller;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceRecord;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceRecordInfo;
import com.dr.archive.onlineGuide.service.BusinessGuidanceRecordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-06-11 15:37
 * @Description:
 */
@RestController
@RequestMapping("api/businessGuidanceRecord")
public class BusinessGuidanceRecordController extends BaseServiceController<BusinessGuidanceRecordService, BusinessGuidanceRecord> {
    //TODO 过滤条件，添加时添加日期为空，其他绑定值了
    @Override
    protected SqlQuery<BusinessGuidanceRecord> buildPageQuery(HttpServletRequest httpServletRequest, BusinessGuidanceRecord businessGuidanceRecord) {
        return SqlQuery.from(BusinessGuidanceRecord.class)
                .equal(BusinessGuidanceRecordInfo.BUSINESSID, businessGuidanceRecord.getBusinessId())
                .orderByDesc(BusinessGuidanceRecordInfo.CREATEDATE);
    }
}
