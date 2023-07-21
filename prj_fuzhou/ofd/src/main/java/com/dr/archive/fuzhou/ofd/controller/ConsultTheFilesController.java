package com.dr.archive.fuzhou.ofd.controller;

import com.dr.archive.fuzhou.ofd.service.ConsultTheFilesService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: caor
 * @Date: 2021-12-24 19:27
 * @Description: 自主查档相关
 */
@RestController
@RequestMapping("/api/consultTheFiles")
public class ConsultTheFilesController {
    @Autowired
    ConsultTheFilesService consultTheFilesService;

    /**
     * 原文预览，调用ofd云阅读，根据formDataId，查询第一条原文，多的过滤，无原文的返回原文不存在
     *
     * @return
     */
    @RequestMapping("/filePreview")
    public ResultEntity filePreview(@RequestParam(name = "refId") String refId, Person person) {
        return ResultEntity.success(consultTheFilesService.filePreview(refId, person));
    }
}
