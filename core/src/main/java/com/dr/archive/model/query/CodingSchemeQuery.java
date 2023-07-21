package com.dr.archive.model.query;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/17 15:45
 */
public class CodingSchemeQuery extends BaseConfigQuery {
    private String id;
    private String schemeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }
}
