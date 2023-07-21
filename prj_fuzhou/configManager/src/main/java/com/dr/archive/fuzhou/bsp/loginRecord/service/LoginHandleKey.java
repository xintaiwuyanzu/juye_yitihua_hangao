package com.dr.archive.fuzhou.bsp.loginRecord.service;

/**
 * @author lych
 * @date 2023/3/20 下午 4:34
 * @mood happy
 */
public interface LoginHandleKey {

    /**
     * 运行天数
     */
    String SYSTEM_DAY = "system_day";
    /**
     * 当前登录人数
     */
    String CURRENT_PERSON = "current_person";
    /**
     * 当天最大登录数量
     */
    String CURRENT_DAY_PERSON = "current_day_person";
    /**
     * 最大登录人数
     */
    String MAX_PERSON = "max_person";
    /**
     * 历史登录数
     */
    String HISTORY_PERSON = "history_person";
}
