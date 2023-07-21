package com.dr.archive.common.statistics.controller;

import com.dr.archive.common.statistics.service.StatisticsService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author caiwb
 * @version 1.0.0
 * @ClassName StatisticsController.java
 * @Description 档案统计控制层
 * @createTime 2021年08月18日
 */
@RestController
@RequestMapping("api/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @RequestMapping("/count")
    public ResultEntity count() {
        return ResultEntity.success(statisticsService.count());
    }

    @RequestMapping("/resource")
    public ResultEntity resource(String startTime, String endTime, String id, int flag) {

        return ResultEntity.success(statisticsService.resource(startTime, endTime, id, flag));
    }

    @RequestMapping("/report")
    public ResultEntity report() {
        List<Map> list = statisticsService.report();
        return ResultEntity.success(list);
    }

    @RequestMapping("countUtilize")
    public ResultEntity countUtilize(String startTime, String endTime, String orgId, int flag) {

        return ResultEntity.success(statisticsService.countUtilize(startTime, endTime, orgId, flag));
    }

    @RequestMapping("/countAppraisal")
    public ResultEntity countAppraisal(String startTime, String endTime) {

        return ResultEntity.success(statisticsService.countAppraisal(startTime, endTime));
    }

    @RequestMapping("/resourceByCateGory")
    public ResultEntity resourceByCateGory(String category, String year, int flag) {

        return ResultEntity.success(statisticsService.resourceByCateGory(category, year, flag));
    }

    @RequestMapping("/filemanagement")
    public ResultEntity filemanagement(String year) {

        return ResultEntity.success(statisticsService.filemanagement(year));
    }

    @RequestMapping("/workloadStatistics")
    public ResultEntity workloadStatistics(String startTime, String endTime, String orgId, int flag) {

        return ResultEntity.success(statisticsService.workloadStatistics(startTime, endTime, orgId, flag));
    }

    //获取全宗
    @RequestMapping("/getFond")
    public ResultEntity getFond() {
        return ResultEntity.success(statisticsService.getFond());
    }

    @RequestMapping("/getAllCategory")
    public ResultEntity getAllCategory() {
        return ResultEntity.success(statisticsService.getAllCategory());
    }

}
