package com.dr.archive.common.statistics.service.impl;

import com.dr.archive.common.statistics.entity.ReportForm;
import com.dr.archive.common.statistics.entity.ReportFormInfo;
import com.dr.archive.common.statistics.service.ReportFormService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ReportFormServiceImpl implements ReportFormService {

    @Autowired
    CommonMapper commonMapper;

    @Override
    public Page<ReportForm> getReportForm(ReportForm reportForm, Integer index, Integer size) {

        SqlQuery<ReportForm> equal = SqlQuery.from(ReportForm.class)
                .equal(ReportFormInfo.ORGID, SecurityHolder.get().currentOrganise().getId())
                .orderByDesc(ReportFormInfo.CREATEDATE);

        if (!StringUtils.isEmpty(reportForm.getVintages())) {
            equal.equal(ReportFormInfo.VINTAGES, reportForm.getVintages());
        }

        return commonMapper.selectPageByQuery(equal, index * size, (index + 1) * size);
    }
}
