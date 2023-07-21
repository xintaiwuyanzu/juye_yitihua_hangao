package com.dr.archive.manage.fond.service;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondChangeHistory;
import com.dr.framework.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;


/**
 * @author
 */
public interface FondChangeHistoryService extends BaseService<FondChangeHistory> {

    void add(Fond entity);

    void updateByChange(Fond oldFond, Fond entity);

    void deleteChange(HttpServletRequest request, Fond fond);
}
