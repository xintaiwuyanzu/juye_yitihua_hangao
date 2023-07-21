package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dataClient.entity.DataBatch;
import com.dr.archive.common.dataClient.entity.DataBatchFileDetail;
import com.dr.archive.common.dataClient.service.DataBatchService;
import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.ESaveMediumService;
import com.dr.archive.common.dzdacqbc.service.ESaveOffLineService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;

@Service
public class ESaveOffLineServiceImpl extends DefaultBaseService<ESaveOffLine> implements ESaveOffLineService {

    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ESaveMediumService eSaveMediumService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    DataBatchService dataBatchService;

    @Override
    public ESaveOffLine addBefore(ESaveOffLine entity) {
        Fond fond = fondService.selectById(entity.getFondId());
        if (fond != null) {
            entity.setFond_code(fond.getCode());
            entity.setFond_name(fond.getName());
        }
        Category category = categoryService.selectById(entity.getClassId());
        if (category != null) {
            entity.setClassCode(category.getCode());
            entity.setClassName(category.getName());
        }
        entity = changeBefore(entity);
        SqlQuery<EArchive> sqlQuery = SqlQuery.from(EArchive.class).equal(EArchiveInfo.FONDCODE, entity.getFond_code())
                .equal(EArchiveInfo.CATEGORYCODE, entity.getClassCode()).equal(EArchiveInfo.YEAR, entity.getNd());
        long count = commonMapper.countByQuery(sqlQuery);
        entity.setStrategyCount(count);
        return entity;
    }

    @Override
    public ESaveOffLine changeBefore(ESaveOffLine entity) {
        ESaveMedium eSaveMedium = eSaveMediumService.selectById(entity.getMediumId());
        if (eSaveMedium != null) {
            entity.setMediumName(eSaveMedium.getMediumName());
        }
        return entity;
    }

    /**
     * @param offLineId
     * @return
     */
    @Override
    public long sendDataToClient(String offLineId) {
        ESaveOffLine offLine = selectById(offLineId);
        DataBatch dataBatch = new DataBatch();
        dataBatch.setId(UUIDUtils.getUUID());
        dataBatch.setBatchName(offLine.getStrategyName());
        dataBatch.setBatchDescription("电子档案长期保存离线备份");
        dataBatch.setBatchType("1");
        dataBatchService.insert(dataBatch);

        //找长期保存的原文文件
        SqlQuery sqlQuery = SqlQuery.from(EFileInfo.class)
                .join(EFileInfoInfo.FORMDATAID, ESaveBackBatchDetailInfo.ARCHIVEID)
                .equal(ESaveBackBatchDetailInfo.BATCHID, offLineId)
                .equal(ESaveBackBatchDetailInfo.BACKTYPE, "lx");
        List<EFileInfo> eFileInfos = commonMapper.selectByQuery(sqlQuery);
        if(eFileInfos.size() == offLine.getStrategyCount()){
            for (EFileInfo eFileInfo : eFileInfos) {
                DataBatchFileDetail fileDetail = new DataBatchFileDetail();
                CommonService.bindCreateInfo(fileDetail);
                fileDetail.setBatchId(dataBatch.getId());
                fileDetail.setFileSize(eFileInfo.getFileSize());
                fileDetail.setFilePath(eFileInfo.getFileName());
                int index1 = eFileInfo.getFilePath().lastIndexOf(File.separator);
                int index = index1<0? eFileInfo.getFilePath().lastIndexOf("/"):index1;
                String batchDir = eFileInfo.getFilePath().substring(0, index);
                fileDetail.setBatchDir(batchDir);
                fileDetail.setFileName(eFileInfo.getFileName());
                File file = new File(eFileInfo.getFilePath());
                //求文件的md5hash值
                try {
                    String fileHash = md5HashCode(new FileInputStream(file));
                    fileDetail.setFileHash(fileHash);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                commonMapper.insert(fileDetail);
            }
            offLine.setStatus(StatusEntity.STATUS_ENABLE_STR);//设置为已完成
            updateById(offLine);
            return 1;
        }else{
            return 0;
        }
    }

    public String md5HashCode(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            String fileHash = Hex.encodeHexString(md.digest());
            return fileHash;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
