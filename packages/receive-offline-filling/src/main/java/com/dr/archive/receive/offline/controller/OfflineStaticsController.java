package com.dr.archive.receive.offline.controller;

import com.dr.archive.receive.offline.service.OfflineStaticsService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author:
 * @create: 2022-04-27 15:21
 **/
@RestController
@RequestMapping("${common.api-path:/api}/offlineStatics")
public class OfflineStaticsController {
    @Autowired
    OfflineStaticsService service;

    @RequestMapping("/getArchiveOffline")
    public ResultEntity getArchiveOffline(String fondCode, long time){
        return ResultEntity.success(service.statics(fondCode,time));
    }
}
