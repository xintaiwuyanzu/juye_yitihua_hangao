package com.dr.archive.neo4j.vo;

/**
 * @author: yang
 * @create: 2022-07-07 18:53
 **/
public class CommonEntity {
    String id;
    String name;

    public CommonEntity() {
    }

    public CommonEntity(String id, String name) {
        this.id = id;
        this.name = name;
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
}
