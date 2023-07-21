package com.dr.archive.detection.controller;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.service.TestRecordItemService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/5/12 16:51
 */
@RestController
@RequestMapping("${common.api-path:/api}/testRecordItem")
public class TestRecordItemController {
    @Autowired
    TestRecordItemService testRecordItemService;

    //查询四性检测报告
    @RequestMapping({"/testReport"})
    public ResultEntity testReport(HttpServletRequest request, TestRecordItem testRecordItem) {
        return ResultEntity.success(testRecordItemService.testReport(testRecordItem));
    }
}
