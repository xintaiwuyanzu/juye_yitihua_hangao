package com.dr.archive.statistics.controller;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.statistics.entity.ReportGenerate;
import com.dr.archive.statistics.entity.ReportGenerateInfo;
import com.dr.archive.statistics.service.ReportGenerateService;
import com.dr.archive.statistics.service.impl.ResourceStatisticsServiceImpl;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-07-22 14:59
 **/
@RestController
@RequestMapping("api/reportGenerate")
public class ReportGenerateController extends BaseServiceController<ReportGenerateService, ReportGenerate> {

    @Autowired
    ResourceStatisticsServiceImpl statisticsService;
    @Autowired
    CommonFileConfig fileConfig;
    @Autowired
    ReportGenerateService reportGenerateService;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Override
    protected SqlQuery<ReportGenerate> buildPageQuery(HttpServletRequest httpServletRequest, ReportGenerate reportGenerate) {
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Fond> fonds = fondOrganiseService.getFondListByOrganiseId(organise.getId());
        SqlQuery<ReportGenerate> sqlQuery = SqlQuery.from(ReportGenerate.class)
                .like(ReportGenerateInfo.FONDNAME, reportGenerate.getFondName())
                .like(ReportGenerateInfo.REPORTANNUAL, reportGenerate.getReportAnnual())
                .orderByDesc(ReportGenerateInfo.CREATEDATE);
        if (!"root".equals(organise.getId())) {
            sqlQuery.equal(ReportGenerateInfo.FONDCODE,fonds.get(0).getCode());
        }
            return sqlQuery;
    }

    @Override
    public ResultEntity page(HttpServletRequest request, ReportGenerate entity, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {

        SqlQuery<ReportGenerate> sqlQuery = this.buildPageQuery(request, entity);
        if (page){
            Page<ReportGenerate> reportGeneratePage = this.service.selectPage(sqlQuery, pageIndex, pageSize);
            return ResultEntity.success(reportGeneratePage);
        }else{
            return ResultEntity.success(this.service.selectList(sqlQuery));
        }
    }

    @Override
    public ResultEntity delete(HttpServletRequest request, ReportGenerate entity) {
        ReportGenerate generate = service.selectById(entity.getId());
        File file = new File(fileConfig.getRootDirFullPath() + generate.getReportPath());
        if (file.exists()) {
            file.delete();
        }
        return super.delete(request, entity);
    }

    @RequestMapping("/downloadReport")
    public void downloadReport(String id, HttpServletResponse response) {
        ReportGenerate generate = service.selectById(id);
        Long date = generate.getCreateDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String fileName = generate.getPersonName() + "_" + year + "年" + month + "月" + day + "日.xls";
        statisticsService.downloadDoc(new File(fileConfig.getRootDirFullPath() + generate.getReportPath()), fileName, response);
    }


    @RequestMapping("/total")
    public ResultEntity total() {
        return ResultEntity.success(service.total());
    }

    /**
     * 获得默认展示字段信息
     */
    @RequestMapping("/getFields")
    public ResultEntity getFields(String reportType) {
        return ResultEntity.success(reportGenerateService.getFields(reportType));
    }

}