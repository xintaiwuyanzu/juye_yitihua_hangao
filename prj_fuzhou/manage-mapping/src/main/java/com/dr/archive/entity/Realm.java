package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-05-27 14:32
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_Realm", module = Constants.MODULE_NAME, comment = "领域管理")
public class Realm extends BaseStatusEntity<String> {
    @Column(comment = "领域名称")
    private String realmName;
    @Column(comment = "领域地区")
    private String realmArea;
    @Column(comment = "领域描述")
    private String realmMark;
    @Column(comment = "领域类型")
    private String realmType;
    @Column(comment = "领域节点")
    private String realmNodes;

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public String getRealmArea() {
        return realmArea;
    }

    public void setRealmArea(String realmArea) {
        this.realmArea = realmArea;
    }

    public String getRealmMark() {
        return realmMark;
    }

    public void setRealmMark(String realmMark) {
        this.realmMark = realmMark;
    }

    public String getRealmType() {
        return realmType;
    }

    public void setRealmType(String realmType) {
        this.realmType = realmType;
    }

    public String getRealmNodes() {
        return realmNodes;
    }

    public void setRealmNodes(String realmNodes) {
        this.realmNodes = realmNodes;
    }
}
