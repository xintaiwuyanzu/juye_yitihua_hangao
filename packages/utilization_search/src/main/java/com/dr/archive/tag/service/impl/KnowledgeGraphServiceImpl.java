package com.dr.archive.tag.service.impl;

import com.dr.archive.tag.entity.KnowledgeGraph;
import com.dr.archive.tag.entity.TagLib;
import com.dr.archive.tag.entity.TagLibArchiveInfo;
import com.dr.archive.tag.entity.TagLibInfo;
import com.dr.archive.tag.service.KnowledgeGraphService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: qiuyf
 * @date: 2022/3/30 9:49
 */
@Service
public class KnowledgeGraphServiceImpl extends DefaultBaseService<KnowledgeGraph> implements KnowledgeGraphService {
    public List<Map> getGraphDataByKw(String id){
        List<Map> mapList = new ArrayList<>();
        KnowledgeGraph knowledgeGraph = selectById(id);
        List<String> keyWordIds = Arrays.asList(knowledgeGraph.getKeywords().split(","));
        SqlQuery sqlQuery = SqlQuery.from(TagLib.class, false)
                .join(TagLibInfo.ID, TagLibArchiveInfo.TAGLIBID)
                .column(TagLibArchiveInfo.TITLE, TagLibArchiveInfo.ARCHIVECODE, TagLibArchiveInfo.FORMDATAID, TagLibArchiveInfo.FORMDEFINITIONID, TagLibInfo.TAGNAME, TagLibInfo.ID)
                .in(TagLibArchiveInfo.TAGLIBID, keyWordIds)
                .orderByDesc(TagLibArchiveInfo.CREATEDATE)
                .setReturnClass(Map.class);
        mapList = commonMapper.selectByQuery(sqlQuery);
        return mapList;
    }
}
