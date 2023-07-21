package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.service.AppraisalStatisticsService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 鉴定数量统计
 * @author:
 * @create: 2022-04-27 11:11
 **/
@RequestMapping("api/appraisalStatistics")
@RestController
public class AppraisalStatisticsController {
    @Autowired
    AppraisalStatisticsService appraisalStatisticsService;

    /**
     * 根据全宗、年度统计
     */
    @RequestMapping("/getStatistics")
    public ResultEntity getStatistics(String fondCode, String vintages, String appraisalType) {
        return ResultEntity.success(appraisalStatisticsService.countByFondAndVintagesAndType(null, fondCode, vintages, appraisalType));
    }

}
