package com.dr.archive.statistics.service.impl;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.statistics.entity.ReportGenerate;
import com.dr.archive.statistics.entity.ReportGenerateInfo;
import com.dr.archive.statistics.enums.HandOverField;
import com.dr.archive.statistics.enums.ReportField;
import com.dr.archive.statistics.service.ReportGenerateService;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: yang
 * @create: 2022-07-22 15:37
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class ReportGenerateServiceImpl extends DefaultBaseService<ReportGenerate> implements ReportGenerateService {

    @Autowired
    CommonService commonService;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Override
    public List<ReportGenerate> total() {
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Fond> fonds = fondOrganiseService.getFondListByOrganiseId(organise.getId());
        SqlQuery<ReportGenerate> sqlQuery = SqlQuery.from(ReportGenerate.class,false)
                .count(ReportGenerateInfo.ID,"id",false,false)
                .column(ReportGenerateInfo.REPORTANNUAL)
                .groupBy(ReportGenerateInfo.REPORTANNUAL);
        if (!"root".equals(organise.getId())) {
            sqlQuery.equal(ReportGenerateInfo.FONDCODE,fonds.get(0).getCode());
        }
        List<ReportGenerate> totals = commonMapper.selectByQuery(sqlQuery);
            return totals;
    }

    @Override
    public Page<ReportGenerate> selectPage(SqlQuery<ReportGenerate> var1, int var2, int var3){
        List<ReportGenerate> reportGenerates = commonMapper.selectByQuery(var1);
        Page<ReportGenerate> page = commonService.selectPage(var1, var2, var3);
        page.setTotal(reportGenerates.size());
        return page;
    }

    /**
     * 获得默认展示字段信息
     */
    @Override
    public List<Map<String, String>> getFields(String reportType) {
        List<Map<String, String>> list = new ArrayList<>();
        if("sl".equals(reportType)){
            ReportField[] values = ReportField.values();
            for (ReportField value : values) {
                Map<String, String> map = new HashMap<>();
                map.put("key", value.getKey());
                map.put("label", value.getLabel());
                map.put("title", value.getLabel());
                list.add(map);
            }
        }else if("yj".equals(reportType)){
            HandOverField[] values = HandOverField.values();
            for (HandOverField value : values) {
                Map<String, String> map = new HashMap<>();
                map.put("key", value.getKey());
                map.put("label", value.getLabel());
                map.put("title", value.getLabel());
                list.add(map);
            }
        }
        return list;
    }
}