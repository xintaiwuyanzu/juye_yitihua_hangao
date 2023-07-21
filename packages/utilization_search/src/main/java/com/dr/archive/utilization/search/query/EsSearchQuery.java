package com.dr.archive.utilization.search.query;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/7/23 11:05
 */
public class EsSearchQuery {
    /**
     * 查询字段名
     */
    private String fieldName;
    /**
     * 查询类型{‘like’：模糊查询，‘must’：精确查询}
     */
    private String searchType;
    /**
     * 查询内容
     */
    private String keyword;
    /**
     * 逻辑类型：0：or，1：and，2：not
     */
    private String logicType;

    public EsSearchQuery(String fieldName, String searchType, String keyword, String logicType) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.keyword = keyword;
        this.logicType = logicType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLogicType() {
        return logicType;
    }

    public void setLogicType(String logicType) {
        this.logicType = logicType;
    }
}
