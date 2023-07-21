package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-05-25 11:11
 **/
@Table(name = Constants.TABLE_PREFIX + "Manager_Atlas", module = Constants.MODULE_NAME, comment = "图册管理")
public class Atlas extends BaseStatusEntity<String> {
    @Column(comment = "名称")
    private String atlasName;
    @Column(comment = "类型")
    private String atlasType;
    @Column(comment = "主题")
    private String atlasTitle;
    @Column(comment = "方向")
    private String atlasDirection;
    @Column(comment = "描述")
    private String atlasMark;
    @Column(comment = "制作方式")
    private String atlasMake;
    @Column(comment = "关键词")
    private String atlasKeyWord;

    public String getAtlasKeyWord() {
        return atlasKeyWord;
    }

    public void setAtlasKeyWord(String atlasKeyWord) {
        this.atlasKeyWord = atlasKeyWord;
    }

    public String getAtlasName() {
        return atlasName;
    }

    public void setAtlasName(String atlasName) {
        this.atlasName = atlasName;
    }

    public String getAtlasType() {
        return atlasType;
    }

    public void setAtlasType(String atlasType) {
        this.atlasType = atlasType;
    }

    public String getAtlasTitle() {
        return atlasTitle;
    }

    public void setAtlasTitle(String atlasTitle) {
        this.atlasTitle = atlasTitle;
    }

    public String getAtlasDirection() {
        return atlasDirection;
    }

    public void setAtlasDirection(String atlasDirection) {
        this.atlasDirection = atlasDirection;
    }

    public String getAtlasMark() {
        return atlasMark;
    }

    public void setAtlasMark(String atlasMark) {
        this.atlasMark = atlasMark;
    }

    public String getAtlasMake() {
        return atlasMake;
    }

    public void setAtlasMake(String atlasMake) {
        this.atlasMake = atlasMake;
    }
}
