package com.dr.archive.statistics.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author Mr.Zhu
 * @date 2022/4/18 - 14:17
 */
@Table(name = Constants.TABLE_PREFIX + "STATISTICAL_REPORT", module = Constants.MODULE_NAME, comment = "统计报表查询")
public class StatisticalReport extends BaseStatusEntity<String> {
    @Column(comment = "全宗id")
    private String fondId;
    @Column(comment = "全宗名字")
    private String fondName;
    @Column(comment = "分类id")
    private String typeId;
    @Column(comment = "分类名字")
    private String typeName;
    @Column(comment = "开始年度")
    private String beginYear;
    @Column(comment = "结束年度")
    private String endYear;
    @Column(comment = "所在库")
    private String locationLibrary;
    @Column(comment = "整理方式")
    private String arrMethod;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBeginYear() {
        return beginYear;
    }

    public void setBeginYear(String beginYear) {
        this.beginYear = beginYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getLocationLibrary() {
        return locationLibrary;
    }

    public void setLocationLibrary(String locationLibrary) {
        this.locationLibrary = locationLibrary;
    }

    public String getArrMethod() {
        return arrMethod;
    }

    public void setArrMethod(String arrMethod) {
        this.arrMethod = arrMethod;
    }
}
