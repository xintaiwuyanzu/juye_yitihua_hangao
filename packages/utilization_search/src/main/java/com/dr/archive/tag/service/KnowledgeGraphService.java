package com.dr.archive.tag.service;

import com.dr.archive.tag.entity.KnowledgeGraph;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author: qiuyf
 * @date: 2022/3/30 9:50
 */
public interface KnowledgeGraphService extends BaseService<KnowledgeGraph> {
    List<Map> getGraphDataByKw(String id);
}
