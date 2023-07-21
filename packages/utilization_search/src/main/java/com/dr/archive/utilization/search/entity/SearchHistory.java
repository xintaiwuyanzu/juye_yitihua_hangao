package com.dr.archive.utilization.search.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: lirs
 * @date: 2021/12/23 11:29
 */
@Table(name = Constants.TABLE_PREFIX + "SEARCH_HISTORY", comment = "搜索历史关键字", module = Constants.MODULE_NAME)
public class SearchHistory extends BaseStatusEntity<String> {

    @Column(name = "keyWords", comment = "搜索关键字", length = 1500)
    private String keyWords;
    @Column(name = "secondary", comment = "是否 根据门类筛选")
    private boolean secondary;
    @Column(name = "operato", comment = "检索关系,AND表示 并且,OR表示 或者")
    private String operato;
    @Column(name = "conditions", comment = "全文搜索关键字")
    private String conditions;
    @Column(name = "multiSearch", comment = "是否高级搜索")
    private boolean multiSearch;
    @Column(name = "categoryName", comment = "门类名称")
    private String categoryName;
    @Column(name = "openScope")
    private boolean openScope;
    @Column(name = "category")
    private String category;
    //考虑到二次检索,将所有的查询条件都放到querysListJson中
    @Column(name = "querysListJson", length = 2000)
    private String querysListJson;
    @Column(name = "secondRetrieval", comment = "是否二次检索")
    private boolean secondRetrieval;
    @Column(name = "orgId", comment = "机构id")
    private String orgId;
    @Column(name = "orgName", comment = "机构名称")
    private String orgName;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isOpenScope() {
        return openScope;
    }

    public void setOpenScope(boolean openScope) {
        this.openScope = openScope;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(boolean multiSearch) {
        this.multiSearch = multiSearch;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getOperato() {
        return operato;
    }

    public void setOperato(String operato) {
        this.operato = operato;
    }

    public boolean isSecondary() {
        return secondary;
    }

    public void setSecondary(boolean secondary) {
        this.secondary = secondary;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
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
}
