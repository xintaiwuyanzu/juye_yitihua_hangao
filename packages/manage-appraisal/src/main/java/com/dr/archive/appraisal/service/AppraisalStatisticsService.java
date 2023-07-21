package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-04-27 21:55
 * @Description:
 */
public interface AppraisalStatisticsService {
    List<ArchiveAppraisalTask> countByFondAndVintagesAndType(String userId, String fondCode, String vintages, String appraisalType);
}
