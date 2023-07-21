package com.dr.archive.statistics.service;

import com.dr.archive.statistics.entity.ReportGenerate;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author: yang
 * @create: 2022-07-22 15:37
 **/
public interface ReportGenerateService extends BaseService<ReportGenerate> {

    /**
     *  年报统计
     */
    List<ReportGenerate> total();

    List<Map<String, String>> getFields(String reportType);
}
