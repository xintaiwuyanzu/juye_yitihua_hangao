package com.dr.archive.manage.handover.controller;

import com.dr.archive.manage.handover.entity.HandOverConfig;
import com.dr.archive.manage.handover.entity.HandOverConfigInfo;
import com.dr.archive.manage.handover.service.HandOverConfigService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 移交配置时限
 *
 * @author dr
 */
@RestController
@RequestMapping({"${common.api-path:/api}/manage/handoverConfig"})
public class HandOverConfigController extends BaseServiceController<HandOverConfigService, HandOverConfig> {

    @Override
    protected SqlQuery<HandOverConfig> buildPageQuery(HttpServletRequest httpServletRequest, HandOverConfig handOverConfig) {
        Organise organise = BaseController.getOrganise(httpServletRequest);
        SqlQuery<HandOverConfig> sqlQuery = SqlQuery.from(HandOverConfig.class);

        sqlQuery.equal(HandOverConfigInfo.ORGANISEID, organise.getId());

        return sqlQuery.orderByDesc(HandOverConfigInfo.CREATEDATE);
    }

    @PostMapping("/saveStart")
    public ResultEntity<HandOverConfig> saveStart(HandOverConfig handOverConfig, boolean start, @Current Organise organise) {
        return ResultEntity.success(service.saveStart(handOverConfig, start, organise.getId()));
    }

    /**
     * 切换指定的到期移交配置
     *
     * @param id
     * @return
     */
    @PostMapping("/toggle")
    public ResultEntity<Boolean> saveStart(String id) {
        return ResultEntity.success(service.toggleConfig(id));
    }

    /**
     * 加载指定单位本年度到期移交配置
     *
     * @param organise
     * @return
     */
    @PostMapping("/loadThisYearConfig")
    public ResultEntity<HandOverConfig> loadThisYearConfig(@Current Organise organise) {
        return ResultEntity.success(service.loadThisYearConfig(organise.getId()));
    }

}
