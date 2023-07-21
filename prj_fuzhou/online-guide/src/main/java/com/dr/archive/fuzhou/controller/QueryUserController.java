package com.dr.archive.fuzhou.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.UserLogin;
import com.dr.framework.core.organise.service.LoginService;
import org.apache.tomcat.util.security.MD5Encoder;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/queryUser")
public class QueryUserController {

    @Autowired
    LoginService loginService;

    @RequestMapping("/getUser")
    public ResultEntity getUser(String personId){
        List<UserLogin> userLogins = loginService.userLogin(personId);
        for (UserLogin userLogin :userLogins){
            if("bsp".equals(userLogin.getUserType())){
                return ResultEntity.success(userLogin.getPassword());
            }
        }
            return ResultEntity.error("");
    }
}
