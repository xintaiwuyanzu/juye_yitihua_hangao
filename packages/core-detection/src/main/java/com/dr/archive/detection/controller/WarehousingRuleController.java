package com.dr.archive.detection.controller;

import com.dr.archive.detection.entity.WarehousingRule;
import com.dr.archive.detection.entity.WarehousingRuleInfo;
import com.dr.archive.detection.service.WarehousingRuleService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/warehousingRule")
public class WarehousingRuleController extends BaseServiceController<WarehousingRuleService, WarehousingRule> {
    @Override
    protected SqlQuery<WarehousingRule> buildPageQuery(HttpServletRequest httpServletRequest, WarehousingRule warehousingRule) {
        return SqlQuery.from(WarehousingRule.class).like(WarehousingRuleInfo.RULENATURE, warehousingRule.getRuleNature())
                .like(WarehousingRuleInfo.TESTTYPE, warehousingRule.getTestType())
                .like(WarehousingRuleInfo.TESTOBJECT, warehousingRule.getTestObject())
                .like(WarehousingRuleInfo.PROJECT, warehousingRule.getProject())
                .orderByDesc(WarehousingRuleInfo.CREATEDATE);
    }


    @Autowired
    WarehousingRuleService warehousingRuleService;


    @RequestMapping("/upload")
    public ResultEntity importPressNeed2(MultipartFile file) throws Exception {
        String s = warehousingRuleService.uploadExcel(file);
        if (s == "true") {
            return ResultEntity.success();
        } else {
            return ResultEntity.error("失败");
        }
    }
}
