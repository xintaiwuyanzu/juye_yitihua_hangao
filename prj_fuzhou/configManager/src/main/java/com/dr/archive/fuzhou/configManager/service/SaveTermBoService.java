package com.dr.archive.fuzhou.configManager.service;

/**
 * @Author: caor
 * @Date: 2022-04-22 17:14
 * @Description:
 */
public interface SaveTermBoService {
    /**
     * 获取智能归档配置系统的保管期限
     *
     * @param fondId
     * @return
     */
    String getCompilationContent(String fondId);
}
