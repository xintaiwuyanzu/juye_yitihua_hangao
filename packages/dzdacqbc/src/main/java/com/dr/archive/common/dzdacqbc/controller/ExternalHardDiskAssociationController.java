package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.ExternalHardDisk;
import com.dr.archive.common.dzdacqbc.entity.ExternalHardDiskAssociation;
import com.dr.archive.common.dzdacqbc.entity.ExternalHardDiskAssociationInfo;
import com.dr.archive.common.dzdacqbc.service.ExternalDiskAssociationService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 长期保存库备份
 */
@RestController
@RequestMapping("/api/backups")
public class ExternalHardDiskAssociationController extends BaseServiceController<ExternalDiskAssociationService, ExternalHardDiskAssociation> {

    @Autowired
    private ExternalDiskAssociationService externalDiskAssociationService;


    /**
     *
     * @param diskName 硬盘名称
     * @param uniqueIdentification 硬盘唯一标识
     * @param storageSize 硬盘大小 GB
     * @param classifiId 分类Id
     * @param backupStatus 备份全量备份和继续追加备份
     * @return
     */
    @RequestMapping("/saveJudgeDisk")
    public ResultEntity adoptHardDiskObtain(String diskName ,String uniqueIdentification,String storageSize , String classifiId , String backupStatus){
        return externalDiskAssociationService.selectReturnData(diskName, uniqueIdentification, Long.parseLong(storageSize), classifiId, backupStatus);
    }

    /**
     *   更改备份状态
     * @param eArchiveIds 档案id
     * @param classifiId  分类id
     * @param uniqueIdentification 硬盘唯一标识
     * @return
     */
    @RequestMapping("/updateBackups")
    public ResultEntity updateBackups(@RequestBody List<String> eArchiveIds,String classifiId,String uniqueIdentification){
        return externalDiskAssociationService.updateBackups(eArchiveIds, classifiId, uniqueIdentification);
    }

    @Override
    protected SqlQuery<ExternalHardDiskAssociation> buildPageQuery(HttpServletRequest request, ExternalHardDiskAssociation entity) {
        return SqlQuery.from(ExternalHardDiskAssociation.class)
                .equal(ExternalHardDiskAssociationInfo.HARDDISKGROUPING,entity.getHardDiskGrouping())
                .orderBy(ExternalHardDiskAssociationInfo.LASTBACKUPSDATE);
    }
}
