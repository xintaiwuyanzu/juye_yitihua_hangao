package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 */
@Table(name = Constants.TABLE_PREFIX + "SMART_SEARCH_HISTORY", comment = "智能化检索记录", module = Constants.MODULE_NAME)
public class SmartSearchHistory extends BaseStatusEntity<String> {
    @Column(name = "classType", comment = "对象类型")
    private String classType;
    @Column(name = "content", comment = "检索内容")
    private String content;
    @Column(name = "jsonStr", comment = "筛选条件") //暂时没用到
    private String jsonStr;
    @Column(name = "searchNum", comment = "检索次数")
    private int searchNum;
    @Column(name = "lastSearchDate", type = ColumnType.DATE, comment = "最后检索时间")
    private long lastSearchDate;

    public SmartSearchHistory() {
    }

    public SmartSearchHistory(String classType, String content, String jsonStr) {
        this.classType = classType;
        this.content = content;
        this.jsonStr = jsonStr;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public int getSearchNum() {
        return searchNum;
    }

    public void setSearchNum(int searchNum) {
        this.searchNum = searchNum;
    }

    public long getLastSearchDate() {
        return lastSearchDate;
    }

    public void setLastSearchDate(long lastSearchDate) {
        this.lastSearchDate = lastSearchDate;
    }

}
