package com.dr.archive.fuzhou.controller;

import com.dr.archive.fuzhou.service.InformationOutService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author wx
 */
@RestController
@RequestMapping("/api/information")
public class InformationController {

    @Autowired
    InformationOutService informationOutService;

    @RequestMapping("archivesInformation")
    public ResultEntity informationByIdAndTitle(String categoryCode , String organiseCode, String startDate , String endDate){
        List<String> startDates = new ArrayList<>();
        List<String> endDates = new ArrayList<>();

        if(!"".equals(startDate) && endDate != null){
            startDates = Arrays.asList(startDate.split(","));
        }
        if(!"".equals(endDate) && endDate != null){
             endDates = Arrays.asList(endDate.split(","));
        }
        return ResultEntity.success(informationOutService.queryArchivesInformation(categoryCode, organiseCode, startDates, endDates));

    }
}
