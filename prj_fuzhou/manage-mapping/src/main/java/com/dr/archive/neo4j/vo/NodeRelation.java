package com.dr.archive.neo4j.vo;


/**
 * @author: yang
 * @create: 2022-07-06 10:27
 **/
public class NodeRelation {

    Object start;
    Object end;
    String relation;

    public NodeRelation() {
    }

    public NodeRelation(Object start, Object end, String relation) {
        this.start = start;
        this.end = end;
        this.relation = relation;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
