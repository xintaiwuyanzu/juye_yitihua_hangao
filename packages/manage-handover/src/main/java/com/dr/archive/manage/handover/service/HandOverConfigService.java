package com.dr.archive.manage.handover.service;

import com.dr.archive.manage.handover.entity.HandOverConfig;
import com.dr.framework.common.service.BaseService;

/**
 * 到期移交配置
 *
 * @author dr
 */
public interface HandOverConfigService extends BaseService<HandOverConfig> {

    /**
     * 正在运行状态
     */
    String STATUS_RUNNING = "1";
    /**
     * 暂停状态
     */
    String STATUS_STOP = "2";
    /**
     * 本年度已经完结状态
     */
    String STATUS_END = "3";


    /**
     * 查询当前登录人所属机构本年度到期移交配置
     *
     * @param organiseId
     * @return
     */
    HandOverConfig loadThisYearConfig(String organiseId);

    /**
     * 保存到期移交配置并且启动
     *
     * @param handOverConfig
     * @param start
     * @param organise
     * @return
     */
    HandOverConfig saveStart(HandOverConfig handOverConfig, boolean start, String organise);


    /**
     * 切换运行状态
     *
     * @param id
     * @return
     */
    boolean toggleConfig(String id);

}
