package com.dr.archive.common.statistics.service;

import com.dr.archive.common.statistics.entity.ReportForm;
import com.dr.framework.common.page.Page;

public interface ReportFormService {

    Page<ReportForm> getReportForm(ReportForm reportForm, Integer index, Integer size);

}
