package com.dr.archive.manage.fond.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 全宗变更历史
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "FONDCHANGEHISTORY", module = Constants.MODULE_NAME, comment = "全宗变更历史")
public class FondChangeHistory extends BaseStatusEntity<String> {

    @Column(comment = "全宗号", length = 50)
    private String fondId;

    @Column(comment = "新全宗号", length = 50)
    private String newFondId;

    @Column(comment = "全宗名称", length = 50)
    private String fondName;

    @Column(comment = "新全宗名称", length = 50)
    private String newFondName;

    @Column(comment = "新排序", length = 50)
    private Integer newOrder;

    @Column(comment = "创建日期", type = ColumnType.DATE)
    private Long newCreateDate;
    @Column(comment = "创建人ID", simple = "创建人", length = 100)
    private String newCreatePerson;


    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getNewFondId() {
        return newFondId;
    }

    public void setNewFondId(String newFondId) {
        this.newFondId = newFondId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getNewFondName() {
        return newFondName;
    }

    public void setNewFondName(String newFondName) {
        this.newFondName = newFondName;
    }

    public Integer getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Integer newOrder) {
        this.newOrder = newOrder;
    }

    public Long getNewCreateDate() {
        return newCreateDate;
    }

    public void setNewCreateDate(Long newCreateDate) {
        this.newCreateDate = newCreateDate;
    }

    public String getNewCreatePerson() {
        return newCreatePerson;
    }

    public void setNewCreatePerson(String newCreatePerson) {
        this.newCreatePerson = newCreatePerson;
    }
}
