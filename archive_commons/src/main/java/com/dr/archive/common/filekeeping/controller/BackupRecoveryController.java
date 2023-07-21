package com.dr.archive.common.filekeeping.controller;

import com.dr.archive.common.filekeeping.entity.BackupRecovery;
import com.dr.archive.common.filekeeping.entity.BackupRecoveryInfo;
import com.dr.archive.common.filekeeping.service.BackupRecoveryService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author caor
 * @Date 2020-11-04 9:44
 */
@RestController
@RequestMapping({"${common.api-path:/api}/backuprecovery"})
public class BackupRecoveryController extends BaseServiceController<BackupRecoveryService, BackupRecovery> {
    @Override
    protected SqlQuery<BackupRecovery> buildPageQuery(HttpServletRequest httpServletRequest, BackupRecovery backupRecovery) {
        SqlQuery<BackupRecovery> sqlQuery = SqlQuery.from(BackupRecovery.class);
        if (!StringUtils.isEmpty(backupRecovery.getCreatePersonName())) {
            sqlQuery.like(BackupRecoveryInfo.CREATEPERSONNAME, backupRecovery.getCreatePersonName());
        }
        sqlQuery.orderByDesc(BackupRecoveryInfo.CREATEDATE);
        return sqlQuery;
    }

    @Autowired
    BackupRecoveryService backupRecoveryService;

    @RequestMapping({"/backup"})
    public ResultEntity backup(BackupRecovery backupRecovery) {
        backupRecoveryService.backup(backupRecovery);
        return ResultEntity.success("备份执行中，请稍后刷新页面查看结果！");
    }

    @RequestMapping({"/recovery"})
    public ResultEntity recovery(String id) {
        Assert.isTrue(!StringUtils.isEmpty(id), "id不能为空！");
        backupRecoveryService.recovery(id);
        return ResultEntity.success("恢复执行中，请稍后刷新页面查看结果！");
    }
}
