package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 *  外部硬盘表，用来备份数据
 */
@Table(name = Constants.DZDNCQBC + "hardDisk", module = Constants.MODULE_NAME, comment = "外部硬盘表")
public class ExternalHardDisk extends BaseStatusEntity<String> {

    //名称、唯一标识、存储大小、分类、最后备份时间、备份档案数量、分组
    @Column(comment = "名称")
    private String diskName;
    @Column(comment = "硬盘唯一标识")
    private String uniqueIdentification;
    /*
        GB
     */
    @Column(comment = "存储大小")
    private long storageSize;
    @Column(comment = "分类ID")
    private String classifiId;
    /*
        根据分类分组，一块硬盘可能不够存一个分类的文件,可能需要多块硬盘
     */
    @Column(comment = "硬盘分组")
    private String hardDiskGrouping;
    @Column(comment = "最后备份时间")
    private long lastBackupsDate;
    @Column(comment = "应备份档案数量")
    private long backupsArchiveNum;
    @Column(comment = "已备份档案数量")
    private long alreadyBackupsArchiveNum;

    public long getAlreadyBackupsArchiveNum() {
        return alreadyBackupsArchiveNum;
    }

    public void setAlreadyBackupsArchiveNum(long alreadyBackupsArchiveNum) {
        this.alreadyBackupsArchiveNum = alreadyBackupsArchiveNum;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getUniqueIdentification() {
        return uniqueIdentification;
    }

    public void setUniqueIdentification(String uniqueIdentification) {
        this.uniqueIdentification = uniqueIdentification;
    }

    public long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(long storageSize) {
        this.storageSize = storageSize;
    }

    public String getClassifiId() {
        return classifiId;
    }

    public void setClassifiId(String classifiId) {
        this.classifiId = classifiId;
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
