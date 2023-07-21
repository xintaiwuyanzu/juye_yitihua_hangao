package com.dr.archive.statistics.service.impl;

import com.dr.archive.statistics.entity.StatisticalReport;
import com.dr.archive.statistics.service.StatisticalReportService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mr.Zhu
 * @date 2022/4/18 - 10:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticalReportServiceImpl extends DefaultBaseService<StatisticalReport> implements StatisticalReportService {
}
