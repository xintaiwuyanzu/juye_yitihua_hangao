package com.dr.archive.specialTag.controller;

import com.dr.archive.specialTag.entity.SpecialTag;
import com.dr.archive.specialTag.entity.SpecialTagArchive;
import com.dr.archive.specialTag.entity.SpecialTagArchiveInfo;
import com.dr.archive.specialTag.service.SpecialTagArchiveService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/6/18 14:09
 */
@RestController
@RequestMapping("api/specialTagArchive")
public class SpecialTagArchiveController extends BaseServiceController<SpecialTagArchiveService, SpecialTagArchive> {
    @Override
    protected SqlQuery<SpecialTagArchive> buildPageQuery(HttpServletRequest request, SpecialTagArchive entity) {
        SqlQuery<SpecialTagArchive> sqlQuery = SqlQuery.from(SpecialTagArchive.class)
                .equal(SpecialTagArchiveInfo.FORMDATAID, entity.getFormDataId())
                .equal(SpecialTagArchiveInfo.FORMDEFINITIONID, entity.getFormDefinitionId())
                .like(SpecialTagArchiveInfo.SPECIALTAGNAME, entity.getSpecialTagName());
        return sqlQuery;
    }

    /**
     * 在档案详情页面添加专题标签
     */
    @RequestMapping({"/addSpecialTag"})
    public ResultEntity addTag(HttpServletRequest request, String tagId, String formDataId, String formDefinitionId) {
        return ResultEntity.success(service.addSpecialTag(tagId, formDataId, formDefinitionId));
    }

    @RequestMapping({"/selectTagCount"})
    public ResultEntity selectTagCount(HttpServletRequest request,
                                       SpecialTag specialTag,
                                       @RequestParam(defaultValue = "0") int pageIndex,
                                       @RequestParam(defaultValue = "15") int pageSize,
                                       @RequestParam(defaultValue = "true") boolean page) {
        SqlQuery sqlQuery = service.selectTagCount(specialTag);
        Object result = page ? this.service.selectPage(sqlQuery, pageIndex, pageSize) : this.service.selectList(sqlQuery);
        return ResultEntity.success(result);
    }
}
