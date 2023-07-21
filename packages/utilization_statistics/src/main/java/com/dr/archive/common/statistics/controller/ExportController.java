package com.dr.archive.common.statistics.controller;

import com.dr.archive.common.statistics.entity.ExportEntity;
import com.dr.archive.common.statistics.service.ExportService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/export")
public class ExportController {

    @Autowired
    Map<String, ExportService> exportServiceMap;

    /**
     * 统计报表导出
     * @param exportEntity
     * @return
     */
    @RequestMapping("/exportExcel")
    public ResultEntity exportExcel(ExportEntity exportEntity) {


        ExportService exportService = exportServiceMap.get(exportEntity.getExportType());

        return ResultEntity.success(exportService.exportByType(exportEntity));
    }
}
