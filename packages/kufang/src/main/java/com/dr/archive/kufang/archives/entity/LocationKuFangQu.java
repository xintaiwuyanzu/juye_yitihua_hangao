package com.dr.archive.kufang.archives.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * 库房的分区
 *
 * @author dr
 */

@Table(name = Constants.COMMON_TABLE_PREFIX + "LOCATIONKUFANGQU", module = Constants.COMMON_MODULE_NAME, comment = "库房的分区")
public class LocationKuFangQu extends BaseStatusEntity<String> {

    @Column(name = "kufangId", comment = "库房id", length = 200)
    private String kufangId;
    @Column(comment = "区域名称", length = 200)
    private String areaName;
    @Column(comment = "区号", length = 200)
    private String areaCode;
    @Column(comment = "机构id", length = 50)
    private String orgId;

    public String getKufangId() {
        return kufangId;
    }

    public void setKufangId(String kufangId) {
        this.kufangId = kufangId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
