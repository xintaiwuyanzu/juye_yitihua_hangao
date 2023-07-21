package com.dr.archive.utilization.consult.service;

import java.util.List;
import java.util.Map;

/**
 * @author Mr.Zhu
 * @date 2022/4/26 - 14:37
 */
public interface UtilizationStaticsService {

    List<Map<String,Object>> staticsByYear(String startDate,String endDate);
}
