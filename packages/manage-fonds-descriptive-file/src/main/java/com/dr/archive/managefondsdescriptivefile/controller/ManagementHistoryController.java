package com.dr.archive.managefondsdescriptivefile.controller;

import com.dr.archive.managefondsdescriptivefile.entity.ManagementHistory;
import com.dr.archive.managefondsdescriptivefile.entity.ManagementHistoryInfo;
import com.dr.archive.managefondsdescriptivefile.service.ManagementHistoryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-02-23 20:51
 * @Description:
 */
@RestController
@RequestMapping("api/managementHistory")
public class ManagementHistoryController extends BaseServiceController<ManagementHistoryService, ManagementHistory> {
    @Override
    protected SqlQuery<ManagementHistory> buildPageQuery(HttpServletRequest httpServletRequest, ManagementHistory managementHistory) {
        return SqlQuery.from(ManagementHistory.class).equal(ManagementHistoryInfo.MANAGEMENTID, managementHistory.getManagementId()).equal(ManagementHistoryInfo.PROCESSINSTANCEID, managementHistory.getProcessInstanceId()).equal(ManagementHistoryInfo.TASKINSTANCEID, managementHistory.getTaskInstanceId()).orderByDesc(ManagementHistoryInfo.MANAGEMENTID).orderByDesc(ManagementHistoryInfo.CREATEDATE);
    }
}
