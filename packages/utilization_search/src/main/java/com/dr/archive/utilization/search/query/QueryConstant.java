package com.dr.archive.utilization.search.query;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/7/23 15:40
 */
public class QueryConstant {
    /**
     * 查询类型
     *      gte：大于等于
     *      gt：大于
     *      lte：小于等于
     *      lt：小于
     *      equals：等于
     *      not：不等于
     *
     */
    public static final String LOGIC_GTE = ">=";
    public static final String LOGIC_GT = ">";
    public static final String LOGIC_LTE = "<=";
    public static final String LOGIC_LT = "<";
    public static final String LOGIC_EQUALS = "=";
    public static final String LOGIC_LIKE = "%";

    /**
     * 关系连接逻辑类型
     *      like：或
     *      must：与
     *      not：非
     *
     */
    public static final String SEARCH_MUST = "must";
    public static final String SEARCH_LIKE = "like";
    public static final String SEARCH_NOT = "not";
}
