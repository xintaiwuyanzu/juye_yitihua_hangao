package com.dr.archive.service.impl;

import com.dr.archive.entity.*;
import com.dr.archive.entity.DataResourceMarkResult.*;
import com.dr.archive.entity.task.Task;
import com.dr.archive.neo4j.utils.Neo4jUtils;
import com.dr.archive.service.*;
import com.dr.archive.util.JsonUtils;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.google.gson.Gson;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: yang
 * @create: 2022-06-10 17:32
 **/
@Service
public class DataResourceServiceImpl extends DefaultBaseService<DataResource> implements DataResourceService {
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    TripletDataService tripletDataService;
    @Autowired
    RelationService relationService;
    @Autowired
    DataResourceService dataResourceService;
    @Autowired
    TaskService taskService;
    @Autowired
    RealmClassService realmClassService;
    @Autowired
    Driver driver;

    final int PAGE_SIZE = 100;

    Ids ids = null;
    List<Condition> conditions = null;

    /**
     * 从表单定义梳理数据到对象库
     */
    @Override
    public long transform(String resultStr, Task task) {
        //梳理出的数据数量
        long n = 0;
        try {
            Assert.hasText(resultStr, "参数不能为空");
            Result result = JsonUtils.jsonToObject(resultStr, Result.class);
            String label = formDefinitionService.selectFormDefinitionById(result.getObjIds().getSourceId()).getFormCode();

            //查询梳理数据的时候有没有给对象的NAME赋值，因为图谱数据都是根据NAME名字查找数据，没有该数据就找不到
            List<TransData> properties = result.getProperties();
            boolean b = false;
            for (TransData property : properties) {
                if (property.getTo().equals("NAME")) {
                    b = true;
                }
            }
            if (b) {
                //从元数据表中查询出来所有数据
                List<FormData> formDataByFormId = getFormDataByFormId(resultStr, task);
                FormData formData = new FormData(result.getObjIds().getSourceId());
                //根据属性映射，逐条转换源对象
                for (FormData i : formDataByFormId) {
                    for (TransData j : properties) {
                        //元数据库有该字段而且有数据才放进表单数据
                        if (i.containsKey(j.getFrom()) && StringUtils.hasText(i.get(j.getFrom()))) {
                            //先查询对象库有没有该名字的数据
                            List<FormData> data = getNameFormData(result.getObjIds().getSourceId(), i.getString(j.getFrom()));
                            //如果不存在该名字的数据就放进去
                            if (data.size() == 0) {
                                formData.put(j.getTo(), i.get(j.getFrom()));
                                formData.put("id", System.currentTimeMillis() + new Random().nextLong());
                            }
                            //反之，这里先留个空，后面等数据有唯一标识字段再处理
                            else {
                                FormData d = data.get(0);
                            }
                        }
                    }
                    //存到对象库中
                    if (!StringUtils.hasText(formData.get("NAME"))) continue;
                    org.neo4j.driver.Result nodes = Neo4jUtils.saveNodes(driver.session(), formData.get("NAME"), label);
                    if (nodes != null) {
                        formData.put("STATUS", "2");
                        //插入数据到对象表中
                        formDataService.addFormData(formData);
                        n++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (n > 0) {
            task.setEndDate(System.currentTimeMillis());
            taskService.updateById(task);
        }
        return n;
    }

    /**
     * 保存关系梳理结果
     */
    @Override
    public long saveRelationResult(String resultStr, Task task) {
        long n = 0;//梳理的关系数量
        try {
            Assert.hasText(resultStr, "关系不能为空");
            Result result = JsonUtils.jsonToObject(resultStr, Result.class);
            Ids ids = result.getObjIds();
            InnerRelation relation = result.getRelation();
            String sourceLabel = formDefinitionService.selectFormDefinitionById(ids.getSourceId()).getFormCode();
            String targetLabel = formDefinitionService.selectFormDefinitionById(ids.getSourceId()).getFormCode();

            //从元数据表中查询出来所有数据
            List<FormData> formDataByFormId = getFormDataByFormId(resultStr, task);
            //关系数据梳理
            for (FormData i : formDataByFormId) {
                //根据源对象和目标对象的字段值去对象库中获取名字等于该值的那条数据（因为固定是通过名字查找图谱数据的，无论是对象数据还是关系数据）
                String source = i.get(relation.getSource());
                String target = i.get(relation.getTarget());
                List<FormData> sourceData = getNameFormData(ids.getSourceId(), source);
                List<FormData> targetData = getNameFormData(ids.getSourceId(), target);
                //如果两条数据都存在对象库，才可以进行梳理关系
                if (sourceData.size() > 0 && targetData.size() > 0) {
                    RealmClass realmClass_s = realmClassService.selectOne(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.FORMID, ids.getSourceId()));
                    RealmClass realmClass_t = realmClassService.selectOne(SqlQuery.from(RealmClass.class).equal(RealmClassInfo.FORMID, ids.getTargetId()));
                    TripletData tripletData = tripletDataService.selectOne(SqlQuery.from(TripletData.class)
                            .equal(TripletDataInfo.SOURCEID, sourceData.get(0).getId())
                            .equal(TripletDataInfo.TARGETID, targetData.get(0).getId())
                            .equal(TripletDataInfo.RELATIONID, relation.getRelation()));
                    if (tripletData == null) {
                        TripletData data = new TripletData(ids.getBaseId(), realmClass_s.getFormId(), realmClass_s.getFormAlias(), sourceData.get(0).getId(), i.get(relation.getSource()), realmClass_t.getFormId(), realmClass_t.getFormAlias(), targetData.get(0).getId(), i.get(relation.getTarget()), relation.getRelation(), "", relation.getSource(), relation.getTarget());
                        Relation relationById = relationService.selectById(relation.getRelation());
                        data.setRelationName(relationById.getRelationName());//关系名称
                        if (StringUtils.hasText(data.getSourceName()) && StringUtils.hasText(data.getTargetName())) {
                            //存到关系库中
                            org.neo4j.driver.Result nodes = Neo4jUtils.saveRelations(driver.session(), sourceLabel, targetLabel, source, target, relationById.getRelationName());
                            if (nodes != null) {
                                data.setStatus("1");
                                //插入数据到关系表中
                                tripletDataService.insert(data);
                                n++;
                            }
                        }
                    }
                }
            }
            if (n > 0) {
                //标注对象序号
                markRelationClass(ids.getBaseId(), relation);
                task.setEndDate(System.currentTimeMillis());
                taskService.updateById(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * 源对象和目标对象位置跟踪，前端显示
     */
    private void markRelationClass(String id, InnerRelation relation) {
        String source = relation.getSource();
        String target = relation.getTarget();
        DataResource dataResource = selectOne(SqlQuery.from(DataResource.class).equal(DataResourceInfo.FORMID, id));
        String markResult = dataResource.getMarkResult();
        Map<String, Integer> map = new HashMap<>();
        Gson gson = new Gson();
        try {
            if (StringUtils.hasText(markResult)) {
                map = gson.fromJson(markResult, Map.class);
                //获取map的value最大值
                Object[] array = map.values().toArray();
                Arrays.sort(array);
                int max = Double.valueOf(array[array.length - 1].toString()).intValue() + 1;
                if (!map.containsKey(source)) {
                    map.put(source, max);
                    max++;
                }
                if (!map.containsKey(target)) {
                    map.put(target, max);
                }
            } else {
                map.put(source, 1);
                map.put(target, 2);
            }
            dataResource.setMarkResult(gson.toJson(map));
            updateById(dataResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据标注任务
     */
    @Override
    public long transformPre(String result) {
        long count = formDataService.countId(getBaseId(result), getSqlBuilder(result, 0));
        if (count == 0) return 0;
        //保存定时任务
        Task task = new Task(getBaseId(result), "dataMark", result, 0, PAGE_SIZE, count, 0, 1);
        task.setStatus("0");
        taskService.insert(task);
        //100条数据以下直接执行
        if (count < 100) {
            return transform(result, task);
        } else {
            return -1;
        }
    }

    /**
     * 关系梳理任务
     */
    @Override
    public long saveRelationResultPre(String result) {
        long count = formDataService.countId(getBaseId(result), getSqlBuilder(result, 0));
        if (count == 0) return 0;
        Task task = new Task(getBaseId(result), "relationMark", result, 0, PAGE_SIZE, count, 0, 1);
        task.setStatus("0");
        taskService.insert(task);
        //100条数据以下直接执行
        if (count < 100) {
            return saveRelationResult(result, task);
        } else {
            return -1;
        }
    }

    /**
     * 通过表单id和表单条件查询数据
     */
    private List<FormData> getFormDataByFormId(String resultStr, Task task) {
        if (task.getEndDate() == 0) {
            return formDataService.selectPageFormData(getBaseId(resultStr), getSqlBuilder(resultStr, task.getEndDate()), task.getPageIndex(), (int) task.getPageSize()).getData();
        } else {
            return formDataService.selectFormData(getBaseId(resultStr), getSqlBuilder(resultStr, task.getEndDate()));
        }
    }

    //获取元数据表单id
    private String getBaseId(String result) {
        String baseId = null;
        try {
            Result object = JsonUtils.jsonToObject(result, Result.class);
            baseId = object.getObjIds().getBaseId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseId;
    }

    //根据条件获取查询语句
    private SqlBuilder getSqlBuilder(String resultStr, long endDate) {
        try {
            Result result = JsonUtils.jsonToObject(resultStr, Result.class);
            ids = result.getObjIds();
            conditions = result.getCondition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SqlBuilder sqlBuilder = (sqlQuery, wrapper) -> {
            //endDate=0表示任务第一次执行，反之表示有新数据添加进来，直接找新添加的数据
            if (endDate != 0) {
                sqlQuery.greaterThan(wrapper.getColumn("createDate"), endDate);
            }
            conditions.forEach(i -> {
                if (StringUtils.hasText(i.getValue())) {
                    String iWhen = i.getWhen();
                    switch (iWhen) {
                        case "=":
                            sqlQuery.equal(wrapper.getColumn(i.getName()), i.getValue());
                            break;
                        case "<":
                            sqlQuery.lessThan(wrapper.getColumn(i.getName()), i.getValue());
                            break;
                        case ">":
                            sqlQuery.greaterThan(wrapper.getColumn(i.getName()), i.getValue());
                            break;
                        case "like":
                            sqlQuery.like(wrapper.getColumn(i.getName()), i.getValue());
                            break;
                    }
                }
            });
        };
        return sqlBuilder;
    }

    //通过名字查询数据
    public List<FormData> getNameFormData(String formId, String value) {
        return formDataService.selectFormData(formId, (sqlQuery, wrapper) -> sqlQuery.equal(wrapper.getColumn("NAME"), value));
    }
}
