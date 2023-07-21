package com.dr.archive.entity.DataResourceMarkResult;

import java.io.Serializable;

/**
 * @author: yang
 * @create: 2022-06-11 17:09
 **/
public class Condition implements Serializable {
    private String name;
    private String when;
    private String value;

    public Condition() {
    }

    public Condition(String name, String when, String value) {
        this.name = name;
        this.when = when;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
