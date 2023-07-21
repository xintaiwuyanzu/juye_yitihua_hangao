package com.dr.archive.managearchivechange.controller;

import com.dr.archive.managearchivechange.entity.ErrorCorrection;
import com.dr.archive.managearchivechange.entity.ErrorCorrectionInfo;
import com.dr.archive.managearchivechange.service.ErrorCorrectionService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-03-25 14:59
 * @Description:
 */
@RestController
@RequestMapping("/api/errorCorrection")
public class ErrorCorrectionController extends BaseServiceController<ErrorCorrectionService, ErrorCorrection> {
    @Override
    protected SqlQuery<ErrorCorrection> buildPageQuery(HttpServletRequest httpServletRequest, ErrorCorrection errorCorrection) {
//        TODO 机构id
        Organise organise = SecurityHolder.get().currentOrganise();
        return SqlQuery.from(ErrorCorrection.class)
                .like(ErrorCorrectionInfo.TITLE, errorCorrection.getTitle())
                .equal(ErrorCorrectionInfo.ORGID, organise.getId())
                .equal(ErrorCorrectionInfo.ERRORTYPE, errorCorrection.getErrorType())
                .equal(ErrorCorrectionInfo.FORMDATAID, errorCorrection.getFormDataId())
                .orderByDesc(ErrorCorrectionInfo.CREATEDATE).orderBy(ErrorCorrectionInfo.ERRORTYPE);
    }
}
