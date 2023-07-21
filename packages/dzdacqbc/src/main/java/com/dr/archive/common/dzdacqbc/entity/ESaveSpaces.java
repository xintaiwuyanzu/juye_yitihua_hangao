package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存存储空间信息表实体类
 *
 * @author hyj
 */
@Table(name = Constants.DZDNCQBC + "spaces", module = Constants.MODULE_NAME, comment = "电子档案长期保存存储空间信息表")
public class ESaveSpaces extends BaseStatusEntity<String> {

    @Column(comment = "存储空间名称", length = 200)
    private String spaceName;

    @Column(comment = "挂载目录", length = 200)
    private String catalogue;

    @Column(comment = "备份目录", length = 200)
    private String backPath;

    @Column(comment = "容量", length = 200)
    private String capacity;

    @Column(comment = "起始时间", type = ColumnType.DATE)
    private long startTime;

    @Column(comment = "结束时间", type = ColumnType.DATE)
    private long endTime;

    @Column(comment = "备注", length = 200)
    private String remarks;

    @Column(comment = "是否为默认存储方案")
    private String isDefault;

    @Column(comment = "拥有者")
    private String spaceOwner;

    @Column(comment = "文件数量")
    private long objectNum;
    @Column(comment = "所有文件大小")
    private long totalFileSize;

    @Column(comment = "对象数规格")
    private String spec;
    @Column(comment = "所属站点")
    private String site;
    @Column(comment = "写保护")
    private String isWriteProtect;
    @Column(comment = "机构id")
    private String organiseId;
    @Column(comment = "机构名称")
    private String organiseName;

    @Column(comment = "上次检测时间")
    private long latestDetectDate;
    @Column(comment = "类型(1 服务器,2 移动硬盘)")
    private String spacesType;

    //已用空间百分比
    private double percent = 0;

    //已用空间
    private String usedSpace;

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }

    public String getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(String usedSpace) {
        this.usedSpace = usedSpace;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSpaceOwner() {
        return spaceOwner;
    }

    public void setSpaceOwner(String spaceOwner) {
        this.spaceOwner = spaceOwner;
    }


    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIsWriteProtect() {
        return isWriteProtect;
    }

    public void setIsWriteProtect(String isWriteProtect) {
        this.isWriteProtect = isWriteProtect;
    }

    public long getLatestDetectDate() {
        return latestDetectDate;
    }

    public void setLatestDetectDate(long latestDetectDate) {
        this.latestDetectDate = latestDetectDate;
    }

    public long getObjectNum() {
        return objectNum;
    }

    public void setObjectNum(long objectNum) {
        this.objectNum = objectNum;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public String getSpacesType() {
        return spacesType;
    }

    public void setSpacesType(String spacesType) {
        this.spacesType = spacesType;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }
}
