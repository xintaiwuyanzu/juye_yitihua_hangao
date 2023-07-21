package com.dr.archive.common.statistics.controller;

import com.dr.archive.common.statistics.entity.ReportForm;
import com.dr.archive.common.statistics.service.ReportFormService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reportform")
public class ReportFormController {

    @Autowired
    ReportFormService reportFormService;


    @RequestMapping("/getReportForm")
    public ResultEntity getReportForm(ReportForm reportForm,
                                      @RequestParam(value = "index",defaultValue = "0")Integer index,
                                      @RequestParam(value = "size",defaultValue = "15")Integer size){

        return ResultEntity.success(reportFormService.getReportForm(reportForm,index,size));
    }
}
