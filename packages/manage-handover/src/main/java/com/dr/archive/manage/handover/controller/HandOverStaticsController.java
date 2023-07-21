package com.dr.archive.manage.handover.controller;

import com.dr.archive.manage.handover.service.HandOverStatics;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author:
 * @create: 2022-04-27 17:58
 **/
@RestController
@RequestMapping({"${common.api-path:/api}/handoverStatics"})
public class HandOverStaticsController {
    @Autowired
    HandOverStatics service;

    @RequestMapping("/getHandoverStatics")
    public ResultEntity getHandoverStatics(String fondCode, long time){
        return ResultEntity.success(service.statics(fondCode,time));
    }
}
