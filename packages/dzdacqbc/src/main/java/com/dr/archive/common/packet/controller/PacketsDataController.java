package com.dr.archive.common.packet.controller;

import com.dr.archive.common.packet.service.PacketsDataService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2021/11/23 14:22
 * @Description:
 */
@RestController
@RequestMapping(("api/packetsData"))
public class PacketsDataController {
    @Autowired
    PacketsDataService packetsDataService;

    @RequestMapping("/packet")
    ResultEntity packetData(HttpServletRequest request) {
        String formDefinitionId = request.getParameter("formDefinitionId");
        String formDataId = request.getParameter("formDataId");
        Assert.isTrue(!StringUtils.isEmpty(formDefinitionId), "formdefinitionId不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(formDataId), "formDataId不能为空！");
        packetsDataService.packet(formDefinitionId, formDataId, null);
        return ResultEntity.success("正在打包");
    }

    @RequestMapping("/parser")
    ResultEntity parser(HttpServletRequest request) {
        packetsDataService.parser(null);
        return ResultEntity.success("正在解包");
    }
}
