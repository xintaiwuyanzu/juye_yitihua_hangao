package com.dr.archive.statistics.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author Mr.Zhu
 * @date 2022/4/18 - 10:22
 */
@Table(name = Constants.TABLE_PREFIX + "RESOURECE_STATISTICS_TEMP", module = Constants.MODULE_NAME, comment = "档案资源统计临时表")
public class ResourceStatisticsTemp {
    @Column(comment = "数量")
    long fileArchiveNum;
    @Column(comment = "年度")
    String VINTAGES;
    @Column(comment = "全宗")
    String fondCode;
    @Column(comment = "分类")
    String categoryCode;

    public long getFileArchiveNum() {
        return fileArchiveNum;
    }

    public void setFileArchiveNum(long fileArchiveNum) {
        this.fileArchiveNum = fileArchiveNum;
    }

    public String getVINTAGES() {
        return VINTAGES;
    }

    public void setVINTAGES(String VINTAGES) {
        this.VINTAGES = VINTAGES;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
