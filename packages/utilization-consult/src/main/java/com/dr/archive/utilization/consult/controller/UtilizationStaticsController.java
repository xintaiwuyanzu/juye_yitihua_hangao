package com.dr.archive.utilization.consult.controller;

import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.utilization.consult.service.UtilizationStaticsService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @author Mr.Zhu
 * @date 2022/4/26 - 14:36
 */
@RestController
@RequestMapping("${common.api-path:/api}/utilization/consult")
public class UtilizationStaticsController {
    @Autowired
    UtilizationStaticsService utilizationStaticsService;

    @RequestMapping("/staticsByYear")
    public ResultEntity staticsByYear(HttpServletRequest request, String startDate, String endDate) {
        String s = "";
        String e = "";
        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            s = DateTimeUtils.stringToMillis(startDate, "yyyy-MM-dd").toString();
            e = DateTimeUtils.stringToMillis(endDate, "yyyy-MM-dd").toString();
        }
        return ResultEntity.success(utilizationStaticsService.staticsByYear(s, e));
    }


}
