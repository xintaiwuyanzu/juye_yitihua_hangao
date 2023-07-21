package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 档案类别
 * 包含关系：档案类别--》档案属性--》属性值
 *
 * @author cuiyj
 * @Date 2021-09-02
 */
@Table(name = Constants.TABLE_PREFIX + "ArchiveType", module = Constants.MODULE_NAME, comment = "类别")
public class ArchiveType extends BaseStatusEntity<String> {

    public static final String TYPE_根类别 = "root";
    public static final String TYPE_案卷 = "aj";
    public static final String TYPE_文件 = "wj";
    public static final String TYPE_卷内文件 = "jn";


    @Column(comment = "档案类别编号", length = 200)
    private String archiveTypeCode;

    @Column(comment = "档案类别", length = 200)
    private String archiveTypeName;

    @Column(comment = "机构id", length = 50)
    private String organiseId;

    //档案分类下的案卷和文件分类
    @Column(comment = "父id", length = 200)
    private String pid;

    @Column(comment = "类型", length = 200)
    private String aType;

    @Column(comment = "全宗号", length = 200)
    private String fondId;
    @Column(comment = "全宗名称", length = 200)
    private String fondName;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getArchiveTypeCode() {
        return archiveTypeCode;
    }

    public void setArchiveTypeCode(String archiveTypeCode) {
        this.archiveTypeCode = archiveTypeCode;
    }

    public String getArchiveTypeName() {
        return archiveTypeName;
    }

    public void setArchiveTypeName(String archiveTypeName) {
        this.archiveTypeName = archiveTypeName;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }
}
