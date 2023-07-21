package com.dr.archive.neo4j.service;

import com.dr.archive.entity.RealmClass;
import com.dr.archive.entity.TripletData;
import com.dr.archive.entity.TripletDataInfo;
import com.dr.archive.neo4j.vo.CommonEntity;
import com.dr.archive.neo4j.vo.NodeRelation;
import com.dr.archive.neo4j.vo.SearchVo;
import com.dr.archive.service.RealmClassService;
import com.dr.archive.service.TripletDataService;
import com.dr.archive.util.JsonUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: qiuyf
 * @date: 2022/6/28 14:43
 */
@Service
public class Neo4jDataService {
    @Autowired
    RealmClassService realmClassService;
    @Autowired
    FormDataService formDataService;
    @Resource
    CommonMapper commonMapper;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    TripletDataService tripletDataService;
    @Autowired
    Driver driver;

    Session session = null;

    static final Logger logger = LoggerFactory.getLogger(Neo4jDataService.class);

    //图数据库所有对象
    String[] classes = new String[]{"person", "organize", "event", "thing", "land", "entity"};

    static String classType;

    //对象与关系数据同步到neo4j
    public void rebuildNeo4jData() {
        Session session = driver.session();
        //查所有的对象
        List<RealmClass> realmClassList = realmClassService.selectList(SqlQuery.from(RealmClass.class));
        for (RealmClass realmClass : realmClassList) {
            String label = realmClass.getFormTable();
            //TODO 其他表结构有问题
            if(!label.equals("person")) continue;
            //根据对象查对象数据
            SqlBuilder sqlBuilder = (sqlQuery, wrapper) -> {
                sqlQuery.isNotNull(wrapper.getColumn("NAME"))
                        .isNull(wrapper.getColumn("STATUS"))
                        .or()
                        .equal(wrapper.getColumn("STATUS"), "0");
            };
            Page<FormData> page = formDataService.selectPageFormData(realmClass.getFormId(), sqlBuilder, 0, 1000);
            if (page.getData().size() == 0) continue;
            for (FormData formData : page.getData()) {
                String name = formData.get("NAME");
                if (StringUtils.hasText(name)) {
                    //保存节点
                    String sql = String.format("CREATE (node:%s {name:'%s'})", label, name.replaceAll("'", "_"));
                    logger.info(sql);
                    Result result = session.run(sql);
                    if (result != null) {
                        formData.put("STATUS", "1");
                        formDataService.updateFormDataById(formData);
                    }
                }
            }
        }
        saveRelation();
    }

    /**
     * 保存关系到图数据库
     */
    public void saveRelation() {
        session = driver.session();
        //关系数据
        List<TripletData> tripletDataList = commonMapper.selectByQuery(SqlQuery.from(TripletData.class)
                .isNull(TripletDataInfo.STATUS)
                .or()
                .equal(TripletDataInfo.STATUS, "0")
                .groupBy(TripletDataInfo.SOURCENAME, TripletDataInfo.TARGETNAME));
        try {
            for (TripletData t : tripletDataList) {
                String sourceName = t.getSourceName();
                String targetName = t.getTargetName();
                String source = formDefinitionService.selectFormDefinitionById(t.getSourceFormId()).getFormCode();
                String target = formDefinitionService.selectFormDefinitionById(t.getTargetFormId()).getFormCode();
                //如果关系的两个对象都存在图数据库，才保存关系到图数据库
                String s1 = String.format("MATCH (n:%s) where n.name='%s' return n", source, sourceName);
                String s2 = String.format("MATCH (n:%s) where n.name='%s' return n", target, targetName);
                List<Record> list = session.run(s1).list();
                List<Record> list1 = session.run(s2).list();
                if (list1.size() > 0 && list.size() > 0) {
                    String sql = String.format("MATCH (p:%s),(p2:%s) " +
                            "WHERE p.name = '%s' and p2.name ='%s' " +
                            "Merge (p)-[r:%s]->(p2)", source, target, sourceName, targetName, t.getRelationName());
                    logger.info(sql);
                    Result result = session.run(sql);
                    if (result != null) {
                        t.setStatus("1");
                        tripletDataService.updateById(t);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * @param t 关系数据
     * @return
     */
    public Object getRelations(TripletData t) {
        //0代表查所有，1代表根据源和目标对象的数据id查找，2代表根据关系名字查找，3代表根据源和目标对象表单id查找
        int i = Integer.parseInt(t.getStatus());
        Integer order = t.getOrder();
        String sql = null;
        String relationName = t.getRelationName();
        String sourceFormId = t.getSourceFormId();
        String targetFormId = t.getTargetFormId();
        String source = "";
        String target = "";
        if (StringUtils.hasText(t.getSourceFormId())) {
            source = ":" + formDefinitionService.selectFormDefinitionById(sourceFormId).getFormCode();
        }
        if (StringUtils.hasText(t.getSourceFormId())) {
            target = ":" + formDefinitionService.selectFormDefinitionById(targetFormId).getFormCode();
        }
        if (i == 0) {
            sql = String.format("MATCH p=(a%s)-[r]->(b%s) RETURN p", source, target);
        } else if (i == 2) {
            sql = String.format("MATCH p=(a%s)-[r:`%s`]->(b%s) RETURN p", source, target, relationName);
        }
        if (order != null && order != 0) {
            sql = String.format(sql + " LIMIT %d", order);
        }

        return getResult(sql);
    }


    public List<CommonEntity> getNodes(String classType, String content, String jsonStr) {
        session = driver.session();
        String query;
        if (StringUtils.hasText(content)) {
            query = String.format("MATCH (n:%s) where n.name contains '%s'  RETURN n", classType, content);
        } else {
            query = String.format("MATCH (n:%s) RETURN n LIMIT 100", classType);
        }
        logger.info(query);
        Result result = session.run(query);
        List<Map<String, Object>> list = result.list(Record::asMap);
        List<CommonEntity> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Node n = (Node) map.get("n");
            String name = n.keys().iterator().next();
            resultList.add(new CommonEntity(n.id() + "", name));
        }
        return resultList;
    }

    private CommonEntity getEntity(String id, String name) {
        try {
            id = JsonUtils.jsonToObject(id, String.class);
            name = JsonUtils.jsonToObject(name, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonEntity(id, name);
    }


    /////////////////////////////////////////////////以下为从图数据库查找

    public List<CommonEntity> getNodesByGraph(String classType, String content, String jsonStr) {
        session = driver.session();
        String query;
        List<CommonEntity> all = new ArrayList<>();
        if (StringUtils.hasText(content)) {
            for (String cl : classes) {
                query = String.format("MATCH (n:%s) where n.name = '%s' RETURN n order by id(n)", cl, content);
                List<CommonEntity> result = getNodeEntity(query);
                all.addAll(result);
            }
        }
        return all;
    }

    private List<CommonEntity> getNodeEntity(String sql) {
        session = driver.session();
        logger.info(sql);
        Result result = session.run(sql);
        List<Map<String, Object>> list = result.list(Record::asMap);
        List<CommonEntity> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Node n = (Node) map.get("n");
            Iterable<String> keys = n.keys();
            Iterator<String> iterator = keys.iterator();
            resultList.add(getEntity(n.id() + "", iterator.next().toString()));
        }
        return resultList;
    }

    public List<NodeRelation> getRelationsByGraph(SearchVo search) {
        Integer type = search.getType();
        String name = search.getName();
        String relationName = search.getRelationName();
        Integer pageSize = search.getPageSize();
        classType = search.getClassType();
        String sql;
        if (StringUtils.hasText(relationName)) {
            sql = String.format("MATCH p=(a:%s)-[r]->(b)  where r.name='%s' RETURN p", "entity", relationName);
        } else {
            if (StringUtils.hasText(classType)) {
                sql = String.format("MATCH p=(a:%s)-[r]->(b) RETURN p", classType);
            } else {
                ArrayList<NodeRelation> list = new ArrayList<>();
                for (String cl : classes) {
                    sql = String.format("MATCH p=(a:%s)-[r]->(b) where a.name='%s' RETURN p", cl, name);
                    List<NodeRelation> result = getResult(sql);
                    list.addAll(result);
                }
                return list;
            }
        }
        if (pageSize != null && pageSize != 0) {
            if (pageSize >= 300) pageSize = 300;
        } else {
            pageSize = 25;
        }
        sql = String.format(sql + " LIMIT %d", pageSize);
        return getResult(sql);
    }

    private List<NodeRelation> getResult(String sql) {
        session = driver.session();
        logger.info(sql);
        Result result = session.run(sql);
        List<Map<String, Object>> list = result.list(Record::asMap);
        List<NodeRelation> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Path path = (Path) map.get("p");
            Iterator<Value> start = path.start().values().iterator();
            Iterator<Value> end = path.end().values().iterator();
            String startId = path.start().id() + "";
            String endId = path.end().id() + "";
            Relationship relationship = path.relationships().iterator().next();
            String relationName;
            try {
                relationName = relationship.values().iterator().next().asString();
            } catch (Exception e) {
                relationName = relationship.type();
            }
            resultList.add(new NodeRelation(getEntity(startId, start.next().toString()), getEntity(endId, end.next().toString()), relationName));
        }
        return resultList;
    }
}
