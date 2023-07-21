package com.dr.archive.entity.DataResourceMarkResult;

import java.io.Serializable;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-06-11 16:03
 **/
public class Result implements Serializable {
    private InnerRelation relation;
    private List<TransData> properties;
    private List<Condition> condition;
    private Ids objIds;

    public Result() {
    }

    public Result(InnerRelation relation, List<TransData> properties, List<Condition> condition, Ids objIds) {
        this.relation = relation;
        this.properties = properties;
        this.condition = condition;
        this.objIds = objIds;
    }

    public List<TransData> getProperties() {
        return properties;
    }

    public void setProperties(List<TransData> properties) {
        this.properties = properties;
    }

    public Ids getObjIds() {
        return objIds;
    }

    public void setObjIds(Ids objIds) {
        this.objIds = objIds;
    }

    public InnerRelation getRelation() {
        return relation;
    }

    public void setRelation(InnerRelation relation) {
        this.relation = relation;
    }

    public List<Condition> getCondition() {
        return condition;
    }

    public void setCondition(List<Condition> condition) {
        this.condition = condition;
    }

}



