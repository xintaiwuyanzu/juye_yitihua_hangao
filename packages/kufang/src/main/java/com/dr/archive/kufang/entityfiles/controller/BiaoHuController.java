package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.BiaoHu;
import com.dr.archive.kufang.entityfiles.entity.BiaoHuInfo;
import com.dr.archive.kufang.entityfiles.service.BiaoHuService;
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
@RequestMapping("${common.api-path:/api}/biaoHu")
public class BiaoHuController extends BaseServiceController<BiaoHuService, BiaoHu> {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    protected SqlQuery<BiaoHu> buildPageQuery(HttpServletRequest httpServletRequest, BiaoHu biaoHu) {
        String startData = httpServletRequest.getParameter("startDate");
        String endData = httpServletRequest.getParameter("endDate");
        SqlQuery<BiaoHu> sqlQuery = SqlQuery.from(BiaoHu.class);
        if (!StringUtils.isEmpty(biaoHu.getKuFangMingCheng())) {
            sqlQuery.like(BiaoHuInfo.KUFANGMINGCHENG, biaoHu.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startData) && !StringUtils.isEmpty(endData)) {
            sqlQuery.greaterThanEqual(BiaoHuInfo.CREATEDATE, Long.parseLong(startData))
                    .lessThanEqual(BiaoHuInfo.CREATEDATE, Long.parseLong(endData) + (24 * 3600 - 1) * 1000);
        }

        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(BiaoHuInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(BiaoHuInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<BiaoHu> insert(HttpServletRequest request, BiaoHu entity) {
        Person person = BaseController.getUserLogin(request);
        entity.setCreateDate(System.currentTimeMillis());
        entity.setCreatePerson(person.getId());
        entity.setPersonName(person.getUserName());
        return super.insert(request, entity);
    }

    @RequestMapping("/exp")
    public void exp(HttpServletRequest request, HttpServletResponse response, BiaoHu biaoHu) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        SqlQuery<BiaoHu> sqlQuery = SqlQuery.from(BiaoHu.class);
        if (!StringUtils.isEmpty(biaoHu.getKuFangMingCheng())) {
            sqlQuery.like(BiaoHuInfo.KUFANGMINGCHENG, biaoHu.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(BiaoHuInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(BiaoHuInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(BiaoHuInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(BiaoHuInfo.CREATEDATE);
        List<BiaoHu> data = service.selectList(sqlQuery);
        String[] columnNames = {"年 月 日", "库房名称", "数量", "档号", "记录人", "交卷人", "收卷人", "备注"};
        String[] columnCodes = {"createDate", "kuFangMingCheng", "num", "archiveCode", "personName", "jiaoJuan", "shouJuan", "mark"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "档案裱糊统计表", columnNames, columnCodes, data, "档案裱糊统计表");
    }
}
