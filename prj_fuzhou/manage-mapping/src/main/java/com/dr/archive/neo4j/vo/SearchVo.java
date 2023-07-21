package com.dr.archive.neo4j.vo;

/**
 * @author: yang
 * @create: 2022-07-07 17:44
 **/
public class SearchVo {
    String id;
    String name;
    String relationName;
    String classType;
    Integer type;
    Integer pageSize;

    public SearchVo() {
    }

    public SearchVo(String id, String name, String relationName, String classType, Integer type, Integer pageSize) {
        this.id = id;
        this.name = name;
        this.relationName = relationName;
        this.classType = classType;
        this.type = type;
        this.pageSize = pageSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
