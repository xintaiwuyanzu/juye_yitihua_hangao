package com.dr.archive.receive.offline.service;

import java.util.Map;

/**
 * @description
 * @author:
 * @create: 2022-04-27 15:09
 **/
public interface OfflineStaticsService {

    Map<Object,Object> statics(String fondCode, long vintages);
}
