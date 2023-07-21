package com.dr.archive.common.file.controller;

import com.dr.archive.common.file.service.FileViewService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.common.entity.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: caor
 * @Date: 2022-01-09 15:40
 * @Description:
 */
@RestController
@RequestMapping({"${common.api-path:/api}/fileView"})
public class FileViewController {
    @Autowired
    FileViewService fileViewService;

    @RequestMapping("/fileTree")
    ResultListEntity<TreeNode> fileTree(HttpServletRequest request) {
        String refId = request.getParameter("refId");
        Assert.isTrue(!StringUtils.isEmpty(refId), "档案不能为空");
        return ResultListEntity.success(fileViewService.getFileTree(refId, request.getParameter("refType"), request.getParameter("groupCode")));
    }

    @RequestMapping("/getFile")
//TODO 调用云阅读和返回流的方式都用此方法，其他项目需要处理：返回流的方式改成void，会报异常未处理。云阅读需要返回String类型
    ResultEntity getFile(HttpServletRequest request, @RequestParam(name = "watermark", defaultValue = "true") boolean watermark, @RequestParam(name = "tools", defaultValue = "none") String tools, HttpServletResponse response) {
        String fileId = request.getParameter("fileId");
        String filePath = request.getParameter("filePath");
        Assert.isTrue(StringUtils.hasText(fileId), "文件ID不能为空！");
        Assert.isTrue(StringUtils.hasText(filePath), "文件路径不能为空！");
        return ResultEntity.success(fileViewService.getFile(response, fileId, filePath, "", request.getParameter("systemModel"), watermark, tools));
    }

    @RequestMapping("/getXml")
    ResultEntity getXml(HttpServletRequest request) {
        String fileId = request.getParameter("fileId");
        String filePath = request.getParameter("filePath");
        Assert.isTrue(StringUtils.hasText(fileId), "文件ID不能为空！");
        Assert.isTrue(StringUtils.hasText(filePath), "文件路径不能为空！");
        return ResultEntity.success(fileViewService.getXml(fileId, filePath));
    }
}
