package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ExternalHardDisk;
import com.dr.archive.common.dzdacqbc.entity.ExternalHardDiskAssociation;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface ExternalDiskAssociationService extends BaseService<ExternalHardDiskAssociation> {

    /**
     * 继续备份
     */
    String CONTINUE = "continue";
    /**
     * 重新备份
     */
    String RE_BACKUP = "reBackup";

    ExternalHardDisk saveJudgeDisk (String diskName ,String uniqueIdentification,long storageSize , String classifiId ,String BackupStatus);

    ResultEntity selectReturnData(String diskName ,String uniqueIdentification,long storageSize , String classifiId ,String BackupStatus);

    ResultEntity updateBackups(List<String> list,String classifiId,String uniqueIdentification);
}
