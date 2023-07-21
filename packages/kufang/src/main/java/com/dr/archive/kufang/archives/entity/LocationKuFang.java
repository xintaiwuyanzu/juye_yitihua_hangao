package com.dr.archive.kufang.archives.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * 档案室
 *
 * @author dr
 */

@Table(name = Constants.COMMON_TABLE_PREFIX + "LOCATIONKUFANG", module = Constants.COMMON_MODULE_NAME, comment = "档案室")
public class LocationKuFang extends BaseStatusEntity<String> {

    //  档案室类型 普通
    public static final String LOCTYPE_ORDINARY = "Ordinary_Room";
    //  档案室类型  机密
    public static final String LOCTYPE_SECRET = "Secret_Room";

    @Column(name = "locName", comment = "档案室名称", length = 50)
    private String name;
    /**
     * 馆藏室编码加上图书馆id，应该是全局唯一的，这个唯一性检查通过代码检查吧
     */
    @Column(comment = "档案室类型", length = 30)
    private String locType;
    @Column(comment = "位置描述", length = 500)
    private String description;
    @Column(comment = "档案室楼层", length = 500)
    private String locFloor;
    @Column(comment = "档案室面积", length = 50)
    private String locArea;
    @Column(comment = "机构id", length = 50)
    private String organiseId;

    public String getLocArea() {
        return locArea;
    }

    public void setLocArea(String locArea) {
        this.locArea = locArea;
    }

    public String getLocFloor() {
        return locFloor;
    }

    public void setLocFloor(String locFloor) {
        this.locFloor = locFloor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }
}
