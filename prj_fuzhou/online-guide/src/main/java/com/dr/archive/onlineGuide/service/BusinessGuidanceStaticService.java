package com.dr.archive.onlineGuide.service;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;

import java.util.List;
import java.util.Map;

/**
 * @Author: caor
 * @Date: 2022-06-12 12:34
 * @Description:
 */
public interface BusinessGuidanceStaticService {
    List<Map<String, Object>> staticsByYear(String startDate, String endDate, String fondCode);

    /**
     * 指导统计
     *
     * @return
     */
    List<BusinessGuidanceBatch> totals();

}
