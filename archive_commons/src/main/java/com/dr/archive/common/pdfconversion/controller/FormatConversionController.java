package com.dr.archive.common.pdfconversion.controller;

import com.dr.archive.common.pdfconversion.service.FormatConversionService;
import com.dr.framework.common.entity.ResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author caor
 * @Date 2020-11-02 17:55
 */
@RestController
@RequestMapping({"${common.api-path:/api}/formatconversion"})
public class FormatConversionController {
    @Autowired
    FormatConversionService formatConversionService;

    @RequestMapping("/fileToPdf")
    public ResultEntity fileToPdf(HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(!StringUtils.isEmpty(request.getParameter("fileId")), "id不能为空");
        formatConversionService.fileToPdf(request.getParameter("fileId"));
        return ResultEntity.success("正在执行转换操作，请稍后刷新原文列表查看转换结果！");
    }

    @RequestMapping("/batchfileToPdf")
    public ResultEntity batchfileToPdf(String fileIds) {
        formatConversionService.batchFileToPdf(fileIds);
        return ResultEntity.success("正在执行转换操作，请稍后刷新原文列表查看转换结果！");
    }

}
