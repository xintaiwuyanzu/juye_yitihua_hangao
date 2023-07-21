package com.dr.archive.fuzhou.controller;

import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/intelligent")
public class IntelligentController {


    @Value("${fuzhou.customerService.login_url}")
    private String loginBase;

    @RequestMapping("/getUrl")
    public ResultEntity getUrl(){

        return ResultEntity.success(loginBase);
    }
}
