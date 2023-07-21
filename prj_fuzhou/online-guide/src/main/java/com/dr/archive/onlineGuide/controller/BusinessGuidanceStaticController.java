package com.dr.archive.onlineGuide.controller;

import com.dr.archive.onlineGuide.service.BusinessGuidanceStaticService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-06-12 12:44
 * @Description:
 */
@RestController
@RequestMapping("api/businessGuidanceStatic")
public class BusinessGuidanceStaticController {
    @Autowired
    public BusinessGuidanceStaticService businessGuidanceStaticService;

    @RequestMapping("/statusStatic")
    public ResultEntity ywzdStatic(HttpServletRequest request, String startDate, String endDate,@Current String fondCode) {
        String s = "", e = "";
        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            s = DateTimeUtils.stringToMillis(startDate, "yyyy-MM-dd").toString();
            e = DateTimeUtils.stringToMillis(endDate, "yyyy-MM-dd").toString();
        }
        return ResultEntity.success(businessGuidanceStaticService.staticsByYear(s, e, fondCode));
    }

    /**
     * 指导统计
     * @return
     */
    @RequestMapping("/total")
    public ResultEntity total(){
        return ResultEntity.success(businessGuidanceStaticService.totals());
    }




}
