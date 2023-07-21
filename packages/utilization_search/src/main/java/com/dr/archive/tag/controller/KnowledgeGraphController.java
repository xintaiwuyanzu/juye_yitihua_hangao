package com.dr.archive.tag.controller;

import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.service.ArchiveCarBatchService;
import com.dr.archive.tag.entity.KnowledgeGraph;
import com.dr.archive.tag.entity.KnowledgeGraphInfo;
import com.dr.archive.tag.service.KnowledgeGraphService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/3/30 9:50
 */
@RestController
@RequestMapping("api/knowledgeGraph")
public class KnowledgeGraphController extends BaseServiceController<KnowledgeGraphService, KnowledgeGraph> {
    @Autowired
    KnowledgeGraphService knowledgeGraphService;
    @Autowired
    ArchiveCarBatchService carBatchService;
    @Override
    protected SqlQuery<KnowledgeGraph> buildPageQuery(HttpServletRequest request, KnowledgeGraph entity) {
        SqlQuery<KnowledgeGraph> sqlQuery = SqlQuery.from(KnowledgeGraph.class);
        if(!StringUtils.isEmpty(entity.getThemeName())){
            sqlQuery.like(KnowledgeGraphInfo.THEMENAME, entity.getThemeName());
        }
        sqlQuery.orderByDesc(KnowledgeGraphInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping({"/insert"})
    public ResultEntity<KnowledgeGraph> insert(HttpServletRequest request, KnowledgeGraph entity) {

        this.service.insert(entity);
        //同步创建档案车
        ArchiveCarBatch carBatch = new ArchiveCarBatch();
        //档案车Id与档案查档批次相同
        carBatch.setId(entity.getId());
        carBatch.setBatchName("图谱车【" + entity.getThemeName() + "】");
        carBatchService.insert(carBatch);
        return ResultEntity.success(entity);
    }

    @RequestMapping({"/updategraphData"})
    public ResultEntity updategraphData(HttpServletRequest request, String id, String graphData) {
        SqlQuery sqlQuery = SqlQuery.from(KnowledgeGraph.class);
        sqlQuery.equal(KnowledgeGraphInfo.ID,id)
                .set(KnowledgeGraphInfo.GRAPHDATA,graphData);
        long aa = this.service.updateBySqlQuery(sqlQuery);
        return ResultEntity.success(aa);
    }

    /*
     * 查询 知识图谱数据
     */
    @RequestMapping({"/getGraphDataByKw"})
    public ResultEntity getGraphDataByKw(HttpServletRequest request, String id){
        return ResultEntity.success(knowledgeGraphService.getGraphDataByKw(id));
    }


}
