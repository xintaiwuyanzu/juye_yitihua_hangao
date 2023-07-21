package com.dr.archive.fuzhou.configManager.controller;

import com.dr.archive.fuzhou.configManager.service.ArchiveCodeService;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-01-06 15:51
 * @Description:
 */
@RestController
@RequestMapping({"${common.api-path:/api}/archivecode"})
public class ArchiveCodeController {
    @Autowired
    ArchiveCodeService archiveCodeService;

    @RequestMapping("/getArchiveByCategoryCodeAndYear")
    ResultEntity getArchiveByCategoryCodeAndYear(HttpServletRequest request, @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent, BatchCreateQuery query) {
        String typeCode = request.getParameter("typeCode");
        Assert.isTrue(!StringUtils.isEmpty(typeCode), "分类编码不能为空！");
        query.parseQuery(queryContent);
        return ResultEntity.success(archiveCodeService.getArchiveByCategoryCodeAndYear(typeCode, query));
    }
}
