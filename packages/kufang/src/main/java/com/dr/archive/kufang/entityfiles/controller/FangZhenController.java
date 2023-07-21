package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.FangZhen;
import com.dr.archive.kufang.entityfiles.entity.FangZhenInfo;
import com.dr.archive.kufang.entityfiles.service.FangZhenService;
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
@RequestMapping({"${common.api-path:/api}/fangZhen"})
public class FangZhenController extends BaseServiceController<FangZhenService, FangZhen> {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    protected SqlQuery<FangZhen> buildPageQuery(HttpServletRequest httpServletRequest, FangZhen fangZhen) {
        String startDate = httpServletRequest.getParameter("startDate");
        String endDate = httpServletRequest.getParameter("endDate");
        SqlQuery<FangZhen> sqlQuery = SqlQuery.from(FangZhen.class);
        if (!StringUtils.isEmpty(fangZhen.getKuFangMingCheng())) {
            sqlQuery.like(FangZhenInfo.KUFANGMINGCHENG, fangZhen.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(FangZhenInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(FangZhenInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        List<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person -> {
            persons.add(person.getId());
        });
        sqlQuery.in(FangZhenInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(FangZhenInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<FangZhen> insert(HttpServletRequest request, FangZhen entity) {
        Person person = BaseController.getUserLogin(request);
        entity.setCreateDate(System.currentTimeMillis());
        entity.setCreatePerson(person.getId());
        entity.setPersonName(person.getUserName());
        return super.insert(request, entity);
    }

    @RequestMapping("/exp")
    public void exp(HttpServletRequest request, HttpServletResponse response, FangZhen fangZhen) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        SqlQuery<FangZhen> sqlQuery = SqlQuery.from(FangZhen.class);
        if (!StringUtils.isEmpty(fangZhen.getKuFangMingCheng())) {
            sqlQuery.like(FangZhenInfo.KUFANGMINGCHENG, fangZhen.getKuFangMingCheng());
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sqlQuery.greaterThanEqual(FangZhenInfo.CREATEDATE, Long.parseLong(startDate))
                    .lessThanEqual(FangZhenInfo.CREATEDATE, Long.parseLong(endDate) + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> organiseDefaultPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        List<String> persons = new ArrayList<>();
        organiseDefaultPersons.forEach(person -> {
            persons.add(person.getId());
        });
        sqlQuery.in(FangZhenInfo.CREATEPERSON, persons);
        sqlQuery.orderByDesc(FangZhenInfo.CREATEDATE);
        List<FangZhen> data = service.selectList(sqlQuery);
        String[] columnNames = {"年 月 日", "任务名称", "申请单位", "记录人", "库房名称", "题名", "档号", "复制用途", "仿真要求", "处理情况", "备注"};
        String[] columnCodes = {"createDate", "taskName", "unit", "personName", "kuFangMingCheng", "title", "archiveCode", "copyPurpose", "copyRequire", "situation", "mark"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "档案仿真统计表", columnNames, columnCodes, data, "档案仿真统计表");
    }
}
