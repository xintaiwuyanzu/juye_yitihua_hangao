package com.dr.archive.common.filereading.service;


import com.dr.archive.common.filereading.entity.BackupRecovery;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface BackupRecoveryService extends BaseService<BackupRecovery> {


    BackupRecovery backup(String table);

    BackupRecovery recovery(String filePath, String showPath, String versionNum, String sysName);

    List<BackupRecovery> check(String sysName);

    void wjbackup();
}
