package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存分类信息表实体类
 *
 * @author hyj
 */
@Table(name = Constants.DZDNCQBC + "classification", module = Constants.MODULE_NAME, comment = "电子档案长期保存分类信息表")
public class CqbcClassification extends BaseStatusEntity<String> {

    @Column(comment = "名称", length = 200)
    private String classificationName;

    @Column(comment = "全宗id", length = 200)
    private String fondId;

    @Column(comment = "全宗号", length = 200)
    private String fondNo;

    @Column(comment = "全宗名称", length = 200)
    private String fondName;

    @Column(comment = "门类id", length = 200)
    private String classId;

    @Column(comment = "门类代码", length = 200)
    private String classCode;

    @Column(comment = "门类名称", length = 200)
    private String className;

    @Column(comment = "年度", length = 200)
    private String annual;

    @Column(comment = "策略Id", length = 200)
    private String tacticsId;

    @Column(comment = "存储信息id", length = 200)
    private String spacesId;

    @Column(comment = "是否默认", length = 200)
    private String isDefault;

    //目前没啥用直接统计
    @Column(comment = "异常数量",length = 200)
    private long problemNum;

    @Column(comment = "最后一次检测时间",length = 100,type = ColumnType.DATE)
    private long lastTestTime;

    public long getLastTestTime() {
        return lastTestTime;
    }

    public void setLastTestTime(long lastTestTime) {
        this.lastTestTime = lastTestTime;
    }

    public long getProblemNum() {
        return problemNum;
    }

    public void setProblemNum(long problemNum) {
        this.problemNum = problemNum;
    }

    public String getSpacesId() {
        return spacesId;
    }

    public void setSpacesId(String spacesId) {
        this.spacesId = spacesId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getFondNo() {
        return fondNo;
    }

    public void setFondNo(String fondNo) {
        this.fondNo = fondNo;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAnnual() {
        return annual;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public String getTacticsId() {
        return tacticsId;
    }

    public void setTacticsId(String tacticsId) {
        this.tacticsId = tacticsId;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
