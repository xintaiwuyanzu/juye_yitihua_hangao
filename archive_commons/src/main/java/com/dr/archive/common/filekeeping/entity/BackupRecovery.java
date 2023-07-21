package com.dr.archive.common.filekeeping.entity;

/**
 * @author caor
 * @Date 2020-11-04 14:47
 */

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * @author caor
 * @Date 2020-11-02 13:59
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "BACKUPRECOVERY", module = Constants.COMMON_MODULE_NAME, comment = "备份恢复记录")
public class BackupRecovery extends BaseStatusEntity<String> {
    @Column(comment = "备份路径", length = 500)
    private String backupRecoveryPath;
    @Column(comment = "展示路径", length = 500)
    private String showPath;
    @Column(comment = "创建人姓名", length = 50)
    private String createPersonName;

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
