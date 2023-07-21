package com.dr.archive.statistics.controller;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.statistics.entity.ResourceStatistics;
import com.dr.archive.statistics.entity.ResourceStatisticsInfo;
import com.dr.archive.statistics.service.ResourceStatisticsService;
import com.dr.archive.statistics.service.StatisticsBusinessService;
import com.dr.archive.statistics.service.StatisticsSaveTermService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author Mr.Zhu
 * @date 2022/4/18 - 10:31
 */
@RestController
@RequestMapping("api/resourceStatistics")
public class ResourceStatisticsController extends BaseServiceController<ResourceStatisticsService, ResourceStatistics> {
    @Autowired
    ResourceManager resourceManager;
    @Autowired
    StatisticsSaveTermService statisticsSaveTermService;
    @Autowired
    StatisticsBusinessService statisticsBusinessService;
    @Autowired
    CommonMapper commonMapper;

    @Override
    protected SqlQuery<ResourceStatistics> buildPageQuery(HttpServletRequest httpServletRequest, ResourceStatistics resourceStatistics) {
        Person person = getUserLogin(httpServletRequest);
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(person.getId(), "fond");
        List<String> fondCodeList = fondList.stream().map(Fond::getCode).collect(Collectors.toList());
        SqlQuery<ResourceStatistics> sqlQuery = SqlQuery.from(ResourceStatistics.class,false)
                .column(ResourceStatisticsInfo.CATEGORYNAME,
                        ResourceStatisticsInfo.ARCARCHIVENUM.sum(), ResourceStatisticsInfo.FILEARCHIVENUM.sum(),
                        ResourceStatisticsInfo.ARCFILENUM.sum(), ResourceStatisticsInfo.FILEFILENUM.sum(),
                        ResourceStatisticsInfo.FILEFILESIZE.sum(), ResourceStatisticsInfo.ARCFILESIZE.sum())
                .in(ResourceStatisticsInfo.FONDCODE, fondCodeList)
                .equal(ResourceStatisticsInfo.FONDCODE, resourceStatistics.getFondCode())
                .equal(ResourceStatisticsInfo.CATEGORYCODE, resourceStatistics.getCategoryCode())
                .like(ResourceStatisticsInfo.CATEGORYNAME, resourceStatistics.getCategoryName())
                .equal(ResourceStatisticsInfo.VINTAGES, resourceStatistics.getVintages())
                .groupBy(ResourceStatisticsInfo.CATEGORYNAME);/*.orderByDesc(ResourceStatisticsInfo.FILEARCHIVENUM)
                .orderBy(ResourceStatisticsInfo.VINTAGES)*/
        Organise organise = SecurityHolder.get().currentOrganise();
        if (organise.getOrganiseType().equals("dag")){
            sqlQuery.equal(ResourceStatisticsInfo.ORGID,organise.getId());
        }
        return sqlQuery;
    }
    @RequestMapping({"/total"})
    public ResultEntity total() {
        Person person = SecurityHolder.get().currentPerson();
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(person.getId(), "fond");
        List<String> fondCodeList = fondList.stream().map(Fond::getCode).collect(Collectors.toList());
        List<ResourceStatistics> resourceStatistics = commonMapper.selectByQuery(SqlQuery.from(ResourceStatistics.class, false)
                .in(ResourceStatisticsInfo.FONDCODE, fondCodeList)
                .column(ResourceStatisticsInfo.CATEGORYNAME)
                .groupBy(ResourceStatisticsInfo.CATEGORYNAME)
        );
        List<ResourceStatistics> list = commonMapper.selectByQuery(SqlQuery.from(ResourceStatistics.class)
                .in(ResourceStatisticsInfo.FONDCODE, fondCodeList).equal(ResourceStatisticsInfo.ORGID,SecurityHolder.get().currentOrganise().getId())
        );
        for (ResourceStatistics resourceStatistic : resourceStatistics) {
            if (resourceStatistic.getCategoryName().startsWith("【")){
                resourceStatistic.setCategoryName(resourceStatistic.getCategoryName().substring(1,resourceStatistic.getCategoryName().indexOf("】")));
            }
            for (ResourceStatistics statistics : list) {
                if (statistics.getCategoryName().contains(resourceStatistic.getCategoryName())) {
                    resourceStatistic.setFileArchiveNum(resourceStatistic.getFileArchiveNum() + statistics.getFileArchiveNum());
                    resourceStatistic.setArcArchiveNum(resourceStatistic.getArcArchiveNum() + statistics.getArcArchiveNum());
                }
            }
        }
        List<ResourceStatistics> unique = resourceStatistics.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<ResourceStatistics>(comparing(ResourceStatistics::getCategoryName))), ArrayList::new)
        );
        Fond fond = fondList.get(0);
        for (Category category : commonMapper.selectByQuery(SqlQuery.from(Category.class).equal(CategoryInfo.PARENTID, fond.getId()))) {
            int count=0;
            for (ResourceStatistics resourceStatistic : unique) {
                if (category.getName().equals(resourceStatistic.getCategoryName())){
                    count++;
                }
            }
            if (count==0){
                ResourceStatistics statistics = new ResourceStatistics();
                statistics.setCategoryName(category.getName());
                unique.add(statistics);
            }
        }

        return ResultEntity.success(unique);
    }




    protected SqlQuery<ResourceStatistics> buildPageQuery1(HttpServletRequest httpServletRequest, ResourceStatistics resourceStatistics) {
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");
        List<String> fondCodeList = fondList.stream().map(Fond::getCode).collect(Collectors.toList());
        return SqlQuery.from(ResourceStatistics.class, false)
                .column(ResourceStatisticsInfo.CATEGORYNAME, ResourceStatisticsInfo.CATEGORYCODE, ResourceStatisticsInfo.CREATEDATE, ResourceStatisticsInfo.UPDATEDATE,
                        ResourceStatisticsInfo.ARCARCHIVENUM.sum(), ResourceStatisticsInfo.FILEARCHIVENUM.sum(),
                        ResourceStatisticsInfo.ARCFILENUM.sum(), ResourceStatisticsInfo.FILEFILENUM.sum(),
                        ResourceStatisticsInfo.FILEFILESIZE.sum(), ResourceStatisticsInfo.ARCFILESIZE.sum())
                .in(ResourceStatisticsInfo.FONDCODE, fondCodeList)
                .equal(ResourceStatisticsInfo.FONDCODE, resourceStatistics.getFondCode())
                .equal(ResourceStatisticsInfo.CATEGORYCODE, resourceStatistics.getCategoryCode())
                .equal(ResourceStatisticsInfo.VINTAGES, resourceStatistics.getVintages())
                .groupBy(ResourceStatisticsInfo.CATEGORYNAME)
                .orderBy(ResourceStatisticsInfo.FONDCODE, ResourceStatisticsInfo.CATEGORYCODE, ResourceStatisticsInfo.VINTAGES)
                .orderByDesc(ResourceStatisticsInfo.FILEARCHIVENUM);
    }

    @RequestMapping({"/count"})
    public ResultEntity count() {
        service.count();
        return ResultEntity.success();
    }

    @RequestMapping({"/getResourceStatisticsListByPeronRole"})
    public ResultEntity getResourceStatisticsListByPeronRole(HttpServletRequest request) {
        return ResultEntity.success(service.getResourceStatisticsListByPeronRole(request.getParameter("fondCode"), request.getParameter("categoryCode"), request.getParameter("year")));
    }

    /**
     * 按照保管期限统计档案数量，todo 权限暂时没处理
     *
     * @return
     */
    @RequestMapping({"/countBySaveTerm"})
    public ResultEntity countBySaveTerm() {
        return ResultEntity.success(statisticsSaveTermService.countBySaveTerm());
    }

    @RequestMapping({"/updateCountBySaveTerm"})
    public ResultEntity updateCountBySaveTerm() {
        return ResultEntity.success(statisticsSaveTermService.updateCountBySaveTerm());
    }

    @RequestMapping({"/updateCountByBusiness"})
    public ResultEntity updateCountByBusiness() {
        statisticsBusinessService.updateCountByBusiness();
        return ResultEntity.success();
    }

    @RequestMapping({"/downloadExcel"})
    public void downloadExcel(HttpServletResponse response) {
        service.downloadExcel(response);
    }

    /**
     * 资源统计生成报表
     *
     * @return
     */
    @RequestMapping("/generateReport")
    public ResultEntity generateReport(HttpServletRequest request, ResourceStatistics resourceStatistics) {
        return ResultEntity.success(service.generateReport(service.selectList(buildPageQuery(request, resourceStatistics)), resourceStatistics, request));
    }

    /**
     * 工作量统计生成报表
     *
     * @return
     */
    @RequestMapping("/workloadReport")
    public ResultEntity workloadReport(HttpServletRequest request, int type, String fondCode, String vintages) {
        return ResultEntity.success(service.workloadReport(request, type, fondCode, vintages));
    }

    @RequestMapping("/year")
    public ResultEntity year() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return ResultEntity.success(year);
    }

    /**
     * 根据全宗分组统计
     *
     * @return
     */
    @RequestMapping("/statisticByFond")
    public ResultEntity statisticByFond(ResourceStatistics statistics) {
        return ResultEntity.success(service.statisticByFond(statistics));
    }


    /**
     * 根据门类分组统计
     *
     * @return
     */
    @RequestMapping("/statisticByCategory")
    public ResultEntity statisticByCategory(ResourceStatistics statistics) {
        return ResultEntity.success(service.statisticByCategory(statistics));
    }

    /**
     * 根据年度分类统计
     *
     * @param statistics
     * @return
     */
    @RequestMapping("/statisticByYear")
    public ResultEntity statisticByYear(ResourceStatistics statistics) {
        return ResultEntity.success(service.statisticByYear(statistics));
    }


    /**
     * 全宗卷 台账模板
     *
     * @param management
     * @return
     */
    @RequestMapping("/getStatisticByCategory")
    public ResultEntity getStatisticByCategory(HttpServletRequest request, Management management) {
        return ResultEntity.success(service.getStatisticByCategory(request.getParameter("templateId"), management));
    }

}
