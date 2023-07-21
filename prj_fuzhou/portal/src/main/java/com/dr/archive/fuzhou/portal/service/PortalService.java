package com.dr.archive.fuzhou.portal.service;

import java.util.List;
import java.util.Map;

public interface PortalService {
    List<Map> resourceByCateGory(String userId);

    int openCount(String userId);

    /**
     * 获取门户系统地址
     *
     * @return
     */
    String getPortalSystem();
}
