package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.AnQuan;
import com.dr.archive.kufang.entityfiles.entity.AnQuanInfo;
import com.dr.archive.kufang.entityfiles.service.AnQuanService;
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
@RequestMapping("${common.api-path:/api}/anQuan")
public class AnQuanController extends BaseServiceController<AnQuanService, AnQuan> {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    protected SqlQuery<AnQuan> buildPageQuery(HttpServletRequest httpServletRequest, AnQuan anQuan) {
        String startData = httpServletRequest.getParameter("startDate");
        String endData = httpServletRequest.getParameter("endDate");
        SqlQuery<AnQuan> sqlQuery = SqlQuery.from(AnQuan.class);
        if (!StringUtils.isEmpty(anQuan.getKuFangMingCheng())) {
            sqlQuery.like(AnQuanInfo.KUFANGMINGCHENG, anQuan.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startData) && !StringUtils.isEmpty(endData)) {
            sqlQuery.greaterThanEqual(AnQuanInfo.CREATEDATE, Long.parseLong(startData))
                    .lessThanEqual(AnQuanInfo.CREATEDATE, Long.parseLong(endData) + (24 * 3600 - 1) * 1000);
        }

        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(AnQuanInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(AnQuanInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<AnQuan> insert(HttpServletRequest request, AnQuan entity) {
        Person person = BaseController.getUserLogin(request);
        entity.setCreateDate(System.currentTimeMillis());
        entity.setCreatePerson(person.getId());
        entity.setPersonName(person.getUserName());
        return super.insert(request, entity);
    }

    @RequestMapping("/exp")
    public void exp(HttpServletRequest request, HttpServletResponse response, AnQuan anQuan) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        SqlQuery<AnQuan> sqlQuery = SqlQuery.from(AnQuan.class);
        if (!StringUtils.isEmpty(anQuan.getKuFangMingCheng())) {
            sqlQuery.like(AnQuanInfo.KUFANGMINGCHENG, anQuan.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(AnQuanInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(AnQuanInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(AnQuanInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(AnQuanInfo.CREATEDATE);
        List<AnQuan> data = service.selectList(sqlQuery);
        String[] columnNames = {"年 月 日", "防火情况", "防盗情况", "记录人", "采取措施"};
        String[] columnCodes = {"createDate", "fangHuo", "fangDao", "personName", "mark"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "安全登记检查表", columnNames, columnCodes, data, "安全登记检查表");

    }
}
