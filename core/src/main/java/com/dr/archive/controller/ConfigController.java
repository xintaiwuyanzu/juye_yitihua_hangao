package com.dr.archive.controller;

import com.dr.archive.formMap.bo.FormKeyMapGroup;
import com.dr.archive.formMap.service.FormKeyMapService;
import com.dr.framework.common.entity.ResultListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档案全局配置配置相关controller
 * 用来统一配置信息，并且暴漏配置信息给前端调用
 *
 * @author dr
 */
@RestController
@RequestMapping("/api/archiveConfig/")
public class ConfigController {
    @Autowired
    FormKeyMapService formKeyMapService;

    /**
     * 获取全局字段映射配置列表
     *
     * @param fondCode
     * @param cateGoryCode
     * @param year
     * @param businessCode
     * @return
     */
    @PostMapping("/keyMapGroup")
    public ResultListEntity<FormKeyMapGroup> getKeyMapGroup(String fondCode, String cateGoryCode, String year, String businessCode) {
        return ResultListEntity.success(formKeyMapService.getFormKeyMapGroup(fondCode, cateGoryCode, year, businessCode));
    }

}
