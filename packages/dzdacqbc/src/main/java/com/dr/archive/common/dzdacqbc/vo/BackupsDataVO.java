package com.dr.archive.common.dzdacqbc.vo;

import com.dr.archive.common.dzdacqbc.entity.ExternalHardDiskAssociation;

import java.util.List;

/**
 * 返回VO
 * 到时候需要啥再添
 */
public class BackupsDataVO {

    //当前分类还剩多少数据未分配 备份盘
    private long unassigned;

    //当前盘未备份的
    private long assigned;

    //分组名
    private String CurrentGrouping;

    public String getCurrentGrouping() {
        return CurrentGrouping;
    }

    public void setCurrentGrouping(String currentGrouping) {
        CurrentGrouping = currentGrouping;
    }

    public long getUnassigned() {
        return unassigned;
    }

    public void setUnassigned(long unassigned) {
        this.unassigned = unassigned;
    }

    public long getAssigned() {
        return assigned;
    }

    public void setAssigned(long assigned) {
        this.assigned = assigned;
    }

}
