package com.dr.archive.tag.controller;

import com.dr.archive.tag.entity.TagLib;
import com.dr.archive.tag.entity.TagLibArchive;
import com.dr.archive.tag.entity.TagLibArchiveInfo;
import com.dr.archive.tag.service.TagLibArchiveService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: qiuyf
 * @date: 2022/3/16 9:42
 */
@RestController
@RequestMapping("api/tagLibArchive")
public class TagLibArchiveController extends BaseServiceController<TagLibArchiveService, TagLibArchive> {
    @Autowired
    TagLibArchiveService tagLibArchiveService;

    @Override
    protected SqlQuery<TagLibArchive> buildPageQuery(HttpServletRequest request, TagLibArchive entity) {
        SqlQuery<TagLibArchive> sqlQuery = SqlQuery.from(TagLibArchive.class)
                .like(TagLibArchiveInfo.TITLE, entity.getTitle())
                .like(TagLibArchiveInfo.TAGNAME, entity.getTagName())
                .like(TagLibArchiveInfo.ARCHIVECODE, entity.getArchiveCode())
                .equal(TagLibArchiveInfo.TAGLIBID, entity.getTagLibId())
                .equal(TagLibArchiveInfo.FORMDEFINITIONID, entity.getFormDefinitionId())
                .equal(TagLibArchiveInfo.FORMDATAID, entity.getFormDataId())
                .equal(TagLibArchiveInfo.STATUS, entity.getStatus())
                .orderByDesc(TagLibArchiveInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping({"/selectTagLibCount"})
    public ResultEntity selectTagLibCount(HttpServletRequest request,
                                          TagLib tagLib,
                                          @RequestParam(defaultValue = "0") int pageIndex,
                                          @RequestParam(defaultValue = "15") int pageSize,
                                          @RequestParam(defaultValue = "true") boolean page
    ) {
        SqlQuery sqlQuery = tagLibArchiveService.selectByTag(tagLib);
        Object result = page ? this.service.selectPage(sqlQuery, pageIndex, pageSize) : this.service.selectList(sqlQuery);
        return ResultEntity.success(result);
    }

    /**
     * 在档案详情页面添加标签
     */
    @RequestMapping({"/addTag"})
    public ResultEntity addTag(HttpServletRequest request, String tagName, String ctype, String formDataId, String formDefinitionId) {
        TagLib tagLib = new TagLib();
        tagLib.setTagName(tagName);
        tagLib.setCtype(ctype);
        // 在详情页面添加的需要在标签管理中审核通过
        tagLib.setStatus("0");
        List<TagLib> tagLibs = new ArrayList<>();
        tagLibs.add(tagLib);
        return ResultEntity.success(tagLibArchiveService.addTagLibArchives(tagLibs, formDataId, formDefinitionId, TagLibArchive.SOURCETYPE_ZIDINGYI));
    }

    //获取 档案详情知识图谱数据
    @RequestMapping({"/archiveKnowledgeGraph"})
    public ResultEntity archiveKnowledgeGraph(HttpServletRequest request, String formDefinitionId, String formDataId, String tagid) {
        return ResultEntity.success(tagLibArchiveService.archiveKnowledgeGraph(formDefinitionId, formDataId, tagid));
    }

    //自动给档案 打标签
    @RequestMapping({"/autoAddTag"})
    public ResultEntity autoAddTag(HttpServletRequest request, String formDataId, String formDefinitionId) {
        return ResultEntity.success(tagLibArchiveService.autoAddTag(formDataId, formDefinitionId));
    }

    @RequestMapping("/formAutoAddTag")
    public ResultEntity formAutoAddTag(HttpServletRequest request, String formDefinitionId) {
        tagLibArchiveService.formAutoAddTag(formDefinitionId);
        return ResultEntity.success("正在自动打标签中,请稍后查看结果!");
    }

    @RequestMapping("/formTagAddTag")
    public ResultEntity formTagAddTag(HttpServletRequest request, String formDefinitionId) {
        tagLibArchiveService.formTagAddTag(formDefinitionId, request.getParameter("type"));
        return ResultEntity.success("正在自动打标签中,请稍后查看结果!");
    }

    @RequestMapping("/getArchivesByKeyword")
    public ResultEntity getArchivesByKeyword(String keyword) {
        return ResultEntity.success(tagLibArchiveService.getArchivesByKeyword(keyword));
    }
}
