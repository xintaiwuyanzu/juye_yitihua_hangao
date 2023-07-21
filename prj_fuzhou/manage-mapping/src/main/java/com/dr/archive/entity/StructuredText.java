package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-06-03 11:08
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_StructuredText", module = Constants.MODULE_NAME, comment = "结构化原文")
public class StructuredText extends BaseStatusEntity<String> {
    @Column(comment = "标题")
    private String title;
    @Column(comment = "档号")
    private String documentNo;
    @Column(comment = "文件Id")
    private String fileId;
    @Column(comment = "文件路径")
    private String filePath;
    @Column(comment = "档案门类")
    private String categoryName;
    @Column(comment = "数据数量")
    private String dataNum;
    @Column(comment = "开始年份")
    private String startYear;
    @Column(comment = "结束年份")
    private String endYear;
    @Column(comment = "对象标记结果")
    private String markResult;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getMarkResult() {
        return markResult;
    }

    public void setMarkResult(String markResult) {
        this.markResult = markResult;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
