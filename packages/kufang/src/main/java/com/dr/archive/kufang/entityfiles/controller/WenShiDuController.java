package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.WenShiDu;
import com.dr.archive.kufang.entityfiles.entity.WenShiDuInfo;
import com.dr.archive.kufang.entityfiles.service.WenShiDuService;
import com.dr.archive.kufang.entityfiles.utils.ExportExcelUtil;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"${common.api-path:/api}/wenShiDu"})
public class WenShiDuController extends BaseServiceController<WenShiDuService, WenShiDu> {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    protected SqlQuery<WenShiDu> buildPageQuery(HttpServletRequest httpServletRequest, WenShiDu wenShiDu) {
        String startDate = httpServletRequest.getParameter("startDate");
        String endDate = httpServletRequest.getParameter("endDate");
        SqlQuery<WenShiDu> sqlQuery = SqlQuery.from(WenShiDu.class);
        if (!StringUtils.isEmpty(wenShiDu.getKuFangMingCheng())) {
            sqlQuery.like(WenShiDuInfo.KUFANGMINGCHENG, wenShiDu.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(WenShiDuInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(WenShiDuInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        List<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person -> {
            persons.add(person.getId());
        });
        sqlQuery.in(WenShiDuInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(WenShiDuInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping("/exp")
    public void exp(HttpServletRequest request, HttpServletResponse response, WenShiDu wenShiDu) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        SqlQuery<WenShiDu> sqlQuery = SqlQuery.from(WenShiDu.class);
        if (!StringUtils.isEmpty(wenShiDu.getKuFangMingCheng())) {
            sqlQuery.like(WenShiDuInfo.KUFANGMINGCHENG, wenShiDu.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(WenShiDuInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(WenShiDuInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        List<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person -> {
            persons.add(person.getId());
        });
        sqlQuery.in(WenShiDuInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(WenShiDuInfo.CREATEDATE);
        List<WenShiDu> data = service.selectList(sqlQuery);
        String[] columnNames = {"年 月 日", "库房名称", "温度", "相对湿度", "记录人", "备注"};
        String[] columnCodes = {"createDate", "kuFangMingCheng", "wenDu", "shiDu", "personName", "mark"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "温湿度统计表", columnNames, columnCodes, data, "温湿度统计表");
    }
}
