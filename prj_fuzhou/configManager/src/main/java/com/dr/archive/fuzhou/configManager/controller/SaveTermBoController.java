package com.dr.archive.fuzhou.configManager.controller;

import com.dr.archive.fuzhou.configManager.service.SaveTermBoService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-04-22 17:12
 * @Description:
 */
@RestController
@RequestMapping({"${common.api-path:/api}/saveTermBo"})
public class SaveTermBoController {
    @Autowired
    SaveTermBoService saveTermBoService;

    @RequestMapping("/getCompilationContent")
    ResultEntity getCompilationContent(HttpServletRequest request) {
        String fondId = request.getParameter("fondId");
        Assert.isTrue(!StringUtils.isEmpty(fondId), "机构编码不能为空！");
        return ResultEntity.success(saveTermBoService.getCompilationContent(fondId));
    }
}
