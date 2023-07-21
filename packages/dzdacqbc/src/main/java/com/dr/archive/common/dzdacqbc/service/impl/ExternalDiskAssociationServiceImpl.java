package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.ExternalDiskAssociationService;
import com.dr.archive.common.dzdacqbc.vo.BackupsDataVO;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ExternalDiskAssociationServiceImpl extends DefaultBaseService<ExternalHardDiskAssociation> implements ExternalDiskAssociationService {

    @Autowired
    CommonMapper commonMapper;

    @Override
    public ExternalHardDisk saveJudgeDisk(String diskName, String uniqueIdentification, long storageSize, String classifiId,String backupStatus) {

        //根据硬盘的唯一标识和分类查
        ExternalHardDisk externalHardDisk = commonMapper.selectOneByQuery(SqlQuery.from(ExternalHardDisk.class)
                .equal(ExternalHardDiskInfo.UNIQUEIDENTIFICATION, uniqueIdentification)
                .equal(ExternalHardDiskInfo.CLASSIFIID,classifiId));
        //当前分类不存在当前硬盘的情况
        if(StringUtils.isEmpty(externalHardDisk)){
            externalHardDisk = new ExternalHardDisk();
            externalHardDisk.setDiskName(diskName);
            externalHardDisk.setId(UUIDUtils.getUUID());
            externalHardDisk.setUniqueIdentification(uniqueIdentification);
            externalHardDisk.setStorageSize(storageSize);
            externalHardDisk.setClassifiId(classifiId);
            //查询当前分类有无硬盘
            List<ExternalHardDisk> externalHardDisks = commonMapper.selectByQuery(SqlQuery.from(ExternalHardDisk.class).equal(ExternalHardDiskInfo.CLASSIFIID, classifiId).orderByDesc(ExternalHardDiskInfo.LASTBACKUPSDATE));
            //查分类
            CqbcClassification cqbcClassification = commonMapper.selectById(CqbcClassification.class, classifiId);
            //分类档案总数
            long l = commonMapper.countByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID, cqbcClassification.getId()));
            //如果有硬盘那就是有分组,新增硬盘
            if (!externalHardDisks.isEmpty()){

                long aready = 0;
                long backedUp = 0;
                String hardDiskGrouping = externalHardDisks.get(0).getHardDiskGrouping();
                //判断当前分组是否结束
                for (ExternalHardDisk externalHard : externalHardDisks){
                    if(externalHard.getHardDiskGrouping().equals(hardDiskGrouping)){
                        aready += externalHard.getAlreadyBackupsArchiveNum();
                        backedUp += externalHard.getBackupsArchiveNum();
                    }
                }
                // 接着继续备份
                if(backupStatus.equals(CONTINUE)){
                    // 判断当前备份是否完成
                    if(aready >= backedUp){
                        externalHardDisk.setHardDiskGrouping(hardDiskGrouping);
                        //判断分类是否更新过
                        if(aready < l){
                            long num = (((externalHardDisk.getStorageSize() - (externalHardDisk.getStorageSize() / 10)) * 1000) / 20)* 1024;
                            long surplusArchive = l - aready;
                            // 当前分类查找最新备份的档案
                            List<EArchive> archives = commonMapper.selectLimitByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID,externalHardDisk.getClassifiId()).orderByDesc(EArchiveInfo.LASTBACKUPSDATE), 0, (int) surplusArchive);
                            // 根据大小算数量
                            long someNum = 0;
                            long backupsNum = 0;
                            for (int i = 0;i<archives.size();i++) {
                                someNum += archives.get(i).getFileTotalSize();
                                if (someNum <= num) {
                                    ExternalHardDiskAssociation externalHardDiskAssociation = new ExternalHardDiskAssociation();
                                    externalHardDiskAssociation.setArchiveId(archives.get(i).getId());
                                    externalHardDiskAssociation.setClassifiId(archives.get(i).getClassificationId());
                                    externalHardDiskAssociation.setCreateDate(System.currentTimeMillis());
                                    //externalHardDiskAssociation.setCreatePerson(SecurityHolder.get().currentPerson().getId());
                                    externalHardDiskAssociation.setHardDiskGrouping(externalHardDisk.getHardDiskGrouping());
                                    externalHardDiskAssociation.setBackupsArchiveNum(archives.get(i).getFileTotalSize());

                                    externalHardDiskAssociation.setLastBackupsDate(archives.get(i).getLastBackupsDate());
                                    externalHardDiskAssociation.setId(UUIDUtils.getUUID());
                                    externalHardDiskAssociation.setLastUpdateDate(System.currentTimeMillis());
                                    commonMapper.insertIgnoreNull(externalHardDiskAssociation);
                                    backupsNum++;
                                } else {
                                    someNum -= 1;
                                }
                            }
                            externalHardDisk.setLastBackupsDate(0);
                            externalHardDisk.setBackupsArchiveNum(backupsNum);
                            externalHardDisk.setAlreadyBackupsArchiveNum(0);
                            commonMapper.insertIgnoreNull(externalHardDisk);
                            return externalHardDisk;

                        }else {
                            //没有更新过想要重新备份
                            long num = (((externalHardDisk.getStorageSize() - (externalHardDisk.getStorageSize() / 10)) * 1000) / 20)* 1024;
                            //List<ExternalHardDiskAssociation> externalHardDiskAssociations = commonMapper.selectLimitByQuery(SqlQuery.from(ExternalHardDiskAssociation.class).equal(ExternalHardDiskAssociationInfo.CLASSIFIID, externalHardDisk.getClassifiId()).orderBy(), 0, (int) num);
                            List<EArchive> archives = commonMapper.selectLimitByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID,externalHardDisk.getClassifiId()), 0, (int) num);
                            // 根据大小算数量
                            long someNum = 0;
                            long backupsNum = 0;
                            for (int i = 0;i<archives.size();i++){
                                someNum += archives.get(i).getFileTotalSize();
                                if(someNum <= num){
                                    ExternalHardDiskAssociation externalHardDiskAssociation = new ExternalHardDiskAssociation();
                                    externalHardDiskAssociation.setArchiveId(archives.get(i).getId());
                                    externalHardDiskAssociation.setClassifiId(archives.get(i).getClassificationId());
                                    externalHardDiskAssociation.setCreateDate(System.currentTimeMillis());
                                    //externalHardDiskAssociation.setCreatePerson(SecurityHolder.get().currentPerson().getId());
                                    externalHardDiskAssociation.setHardDiskGrouping(externalHardDisk.getHardDiskGrouping());
                                    externalHardDiskAssociation.setBackupsArchiveNum(archives.get(i).getFileTotalSize());

                                    externalHardDiskAssociation.setLastBackupsDate(0);
                                    externalHardDiskAssociation.setId(UUIDUtils.getUUID());
                                    externalHardDiskAssociation.setLastUpdateDate(System.currentTimeMillis());
                                    commonMapper.insertIgnoreNull(externalHardDiskAssociation);
                                    backupsNum++;
                                }else {
                                    someNum -= 1;
                                }
                            }
                            externalHardDisk.setLastBackupsDate(0);
                            externalHardDisk.setBackupsArchiveNum(backupsNum);
                            externalHardDisk.setAlreadyBackupsArchiveNum(0);
                            commonMapper.insertIgnoreNull(externalHardDisk);
                            return externalHardDisk;
                        }
                        //未完成备份
                    }else {
                        long num = (((externalHardDisk.getStorageSize() - (externalHardDisk.getStorageSize() / 10)) * 1000) / 20)* 1024;
                        // ExternalHardDiskAssociation 所有未更新的数据
                        List<ExternalHardDiskAssociation> externalHardDiskAssociations = commonMapper.selectLimitByQuery(SqlQuery.from(ExternalHardDiskAssociation.class)
                                .equal(ExternalHardDiskAssociationInfo.HARDDISKGROUPING, externalHardDisk.getHardDiskGrouping())
                                .orderBy(ExternalHardDiskAssociationInfo.LASTBACKUPSDATE), 0, (int) (backedUp - aready));
                        long some = 0;
                        long backupsNum = 0;
                        for (ExternalHardDiskAssociation externalHardDiskAssociation : externalHardDiskAssociations){
                            some += externalHardDiskAssociation.getBackupsArchiveNum();
                            if(some <= num){
                                backupsNum++;
                            }else {
                                break;
                            }
                        }
                        externalHardDisk.setLastBackupsDate(0);
                        externalHardDisk.setBackupsArchiveNum(backupsNum);
                        externalHardDisk.setAlreadyBackupsArchiveNum(0);
                        commonMapper.insertIgnoreNull(externalHardDisk);
                        return externalHardDisk;
                    }
                }
                //重新备份
                if(backupStatus.equals(RE_BACKUP)){
                    //没有分组的增加分组,也就是新的分组
                    externalHardDisk.setHardDiskGrouping(cqbcClassification.getClassificationName()+":"+System.currentTimeMillis()+"创建的组");
                    //计算当前硬盘可以存储的大小
                    long num = (((externalHardDisk.getStorageSize() - (externalHardDisk.getStorageSize() / 10)) * 1000) / 20)* 1024;
                    num *= 1024;
                    List<EArchive> archives = commonMapper.selectLimitByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID,externalHardDisk.getClassifiId()), 0, (int) num);
                    // 根据大小算数量
                    long someNum = 0;
                    long backupsNum = 0;
                    for (int i = 0;i<archives.size();i++){
                        someNum += archives.get(i).getFileTotalSize();
                        if(someNum <= num){
                            ExternalHardDiskAssociation externalHardDiskAssociation = new ExternalHardDiskAssociation();
                            externalHardDiskAssociation.setArchiveId(archives.get(i).getId());
                            externalHardDiskAssociation.setClassifiId(archives.get(i).getClassificationId());
                            externalHardDiskAssociation.setCreateDate(System.currentTimeMillis());
                            //externalHardDiskAssociation.setCreatePerson(SecurityHolder.get().currentPerson().getId());
                            externalHardDiskAssociation.setHardDiskGrouping(externalHardDisk.getHardDiskGrouping());
                            externalHardDiskAssociation.setBackupsArchiveNum(archives.get(i).getFileTotalSize());
                            //这个新分组的第一个硬盘
                            externalHardDiskAssociation.setLastBackupsDate(0);
                            externalHardDiskAssociation.setId(UUIDUtils.getUUID());
                            externalHardDiskAssociation.setLastUpdateDate(System.currentTimeMillis());
                            commonMapper.insertIgnoreNull(externalHardDiskAssociation);
                            backupsNum++;
                        }else {
                            someNum -= 1;
                        }
                    }
                    externalHardDisk.setLastBackupsDate(0);
                    externalHardDisk.setBackupsArchiveNum(backupsNum);
                    externalHardDisk.setAlreadyBackupsArchiveNum(0);
                    commonMapper.insertIgnoreNull(externalHardDisk);
                    return externalHardDisk;
                }
            //如果以前没有硬盘，该分组的第一块硬盘
            }else {


                //没有分组的增加分组,也就是新的分组
                externalHardDisk.setHardDiskGrouping(cqbcClassification.getClassificationName()+":"+System.currentTimeMillis()+"创建的组");
                //计算当前硬盘可以存储的大小
                long num = (((externalHardDisk.getStorageSize() - (externalHardDisk.getStorageSize() / 10)) * 1000) / 20)* 1024;
                num *= 1024;
                List<EArchive> archives = commonMapper.selectLimitByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID,externalHardDisk.getClassifiId()), 0, (int) num);
                // 根据大小算数量
                long someNum = 0;
                long backupsNum = 0;
                for (int i = 0;i<archives.size();i++){
                    someNum += archives.get(i).getFileTotalSize();
                    if(someNum <= num){
                        ExternalHardDiskAssociation externalHardDiskAssociation = new ExternalHardDiskAssociation();
                        externalHardDiskAssociation.setArchiveId(archives.get(i).getId());
                        externalHardDiskAssociation.setClassifiId(archives.get(i).getClassificationId());
                        externalHardDiskAssociation.setCreateDate(System.currentTimeMillis());
                        //externalHardDiskAssociation.setCreatePerson(SecurityHolder.get().currentPerson().getId());
                        externalHardDiskAssociation.setHardDiskGrouping(externalHardDisk.getHardDiskGrouping());
                        externalHardDiskAssociation.setBackupsArchiveNum(archives.get(i).getFileTotalSize());
                        //这个新分组的第一个硬盘
                        externalHardDiskAssociation.setLastBackupsDate(0);
                        externalHardDiskAssociation.setId(UUIDUtils.getUUID());
                        externalHardDiskAssociation.setLastUpdateDate(System.currentTimeMillis());
                        commonMapper.insertIgnoreNull(externalHardDiskAssociation);
                        backupsNum++;
                    }else {
                        someNum -= 1;
                    }
                }
                externalHardDisk.setLastBackupsDate(0);
                externalHardDisk.setBackupsArchiveNum(backupsNum);
                externalHardDisk.setAlreadyBackupsArchiveNum(0);
                commonMapper.insertIgnoreNull(externalHardDisk);
                return externalHardDisk;
            }
        }else {
            long quantity = commonMapper.countByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID, externalHardDisk.getClassifiId()));
            //将原先的分组备份情况记录
            long l = commonMapper.countByQuery(SqlQuery.from(ExternalHardDiskAssociation.class)
                    .equal(ExternalHardDiskAssociationInfo.CLASSIFIID, externalHardDisk.getClassifiId())
                    .equal(ExternalHardDiskAssociationInfo.HARDDISKGROUPING,externalHardDisk.getHardDiskGrouping()));
            long num = (((externalHardDisk.getStorageSize() - (externalHardDisk.getStorageSize() / 10)) * 1000) / 20)* 1024;
            long newBackups = quantity - l;
            List<EArchive> archives = commonMapper.selectLimitByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID,externalHardDisk.getClassifiId()).orderBy(EArchiveInfo.LASTBACKUPSDATE), 0, (int) newBackups);

            // 根据大小算数量
            long someNum = 0;
            long backupsNum = 0;
            for (int i = 0;i<archives.size();i++){
                someNum += archives.get(i).getFileTotalSize();
                if(someNum <= num){
                    ExternalHardDiskAssociation externalHardDiskAssociation = new ExternalHardDiskAssociation();
                    externalHardDiskAssociation.setArchiveId(archives.get(i).getId());
                    externalHardDiskAssociation.setCreateDate(System.currentTimeMillis());
                    //externalHardDiskAssociation.setCreatePerson(SecurityHolder.get().currentPerson().getId());
                    externalHardDiskAssociation.setClassifiId(archives.get(i).getClassificationId());
                    externalHardDiskAssociation.setHardDiskGrouping(externalHardDisk.getHardDiskGrouping());
                    externalHardDiskAssociation.setBackupsArchiveNum(archives.get(i).getFileTotalSize());
                    //这个新分组的第一个硬盘
                    externalHardDiskAssociation.setLastBackupsDate(0);
                    externalHardDiskAssociation.setId(UUIDUtils.getUUID());
                    externalHardDiskAssociation.setLastUpdateDate(System.currentTimeMillis());
                    commonMapper.insertIgnoreNull(externalHardDiskAssociation);
                    backupsNum++;
                }else {
                    someNum -= 1;
                }
            }
            externalHardDisk.setLastBackupsDate(0);
            externalHardDisk.setBackupsArchiveNum(backupsNum+externalHardDisk.getBackupsArchiveNum());
            //重新更新备份
            if(backupStatus.equals(RE_BACKUP)){
                externalHardDisk.setAlreadyBackupsArchiveNum(0);
            }
            commonMapper.updateIgnoreNullById(externalHardDisk);
            return externalHardDisk;
        }
        return null;
    }

    @Override
    public ResultEntity selectReturnData(String diskName ,String uniqueIdentification,long storageSize , String classifiId ,String BackupStatus) {
        ExternalHardDisk externalHardDisk = saveJudgeDisk(diskName, uniqueIdentification, storageSize, classifiId, BackupStatus);
//        List<ExternalHardDiskAssociation> externalHardDiskAssociations = commonMapper.selectLimitByQuery(SqlQuery.from(ExternalHardDiskAssociation.class)
//                .equal(ExternalHardDiskAssociationInfo.HARDDISKGROUPING, externalHardDisk.getHardDiskGrouping())
//                .equal(ExternalHardDiskAssociationInfo.CLASSIFIID, externalHardDisk.getClassifiId())
//                .orderBy(), 0, (int)(externalHardDisk.getBackupsArchiveNum() - externalHardDisk.getAlreadyBackupsArchiveNum()));
        long count = commonMapper.countByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.CLASSIFICATIONID,externalHardDisk.getClassifiId()));
        BackupsDataVO backupsData = new BackupsDataVO();
        backupsData.setCurrentGrouping(externalHardDisk.getHardDiskGrouping());
        backupsData.setAssigned(externalHardDisk.getBackupsArchiveNum() - externalHardDisk.getAlreadyBackupsArchiveNum());
        backupsData.setUnassigned(count);
        return ResultEntity.success(backupsData);
    }

    @Override
    public ResultEntity updateBackups(List<String> list,String classifiId,String uniqueIdentification) {
        Person person = SecurityHolder.get().currentPerson();
        list.forEach(i->{
            EArchive eArchive = commonMapper.selectById(EArchive.class, i);
            eArchive.setLastBackupsDate(System.currentTimeMillis());
            eArchive.setUpdateDate(System.currentTimeMillis());
            eArchive.setUpdatePerson(person.getId());
            commonMapper.updateIgnoreNullById(eArchive);
            ExternalHardDiskAssociation externalHardDiskAssociation = commonMapper.selectOneByQuery(SqlQuery.from(ExternalHardDiskAssociation.class).equal(ExternalHardDiskAssociationInfo.ARCHIVEID).equal(ExternalHardDiskAssociationInfo.CLASSIFIID));
            externalHardDiskAssociation.setLastBackupsDate(System.currentTimeMillis());
            externalHardDiskAssociation.setUpdatePerson(String.valueOf(System.currentTimeMillis()));
            commonMapper.updateIgnoreNullById(externalHardDiskAssociation);
            ExternalHardDisk externalHardDisk = commonMapper.selectOneByQuery(SqlQuery.from(ExternalHardDisk.class).equal(ExternalHardDiskInfo.CLASSIFIID, classifiId).equal(ExternalHardDiskInfo.UNIQUEIDENTIFICATION, uniqueIdentification));
            externalHardDisk.setLastBackupsDate(System.currentTimeMillis());
            externalHardDisk.setAlreadyBackupsArchiveNum(externalHardDisk.getAlreadyBackupsArchiveNum()+1);
            commonMapper.updateById(externalHardDisk);
        });
        return ResultEntity.success();
    }

}
