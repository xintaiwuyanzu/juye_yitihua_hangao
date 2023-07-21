package com.dr.archive.examine.controller;

import com.dr.archive.examine.service.KeyWordService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关键词管理，推送关键词管理
 */
@RestController
@RequestMapping("api/keyword")
public class KeyWordController {
    @Autowired
    KeyWordService keyWordService;

    @RequestMapping("/getOrg")
    public ResultEntity getOrg() {
        return ResultEntity.success(keyWordService.getOrganise());
    }
}
