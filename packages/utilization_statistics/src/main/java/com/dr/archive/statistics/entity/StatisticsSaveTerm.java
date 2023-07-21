package com.dr.archive.statistics.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-06-28 9:36
 * @Description: todo 可能需要带门类分类
 */
@Table(name = Constants.TABLE_PREFIX + "STATISTICS_SAVE_TERM", module = Constants.MODULE_NAME, comment = "保管期限统计")
public class StatisticsSaveTerm {
    @Column(comment = "全宗id")
    private String fondId;
    @Column(comment = "全宗编码")
    private String fondCode;
    @Column(comment = "全宗名称")
    private String fondName;
    @Column(comment = "保管期限")
    private String SaveTerm;
    @Column(comment = "数量")
    private String num;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getSaveTerm() {
        return SaveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        SaveTerm = saveTerm;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
