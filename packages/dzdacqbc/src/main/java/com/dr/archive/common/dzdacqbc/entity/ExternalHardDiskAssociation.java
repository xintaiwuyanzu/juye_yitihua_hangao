package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 外部硬盘和档案的关联表
 */
@Table(name = Constants.DZDNCQBC + "hardDisk_Archive", module = Constants.MODULE_NAME, comment = "外部硬盘和档案关联表")
public class ExternalHardDiskAssociation extends BaseStatusEntity<String> {

    //档案id、分类Id、分组名称、最后备份时间、最后更新时间
    @Column(comment = "档案id")
    private String archiveId;
    @Column(comment = "分类Id")
    private String classifiId;
    //现在没用
    @Column(comment = "硬盘id")
    private String hardDiskId;
    @Column(comment = "分组名称")
    private String hardDiskGrouping;
    @Column(comment = "最后备份时间")
    private long lastBackupsDate;
    //一份档案的大小
    @Column(comment = "备份档案大小")
    private long backupsArchiveNum;
    @Column(comment = "最后更新时间")
    private long lastUpdateDate;

    public long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getClassifiId() {
        return classifiId;
    }

    public void setClassifiId(String classifiId) {
        this.classifiId = classifiId;
    }

    public String getHardDiskId() {
        return hardDiskId;
    }

    public void setHardDiskId(String hardDiskId) {
        this.hardDiskId = hardDiskId;
    }

    public String getHardDiskGrouping() {
        return hardDiskGrouping;
    }

    public void setHardDiskGrouping(String hardDiskGrouping) {
        this.hardDiskGrouping = hardDiskGrouping;
    }

    public long getLastBackupsDate() {
        return lastBackupsDate;
    }

    public void setLastBackupsDate(long lastBackupsDate) {
        this.lastBackupsDate = lastBackupsDate;
    }

    public long getBackupsArchiveNum() {
        return backupsArchiveNum;
    }

    public void setBackupsArchiveNum(long backupsArchiveNum) {
        this.backupsArchiveNum = backupsArchiveNum;
    }
}
