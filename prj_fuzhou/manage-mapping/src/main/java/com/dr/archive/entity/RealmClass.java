package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-05-30 09:20
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_Realm_Class", module = Constants.MODULE_NAME, comment = "领域对象")
public class RealmClass extends BaseStatusEntity<String> {
    @Column(comment = "领域id", type = ColumnType.CLOB)
    private String realmId;
    @Column(comment = "对象表单id")
    private String formId;
    @Column(comment = "对象表名")
    private String formTable;
    @Column(comment = "对象表类型")
    private String formType;
    @Column(comment = "对象表别名")
    private String formAlias;
    @Column(comment = "领域节点", type = ColumnType.CLOB)
    private String realmNode;
    @Column(comment = "属性数量")
    private int propertyNum;
    @Column(comment = "关系数量")
    private int relationNum;
    @Column(comment = "数据数量")
    private int dataNum;

    public RealmClass() {
    }

    public RealmClass(String realmId, String formId, String formTable, String formType, String formAlias, String realmNode, int propertyNum, int relationNum) {
        this.realmId = realmId;
        this.formId = formId;
        this.formTable = formTable;
        this.formType = formType;
        this.formAlias = formAlias;
        this.realmNode = realmNode;
        this.propertyNum = propertyNum;
        this.relationNum = relationNum;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormAlias() {
        return formAlias;
    }

    public void setFormAlias(String formAlias) {
        this.formAlias = formAlias;
    }

    public String getRealmNode() {
        return realmNode;
    }

    public void setRealmNode(String realmNode) {
        this.realmNode = realmNode;
    }

    public int getPropertyNum() {
        return propertyNum;
    }

    public void setPropertyNum(int propertyNum) {
        this.propertyNum = propertyNum;
    }

    public int getRelationNum() {
        return relationNum;
    }

    public void setRelationNum(int relationNum) {
        this.relationNum = relationNum;
    }

    public String getFormTable() {
        return formTable;
    }

    public void setFormTable(String formTable) {
        this.formTable = formTable;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }
}
