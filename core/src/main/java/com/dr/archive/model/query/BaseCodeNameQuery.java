package com.dr.archive.model.query;

/**
 * 基础查询条件
 * 根据code、name查询
 *
 * @author caor
 * @date 2020/7/28 14:36
 */
public class BaseCodeNameQuery {
    private String code, name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
