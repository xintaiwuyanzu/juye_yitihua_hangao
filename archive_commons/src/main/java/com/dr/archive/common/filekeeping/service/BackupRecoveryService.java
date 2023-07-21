package com.dr.archive.common.filekeeping.service;

import com.dr.archive.common.filekeeping.entity.BackupRecovery;
import com.dr.framework.common.service.BaseService;

/**
 * @author caor
 * @Date 2020-11-04 9:50
 */
public interface BackupRecoveryService extends BaseService<BackupRecovery> {
    void recovery(String id);

    BackupRecovery backup(BackupRecovery backupRecovery);
}
