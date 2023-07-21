package com.dr.archive.kufang.entityfiles.service;

import com.dr.archive.kufang.archives.entity.LocationKuFang;
import com.dr.archive.kufang.entityfiles.entity.*;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class RoomDetailsServiceImpl extends DefaultBaseService<RoomDetails> implements RoomDetailsService{


    @Autowired
    CommonMapper commonMapper;


    @Override
    public ResultEntity updateDetail(String kufangId) {
        Assert.isTrue(!"".equals(kufangId),"库房信息为空");
        if(!"All".equals(kufangId)){

            List<Map> entityFiles = commonMapper.selectByQuery(SqlQuery.from(EntityFiles.class,false)
                    .count(EntityFilesInfo.ID, "SUM")
                    .column(EntityFilesInfo.ARCHIVETYPE)
                    .equal(EntityFilesInfo.LOCID, kufangId)
                    .groupBy(EntityFilesInfo.ARCHIVETYPE)
                    .setReturnClass(Map.class));
            entityFiles.forEach(i->{
                String archiveType1 = (String) i.get("archiveType");
                ArchiveType archiveType = commonMapper.selectById(ArchiveType.class, archiveType1);
                RoomDetails roomDetails = new RoomDetails();
                roomDetails.setKufangId(kufangId);
                roomDetails.setArchivesType(archiveType1);
                roomDetails.setArchivesName(archiveType.getArchiveTypeName());
                roomDetails.setArchivesNum(String.valueOf(i.get("SUM")));
                SqlQuery<RoomDetails> roomDetailsSqlQuery = SqlQuery.from(RoomDetails.class).equal(RoomDetailsInfo.ARCHIVESTYPE,archiveType.getId()).equal(RoomDetailsInfo.KUFANGID,kufangId);
                RoomDetails details = commonMapper.selectOneByQuery(roomDetailsSqlQuery);
                if(!StringUtils.isEmpty(details)){
                    roomDetails.setId(details.getId());
                    commonMapper.updateIgnoreNullById(roomDetails);
                }else {
                    roomDetails.setId(UUIDUtils.getUUID());
                    commonMapper.insertIgnoreNull(roomDetails);
                }

            });
        }else {
            List<LocationKuFang> locationKuFangs = commonMapper.selectAll(LocationKuFang.class);
            locationKuFangs.forEach(s->{
                long l = commonMapper.countByQuery(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.LOCID, s.getId()));
                if(l!=0){
                     List<Map>entityFiles = commonMapper.selectByQuery(
                            SqlQuery.from(EntityFiles.class,false)
                            .count(EntityFilesInfo.ID, "SUM")
                            .column(EntityFilesInfo.ARCHIVETYPE)
                            .equal(EntityFilesInfo.LOCID, kufangId)
                            .groupBy(EntityFilesInfo.ARCHIVETYPE)
                            .setReturnClass(Map.class));
                    entityFiles.forEach(i->{
                        ArchiveType archiveType = commonMapper.selectById(ArchiveType.class, (String) i.get("archiveType"));
                        RoomDetails roomDetails = new RoomDetails();
                        roomDetails.setKufangId(kufangId);
                        roomDetails.setArchivesType((String) i.get("archiveType"));
                        roomDetails.setArchivesName(archiveType.getArchiveTypeName());
                        roomDetails.setArchivesNum(String.valueOf(i.get("SUM")));
                        SqlQuery<RoomDetails> roomDetailsSqlQuery = SqlQuery.from(RoomDetails.class).equal(RoomDetailsInfo.ARCHIVESTYPE,archiveType.getId()).equal(RoomDetailsInfo.KUFANGID,kufangId);
                        RoomDetails details = commonMapper.selectOneByQuery(roomDetailsSqlQuery);
                        if(!StringUtils.isEmpty(details)){
                            roomDetails.setId(details.getId());
                            commonMapper.updateIgnoreNullById(roomDetails);
                        }else {
                            roomDetails.setId(UUIDUtils.getUUID());
                            commonMapper.insertIgnoreNull(roomDetails);
                        }
                    });
                }
            });

        }

        return ResultEntity.success("同步完成");
    }
}
