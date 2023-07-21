package com.dr.archive.entity.DataResourceMarkResult;

import java.io.Serializable;

/**
 * @author: yang
 * @create: 2022-06-11 17:08
 **/
public class InnerRelation implements Serializable {
    private String source;
    private String target;
    private String relation;

    public InnerRelation() {
    }

    public InnerRelation(String source, String target, String relation) {
        this.source = source;
        this.target = target;
        this.relation = relation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
