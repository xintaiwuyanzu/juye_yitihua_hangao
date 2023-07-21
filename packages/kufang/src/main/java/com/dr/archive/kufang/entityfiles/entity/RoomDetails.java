package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 库房详情表
 */

@Table(name = Constants.TABLE_PREFIX + "RoomDetails", module = Constants.MODULE_NAME, comment = "库房详情")
public class RoomDetails extends BaseCreateInfoEntity {

    @Column(comment = "库房ID",length = 100)
    public String kufangId;

    @Column(comment = "档案类型Name",length = 50)
    public String archivesName;

    @Column(comment = "档案类型Id", length = 100)
    public String archivesType;

    @Column(comment = "数量",length = 100)
    public String archivesNum;

    public String getArchivesName() {
        return archivesName;
    }

    public void setArchivesName(String archivesName) {
        this.archivesName = archivesName;
    }

    public String getKufangId() {
        return kufangId;
    }

    public void setKufangId(String kufangId) {
        this.kufangId = kufangId;
    }

    public String getArchivesType() {
        return archivesType;
    }

    public void setArchivesType(String archivesType) {
        this.archivesType = archivesType;
    }

    public String getArchivesNum() {
        return archivesNum;
    }

    public void setArchivesNum(String archivesNum) {
        this.archivesNum = archivesNum;
    }
}
