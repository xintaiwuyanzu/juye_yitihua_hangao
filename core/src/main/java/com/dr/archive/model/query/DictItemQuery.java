package com.dr.archive.model.query;

/**
 * 根据年度，类型，编码查询字典
 *
 * @author dr
 */
public class DictItemQuery extends DictQuery {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
