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

/**
 * 备份
 */
@RestController
@RequestMapping({"/api/backup"})
public class BackupController extends BaseServiceController<BackupRecoveryService, BackupRecovery> {

    @Autowired
    BackupRecoveryService backupRecoveryService;

    @Override
    protected SqlQuery<BackupRecovery> buildPageQuery(HttpServletRequest httpServletRequest, BackupRecovery backupRecovery) {
        SqlQuery<BackupRecovery> sqlQuery = SqlQuery.from(BackupRecovery.class);
        sqlQuery.like(BackupRecoveryInfo.CREATEPERSONNAME, backupRecovery.getCreatePersonName())
                .equal(BackupRecoveryInfo.DOTYPE, BackupRecovery.DOTYPE_SEND)
                .equal(BackupRecoveryInfo.VERSIONNUM, backupRecovery.getVersionNum())
                .equal(BackupRecoveryInfo.SYSNAME, backupRecovery.getSysName())
        ;
        sqlQuery.orderByDesc(BackupRecoveryInfo.VERSIONNUM);
        return sqlQuery;
    }

    /**
     * 备份
     *
     * @param table
     * @return
     */
    @RequestMapping({"/backup"})
    public ResultEntity backup(String table) {
        backupRecoveryService.backup(table);
        return ResultEntity.success("备份执行中，请稍后刷新页面查看结果！");
    }

    /**
     * 文件备份 todo 我不知道我为什么写了这么个东西
     *
     * @return
     */
    @RequestMapping({"/wjbackup"})
    public ResultEntity wjbackup() {
        backupRecoveryService.wjbackup();
        return ResultEntity.success("备份执行中，请稍后刷新页面查看结果！");
    }

}
