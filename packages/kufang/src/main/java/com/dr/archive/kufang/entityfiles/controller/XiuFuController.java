package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.XiuFu;
import com.dr.archive.kufang.entityfiles.entity.XiuFuInfo;
import com.dr.archive.kufang.entityfiles.service.XiuFuService;
import com.dr.archive.kufang.entityfiles.utils.ExportExcelUtil;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
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
@RequestMapping("${common.api-path:/api}/xiuFu")
public class XiuFuController extends BaseServiceController<XiuFuService, XiuFu> {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    protected SqlQuery<XiuFu> buildPageQuery(HttpServletRequest httpServletRequest, XiuFu xiuFu) {
        String startData = httpServletRequest.getParameter("startDate");
        String endData = httpServletRequest.getParameter("endDate");
        SqlQuery<XiuFu> sqlQuery = SqlQuery.from(XiuFu.class);
        if (!StringUtils.isEmpty(xiuFu.getKuFangMingCheng())) {
            sqlQuery.like(XiuFuInfo.KUFANGMINGCHENG, xiuFu.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startData) && !StringUtils.isEmpty(endData)) {
            sqlQuery.greaterThanEqual(XiuFuInfo.CREATEDATE, Long.parseLong(startData))
                    .lessThanEqual(XiuFuInfo.CREATEDATE, Long.parseLong(endData) + (24 * 3600 - 1) * 1000);
        }

        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(XiuFuInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(XiuFuInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<XiuFu> insert(HttpServletRequest request, XiuFu entity) {
        Person person = BaseController.getUserLogin(request);
        entity.setCreateDate(System.currentTimeMillis());
        entity.setCreatePerson(person.getId());
        entity.setPersonName(person.getUserName());
        return super.insert(request, entity);
    }

    @RequestMapping("/exp")
    public void exp(HttpServletRequest request, HttpServletResponse response, XiuFu xiuFu) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        SqlQuery<XiuFu> sqlQuery = SqlQuery.from(XiuFu.class);
        if (!StringUtils.isEmpty(xiuFu.getKuFangMingCheng())) {
            sqlQuery.like(XiuFuInfo.KUFANGMINGCHENG, xiuFu.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(XiuFuInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(XiuFuInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }

        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(XiuFuInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(XiuFuInfo.CREATEDATE);
        List<XiuFu> data = service.selectList(sqlQuery);
        String[] columnNames = {"年 月 日", "名称", "库房名称", "编号", "记录人", "维修原因", "维修记录", "送修人", "维修人", "备注"};
        String[] columnCodes = {"createDate", "fileName", "kuFangMingCheng", "fileNum", "personName", "maintenanceReasons", "maintenanceRecord", "sendPeople", "maintenancePeople", "mark"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "档案修复统计表", columnNames, columnCodes, data, "档案修复统计表");

    }
}
