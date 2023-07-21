package com.dr.archive.common.filereading.entity;

/**
 * @author caor
 * @Date 2020-11-04 14:47
 */

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 备份恢复
 */
@Table(name = Constants.TABLE_PREFIX + "BACKUPRECOVERY", module = Constants.MODULE_NAME, comment = "备份恢复记录")
public class BackupRecovery extends BaseStatusEntity<String> {
    @Column(comment = "备份路径", length = 500, order = 8)
    private String backupRecoveryPath;
    @Column(comment = "展示路径", length = 500, order = 9)
    private String showPath;
    @Column(comment = "创建人姓名", length = 50, order = 10)
    private String createPersonName;
    @Column(comment = "版本号", length = 100, order = 11)
    private Long versionNum;
    @Column(comment = "系统", length = 100, order = 12)
    private String sysName;
    @Column(comment = "同步类型", length = 100, order = 13)
    private String doType;//send  发送  receive 接收
    @Column(comment = "表名", length = 100, order = 14)
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public final static String DOTYPE_SEND = "send";
    public final static String DOTYPE_RECEIVE = "receive";

    public String getDoType() {
        return doType;
    }

    public void setDoType(String doType) {
        this.doType = doType;
    }

    public void setVersionNum(Long versionNum) {
        this.versionNum = versionNum;
    }

    public Long getVersionNum() {
        return versionNum;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getBackupRecoveryPath() {
        return backupRecoveryPath;
    }

    public void setBackupRecoveryPath(String backupRecoveryPath) {
        this.backupRecoveryPath = backupRecoveryPath;
    }

    public String getShowPath() {
        return showPath;
    }

    public void setShowPath(String showPath) {
        this.showPath = showPath;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

}
