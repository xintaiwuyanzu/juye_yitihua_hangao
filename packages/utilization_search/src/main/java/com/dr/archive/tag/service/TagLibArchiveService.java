package com.dr.archive.tag.service;

import com.dr.archive.tag.entity.TagLib;
import com.dr.archive.tag.entity.TagLibArchive;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;

import java.util.List;
import java.util.Map;

/**
 * @author: qiuyf
 * @date: 2022/3/15 11:38
 */
public interface TagLibArchiveService extends BaseService<TagLibArchive> {

    int addTagLibArchives(List<TagLib> tags, String formDataId, String formDefinitionId, String type);

    List<Map> selectByForm(String formDataId, String formDefinitionId);

    SqlQuery selectByTag(TagLib tagLib);

    List<Map> archiveKnowledgeGraph(String formDefinitionId, String formDataId, String tagid);

    int autoAddTag(String formDataId, String formDefinitionId);

    /**
     * 基于语义分析引擎自动打标签
     *
     * @param formDefinitionId
     */
    void formAutoAddTag(String formDefinitionId);

    /**
     * 基于标签库自动打标签
     * TODO 没处理性能问题，数据量大可能崩掉
     *
     * @param formDefinitionId
     */
    void formTagAddTag(String formDefinitionId, String type);

    List<TagLibArchive> getArchivesByKeyword(String keyword);
}
