package com.dr.archive.receive.online.service;

import java.util.Map;

/**
 * @description
 * @author:
 * @create: 2022-04-27 17:19
 **/
public interface OnlineStaticsService {
    Map<Object,Object> statics(String fondCode, long vintages);
}
