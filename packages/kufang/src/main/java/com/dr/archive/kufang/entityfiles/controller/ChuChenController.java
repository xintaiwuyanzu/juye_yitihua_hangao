package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.ChuChen;
import com.dr.archive.kufang.entityfiles.entity.ChuChenInfo;
import com.dr.archive.kufang.entityfiles.service.ChuChenService;
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
@RequestMapping("${common.api-path:/api}/chuChen")
public class ChuChenController extends BaseServiceController<ChuChenService, ChuChen> {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    protected SqlQuery<ChuChen> buildPageQuery(HttpServletRequest httpServletRequest, ChuChen chuChen) {
        String startData = httpServletRequest.getParameter("startDate");
        String endData = httpServletRequest.getParameter("endDate");
        SqlQuery<ChuChen> sqlQuery = SqlQuery.from(ChuChen.class);
        if (!StringUtils.isEmpty(chuChen.getKuFangMingCheng())) {
            sqlQuery.like(ChuChenInfo.KUFANGMINGCHENG, chuChen.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startData) && !StringUtils.isEmpty(endData)) {
            sqlQuery.greaterThanEqual(ChuChenInfo.CREATEDATE, Long.parseLong(startData))
                    .lessThanEqual(ChuChenInfo.CREATEDATE, Long.parseLong(endData) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(ChuChenInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(ChuChenInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<ChuChen> insert(HttpServletRequest request, ChuChen entity) {
        Person person = BaseController.getUserLogin(request);
        entity.setCreateDate(System.currentTimeMillis());
        entity.setCreatePerson(person.getId());
        entity.setPersonName(person.getUserName());
        return super.insert(request, entity);
    }

    @RequestMapping("/exp")
    public void exp(HttpServletRequest request, HttpServletResponse response, ChuChen chuChen) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        SqlQuery<ChuChen> sqlQuery = SqlQuery.from(ChuChen.class);
        if (!StringUtils.isEmpty(chuChen.getKuFangMingCheng())) {
            sqlQuery.like(ChuChenInfo.KUFANGMINGCHENG, chuChen.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(ChuChenInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(ChuChenInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        ArrayList<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person ->
                persons.add(person.getId()));
        sqlQuery.in(ChuChenInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(ChuChenInfo.CREATEDATE);
        List<ChuChen> data = service.selectList(sqlQuery);
        String[] columnNames = {"年 月 日", "库房名称", "除尘情况", "清理人", "记录人", "采取措施"};
        String[] columnCodes = {"createDate", "kuFangMingCheng", "chuChen", "cleaner", "personName", "mark"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "档案除尘统计表", columnNames, columnCodes, data, "档案除尘统计表");

    }
}
