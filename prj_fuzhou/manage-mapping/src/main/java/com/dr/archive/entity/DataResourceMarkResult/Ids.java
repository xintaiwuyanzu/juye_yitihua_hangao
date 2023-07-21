package com.dr.archive.entity.DataResourceMarkResult;

import java.io.Serializable;

/**
 * @author: yang
 * @create: 2022-06-11 17:09
 **/
public class Ids implements Serializable {
    private String baseId;
    private String sourceId;
    private String targetId;

    public Ids() {
    }

    public Ids(String baseId, String sourceId, String targetId) {
        this.baseId = baseId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}