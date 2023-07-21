package com.dr.archive.tag.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * @date: 2022/3/30 9:39
 * 知识图谱 主题表
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "KNOWLEDGE_GRAPH", module = Constants.MODULE_NAME, comment = "知识图谱主题表")
public class KnowledgeGraph extends BaseStatusEntity<String> {
    @Column(comment = "主题")
    private String themeName;
    @Column(comment = "备注")
    private String remark;
    @Column(comment = "关键词名")
    private String keywordsName;
    @Column(comment = "关键词")
    private String keywords;
    @Column(comment = "图谱数据", type = ColumnType.CLOB)
    private String graphData;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getKeywordsName() {
        return keywordsName;
    }

    public void setKeywordsName(String keywordsName) {
        this.keywordsName = keywordsName;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getGraphData() {
        return graphData;
    }

    public void setGraphData(String graphData) {
        this.graphData = graphData;
    }
}
