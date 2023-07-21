package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.ArchiveAppraisalMessage;
import com.dr.archive.appraisal.entity.ArchiveAppraisalMessageInfo;
import com.dr.archive.appraisal.service.ArchiveAppraisalMessageService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴定记录
 */
@RestController
@RequestMapping("api/archiveAppraisalMessage")
public class ArchiveAppraisalMessageController extends BaseServiceController<ArchiveAppraisalMessageService, ArchiveAppraisalMessage> {

    @Override
    protected SqlQuery<ArchiveAppraisalMessage> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveAppraisalMessage archiveAppraisalMessage) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalMessage.class);
        sqlQuery.equal(ArchiveAppraisalMessageInfo.TASKID,archiveAppraisalMessage.getTaskId())
                .equal(ArchiveAppraisalMessageInfo.FORMDATAID,archiveAppraisalMessage.getFormDataId())
                .equal(ArchiveAppraisalMessageInfo.FORMDEFINITIONID,archiveAppraisalMessage.getFormDefinitionId())
                .orderBy(ArchiveAppraisalMessageInfo.CREATEDATE);
        return sqlQuery;
    }
}
