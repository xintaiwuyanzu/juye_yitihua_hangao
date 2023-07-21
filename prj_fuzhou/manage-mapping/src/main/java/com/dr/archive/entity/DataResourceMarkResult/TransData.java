package com.dr.archive.entity.DataResourceMarkResult;

import java.io.Serializable;

/**
 * @author: yang
 * @create: 2022-06-11 17:09
 **/
public class TransData implements Serializable {
    private String from;
    private String to;

    public TransData() {
    }

    public TransData(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
