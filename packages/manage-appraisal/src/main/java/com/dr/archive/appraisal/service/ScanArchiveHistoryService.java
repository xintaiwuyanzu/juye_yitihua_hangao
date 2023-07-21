package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.MatchingWordGroup;
import com.dr.archive.appraisal.entity.ScanArchiveHistory;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;

public interface ScanArchiveHistoryService extends BaseService<ScanArchiveHistory> {

    ResultEntity queryScanArchiveHistoryVo(Page<ScanArchiveHistory> scanArchiveHistoryPage);
}
