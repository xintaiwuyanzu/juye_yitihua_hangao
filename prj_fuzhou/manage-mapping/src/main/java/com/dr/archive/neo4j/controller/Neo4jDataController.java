package com.dr.archive.neo4j.controller;

import com.dr.archive.entity.SmartSearchHistory;
import com.dr.archive.entity.TripletData;
import com.dr.archive.neo4j.service.Neo4jDataService;
import com.dr.archive.neo4j.vo.SearchVo;
import com.dr.archive.service.SmartSearchHistoryService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * luo
 */
@RestController
@RequestMapping("/api/neo4j")
public class Neo4jDataController {
    @Autowired
    Neo4jDataService neo4jService;
    @Autowired
    SmartSearchHistoryService searchHistoryService;

    @RequestMapping("/saveData")
    public ResultEntity saveData() {
        neo4jService.rebuildNeo4jData();
        return ResultEntity.success();
    }

    @RequestMapping("/saveRelations")
    public ResultEntity saveRelations() {
        neo4jService.saveRelation();
        return ResultEntity.success();
    }

    @RequestMapping("/getRelations")
    public ResultEntity getRelations(TripletData tripletData) {
        return ResultEntity.success(neo4jService.getRelations(tripletData));
    }

    @RequestMapping("/getNodes")
    public ResultEntity getNodes(String classType, String content, String jsonStr) {
        searchHistoryService.saveHistory(new SmartSearchHistory(classType, content, jsonStr));
        return ResultEntity.success(neo4jService.getNodes(classType, content, jsonStr));
    }

    @RequestMapping("/getRelationsByGraph")
    public ResultEntity getRelationsByGraph(SearchVo vo) {
        return ResultEntity.success(neo4jService.getRelationsByGraph(vo));
    }

    @RequestMapping("/getNodesByGraph")
    public ResultEntity getNodesByGraph(String classType, String content, String jsonStr) {
        searchHistoryService.saveHistory(new SmartSearchHistory(classType, content, jsonStr));
        return ResultEntity.success(neo4jService.getNodesByGraph(classType, content, jsonStr));
    }

}
