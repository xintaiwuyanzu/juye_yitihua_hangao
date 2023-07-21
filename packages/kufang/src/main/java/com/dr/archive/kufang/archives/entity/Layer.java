package com.dr.archive.kufang.archives.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.common.entity.OrderEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * 密集架格子信息
 *
 * @author dr
 */

@Table(name = Constants.COMMON_TABLE_PREFIX + "LAYER", module = Constants.COMMON_MODULE_NAME,comment = "密集架格子")
public class Layer extends BaseStatusEntity<String> {

    @Column(comment = "库房id", length = 50)
    private String locId;
    @Column(name = "areaId", comment = "区域id", length = 50)
    private String areaId;
    @Column(name = "orgId", comment = "机构id", length = 50)
    private String orgId;
    @Column(comment = "密集架id", length = 50)
    private String caseId;

    @Column(comment = "A面或B面", length = 10)
    private String columnNum;
    @Column(comment = "列", length = 10)
    private String columnNo;
    @Column(comment = "层数", length = 10)
    private String layer;
    @Column(comment = "密集架格子具体信息", length = 100)
    private String remarks;

    @Column(comment = "Tid", length = 50)
    private String tid;

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(String columnNum) {
        this.columnNum = columnNum;
    }

    public String getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(String columnNo) {
        this.columnNo = columnNo;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Override
    public int compareTo(OrderEntity o) {
        if (o instanceof Layer){
            Layer l2= (Layer) o;
            int cn1 = Integer.parseInt(getColumnNo());
            int cn2 = Integer.parseInt(l2.getColumnNo());
            if (cn1 == cn2) {
                return Integer.parseInt(getLayer()) - Integer.parseInt(l2.getLayer());
            } else {
                return cn1 - cn2;
            }
        }
        return 0;
    }
}
