package com.dr.archive.receive.online.controller;

import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManage;
import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManageInfo;
import com.dr.archive.receive.online.service.ArchiveReceiveOnlineSysManageService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/3/7 14:20
 */
@RestController
@RequestMapping("${common.api-path:/api}/receive/sysManage")
public class ArchiveReceiveOnlineSysManageController extends BaseServiceController<ArchiveReceiveOnlineSysManageService, ArchiveReceiveOnlineSysManage> {
    @Autowired
    ArchiveReceiveOnlineSysManageService archiveReceiveOnlineSysManageService;

    @Override
    protected SqlQuery<ArchiveReceiveOnlineSysManage> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveReceiveOnlineSysManage archiveReceiveOnlineSysManage) {
        SqlQuery<ArchiveReceiveOnlineSysManage> sqlQuery = SqlQuery.from(ArchiveReceiveOnlineSysManage.class);

        sqlQuery.orderByDesc(ArchiveReceiveOnlineSysManageInfo.CREATEDATE);
        return sqlQuery;
    }

    //生成密钥
    @RequestMapping({"/getPublicKey"})
    public ResultEntity getPublicKey(String id){
        return ResultEntity.success(archiveReceiveOnlineSysManageService.getPublicKey(id));
    }
}
