package com.dr.archive.statistics.service;

import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.statistics.entity.ResourceStatistics;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mr.Zhu
 * @date 2022/4/18 - 10:30
 */
public interface ResourceStatisticsService extends BaseService<ResourceStatistics> {

    //根据当前登录人权限   按照门类查看所属全宗的信息
    List<ResourceStatistics> getResourceStatisticsListByPeronRole(String fondCode, String categoryCode, String year);

    List<ResourceStatistics> getResouceStatisticsCount(ResourceStatistics resourceStatistics, String startYear, String endYear);

    boolean count();

    /**
     * 查询表单
     *
     * @return
     */
    List<FormDefinition> getForms(String type);

    void downloadExcel(HttpServletResponse response);

    boolean generateReport(List<ResourceStatistics> list, ResourceStatistics resourceStatistics, HttpServletRequest request);

    boolean workloadReport(HttpServletRequest request, int type, String fondCode, String vintages);

    /**
     * 根据全宗统计分类信息
     *
     * @param query
     * @return
     */
    List<ResourceStatistics> statisticByFond(ResourceStatistics query);

    /**
     * 根据
     *
     * @param query
     * @return
     */
    List<ResourceStatistics> statisticByCategory(ResourceStatistics query);
    List<ResourceStatistics> statisticByCategoryAndYear(long s,long e);

    /**
     * 台账情况
     *
     * @param templateId
     * @param management
     * @return
     */
    String getStatisticByCategory(String templateId, Management management);

    /**
     * 根据年度分类统计
     *
     * @param query
     * @return
     */
    List<ResourceStatistics> statisticByYear(ResourceStatistics query);

}
