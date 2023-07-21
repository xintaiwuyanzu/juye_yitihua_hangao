package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;
import com.dr.archive.appraisal.entity.ArchiveAppraisalTaskInfo;
import com.dr.archive.appraisal.service.AppraisalStatisticsService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: caor
 * @Date: 2022-04-27 21:55
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppraisalStatisticsServiceImpl implements AppraisalStatisticsService {
    @Autowired
    ResourceManager resourceManager;
    @Autowired
    CommonMapper commonMapper;

    @Override
    public List<ArchiveAppraisalTask> countByFondAndVintagesAndType(String userId, String fondCode, String vintages, String appraisalType) {
        List<Fond> fondList = (List<Fond>)
                resourceManager.getPersonResources(StringUtils.hasText(userId) ? userId : SecurityHolder.get().currentPerson().getId(), "fond");

        List<String> fondCodeList = fondList.stream().map(Fond::getCode).collect(Collectors.toList());

        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class, false)
                .in(ArchiveAppraisalTaskInfo.FONDCODE, fondCodeList)
                .count(ArchiveAppraisalTaskInfo.ID, "id", false, false)
                .column(ArchiveAppraisalTaskInfo.VINTAGES, ArchiveAppraisalTaskInfo.APPRAISALTYPE)
                .equal(ArchiveAppraisalTaskInfo.APPRAISALTYPE, appraisalType)
                .equal(ArchiveAppraisalTaskInfo.VINTAGES, vintages)
                .equal(ArchiveAppraisalTaskInfo.FONDCODE, fondCode)
                .groupBy(ArchiveAppraisalTaskInfo.VINTAGES,ArchiveAppraisalTaskInfo.APPRAISALTYPE)
                .orderBy(ArchiveAppraisalTaskInfo.VINTAGES);
        List<ArchiveAppraisalTask> list = commonMapper.selectByQuery(sqlQuery);
        list.forEach(i -> i.setAppraisalType("KFJD".equalsIgnoreCase(i.getAppraisalType()) ? "开放鉴定数量" : "到期鉴定数量"));
        return list;
    }
}
