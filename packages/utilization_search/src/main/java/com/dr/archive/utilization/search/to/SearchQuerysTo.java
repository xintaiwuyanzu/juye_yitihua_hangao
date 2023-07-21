package com.dr.archive.utilization.search.to;

import java.util.List;

/**
 * 档案检索 检索条件
 * @author: qiuyf
 * @date: 2022/2/11 10:59
 */
public class SearchQuerysTo {
    //是否二次检索
    private boolean secondRetrieval;
    //分类查询 分类名
    private String categoryName;
    //标签名
    private String tagName;
    //开放范围 true时 只查公开的数据
    private boolean openScope;

    private String category;
    private String fondCode;
    //具体查询条件
    private List<Querys> querysList;

    private String querysListJson;

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getQuerysListJson() {
        return querysListJson;
    }

    public void setQuerysListJson(String querysListJson) {
        this.querysListJson = querysListJson;
    }

    public boolean isSecondRetrieval() {
        return secondRetrieval;
    }

    public void setSecondRetrieval(boolean secondRetrieval) {
        this.secondRetrieval = secondRetrieval;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isOpenScope() {
        return openScope;
    }

    public void setOpenScope(boolean openScope) {
        this.openScope = openScope;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Querys> getQuerysList() {
        return querysList;
    }

    public void setQuerysList(List<Querys> querysList) {
        this.querysList = querysList;
    }
}
