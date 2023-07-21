package com.dr.archive.receive.online.controller;

import com.dr.archive.receive.online.service.OnlineStaticsService;
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
@RequestMapping("${common.api-path:/api}/onlineStatics")
public class OnlineStaticsController {
    @Autowired
    OnlineStaticsService service;

    @RequestMapping("/getArchiveOnline")
    public ResultEntity getArchiveOnline(String fondCode, long time){
        return ResultEntity.success(service.statics(fondCode,time));
    }
}
