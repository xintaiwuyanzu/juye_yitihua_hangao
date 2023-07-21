package com.dr.archive.common.pdfconversion.controller;

import com.dr.archive.common.pdfconversion.entity.FileConversionRecording;
import com.dr.archive.common.pdfconversion.entity.FileConversionRecordingInfo;
import com.dr.archive.common.pdfconversion.service.FileConversionRecordingService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${common.api-path:/api}/fileConversionRecording")
public class FileConversionRecordingController extends BaseServiceController<FileConversionRecordingService, FileConversionRecording> {
    @Override
    protected SqlQuery<FileConversionRecording> buildPageQuery(HttpServletRequest httpServletRequest, FileConversionRecording fileConversionRecording) {
        //人员权限
        String id = SecurityHolder.get().currentPerson().getId();
        SqlQuery<FileConversionRecording> from = SqlQuery.from(FileConversionRecording.class);
        from.equal(FileConversionRecordingInfo.CREATEPERSON,id).orderByDesc(FileConversionRecordingInfo.CREATEDATE);
        return from;
    }
}
