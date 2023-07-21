package com.dr.archive.fuzhou.portal.controller;

import com.dr.archive.fuzhou.portal.service.ExternalQueryService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/external")
public class ExternalQueryController {

    @Autowired
    ExternalQueryService externalQueryService;

    /**
     * 高级检索
     *
     * @param keyWord 关键词
     * @param type    类型
     * @param orgId   立档单位ID
     * @param index   页数
     * @param size    条数
     * @return
     */
    @RequestMapping("/advancedRetrieval")
    public ResultEntity advancedRetrieval(String keyWord, String type, String orgId,
                                          String operato,
                                          boolean multiSearch,
                                          @RequestParam(value = "index", defaultValue = "0") Integer index,
                                          @RequestParam(value = "size", defaultValue = "15") Integer size) {

        return ResultEntity.success(externalQueryService.advancedRetrieval(keyWord, type, orgId, operato, multiSearch, index, size));
    }
}
