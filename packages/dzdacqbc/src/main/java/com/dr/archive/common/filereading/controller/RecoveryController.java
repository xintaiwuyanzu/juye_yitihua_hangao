package com.dr.archive.common.filereading.controller;


import com.dr.archive.common.filereading.entity.BackupRecovery;
import com.dr.archive.common.filereading.entity.BackupRecoveryInfo;
import com.dr.archive.common.filereading.service.BackupRecoveryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 备份
 */
@RestController
@RequestMapping({"/api/recovery"})
public class RecoveryController extends BaseServiceController<BackupRecoveryService, BackupRecovery> {

    @Autowired
    BackupRecoveryService backupRecoveryService;

    @Override
    protected SqlQuery<BackupRecovery> buildPageQuery(HttpServletRequest httpServletRequest, BackupRecovery backupRecovery) {
        SqlQuery<BackupRecovery> sqlQuery = SqlQuery.from(BackupRecovery.class);
        sqlQuery.like(BackupRecoveryInfo.CREATEPERSONNAME, backupRecovery.getCreatePersonName())
                .equal(BackupRecoveryInfo.DOTYPE, backupRecovery.getDoType())
                .notEqual(BackupRecoveryInfo.DOTYPE, BackupRecovery.DOTYPE_SEND)
                .equal(BackupRecoveryInfo.VERSIONNUM, backupRecovery.getVersionNum())
                .equal(BackupRecoveryInfo.SYSNAME, backupRecovery.getSysName())
        ;
        sqlQuery.orderByDesc(BackupRecoveryInfo.VERSIONNUM);
        return sqlQuery;
    }

    @RequestMapping({"/recovery"})
    public ResultEntity recovery(String filePath, String showPath, String versionNum, String sysName) {
        BackupRecovery recovery = backupRecoveryService.recovery(filePath, showPath, versionNum, sysName);
        return ResultEntity.success("恢复执行中，请稍后刷新页面查看结果！");
    }

    @RequestMapping({"/check"})
    public ResultEntity check(String sysName) {
        List<BackupRecovery> list = backupRecoveryService.check(sysName);
        return ResultEntity.success(list);
    }
}
