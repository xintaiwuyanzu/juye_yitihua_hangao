package com.dr.archive.model.query;

/**
 * 根据年度和类型查询字典
 *
 * @author dr
 */
public class DictQuery extends BaseYearQuery {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
