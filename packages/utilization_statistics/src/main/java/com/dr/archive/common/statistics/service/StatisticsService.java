package com.dr.archive.common.statistics.service;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;

import java.util.List;
import java.util.Map;

/**
 * @author caiwb
 * @version 1.0.0
 * @ClassName StatisticsService.java
 * @Description TODO
 * @createTime 2021年08月18日
 */
public interface StatisticsService {
    Map count();

    List<Map> resource(String startTime, String endTime, String id,int flag);

    List<Map> report();

    List<Map> countUtilize(String startTime, String endTime, String orgId,int flag);

    List<Map> countAppraisal(String startTime, String endTime);

    List<Map> resourceByCateGory(String category, String year,int flag);

    Map filemanagement(String year);

    List<Map> workloadStatistics(String startTime,String endTime,String orgId,int flag);

    //获取全宗
    List<Fond> getFond();

    List<Category> getAllCategory();

}
