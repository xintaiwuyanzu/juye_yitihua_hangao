package com.dr.archive.fuzhou.approve.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.fuzhou.approve.bo.ArchiveReceiveBo;
import com.dr.archive.fuzhou.approve.bo.TransferInfo;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.StatusEntity;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

/**
 * 写个工具类，主要用来处理参数绑定和工具操作
 * 服务 {@link ReceiptServiceImpl}
 * <p>
 * 使得service本身只需要关注业务逻辑即可
 */
class ReceiptUtil {


    /**
     * 工具方法，帮助创建实体类
     *
     * @param transferInfo
     * @param receiveBo
     * @return
     */
    static ArchiveBatchReceiveOnline newBatch(TransferInfo transferInfo, ArchiveReceiveBo receiveBo) {
        ArchiveBatchReceiveOnline archiveBatch = new ArchiveBatchReceiveOnline();
        archiveBatch.setBatchName(transferInfo.getBatchName());
        try {
            archiveBatch.setStartDate(DateUtils.parseDate(transferInfo.getExchangeTime(), "YYYY-MM-DD hh:mm:ss").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            archiveBatch.setStartDate(System.currentTimeMillis());
        }
       // archiveBatch.setDetailNum(Long.valueOf(transferInfo.getFileNumber()));
        archiveBatch.setSendSize(transferInfo.getSendSize());
        archiveBatch.setTransferUnitPerson(transferInfo.getTransactor());
        archiveBatch.setFileLocation(receiveBo.getXmlPath());
        archiveBatch.setFileName(receiveBo.getXmlPath());
        if (transferInfo.getDirectories().size() > 0) {
            TransferInfo.TransferDirectory directory = transferInfo.getDirectories().get(0);
            archiveBatch.setSystemNum(directory.getDepartmentName());
            archiveBatch.setSystemNum(transferInfo.getSystemCode());
        } else {
            archiveBatch.setSystemNum(transferInfo.getSystemCode());
        }
        archiveBatch.setBatchType("PRE_ARCHIVE");
        archiveBatch.setStatus(StatusEntity.STATUS_ENABLE_STR);
        return archiveBatch;
    }

    /**
     * 创建归档详情
     *
     * @param archiveBatch
     * @param archiveFileInfo
     * @return
     */
    static ArchiveBatchDetailReceiveOnline newDetail(AbstractArchiveBatch archiveBatch, ArchiveReceiveBo.ArchiveFileInfo archiveFileInfo) {
        ArchiveBatchDetailReceiveOnline receiptBatchDetail = new ArchiveBatchDetailReceiveOnline();
        receiptBatchDetail.setId(UUIDUtils.getUUID());
        receiptBatchDetail.setBatchId(archiveBatch.getId());
        //归档中
        receiptBatchDetail.setStatus("2");
        receiptBatchDetail.setSystemNum(archiveFileInfo.getSystemNum());
        receiptBatchDetail.setSystemName(archiveFileInfo.getSystemName());
        receiptBatchDetail.setBusinessId(archiveFileInfo.getBusinessId());
        receiptBatchDetail.setOfdName(archiveFileInfo.getOfdName());
        receiptBatchDetail.setPath(archiveFileInfo.getPath());
        receiptBatchDetail.setDigitaldigest(archiveFileInfo.getDigitaldigest());
        return receiptBatchDetail;
    }

    /**
     * 从zip中读取所有的附件
     *
     * @param readFile
     * @return
     */
    public static List<String> readZipFiles(InputStream readFile) {
        List<String> zipContentFiles = new ArrayList<>();
        try (ZipArchiveInputStream zais = new ZipArchiveInputStream(readFile, "utf-8")) {
            ZipArchiveEntry archiveEntry = zais.getNextZipEntry();
            while (archiveEntry != null) {
                if (!archiveEntry.isDirectory()) {
                    zipContentFiles.add(archiveEntry.getName());
                }
                archiveEntry = zais.getNextZipEntry();
            }
        } catch (IOException ignored) {
        }
        return zipContentFiles;
    }


    public static Set<String> filterNoContent(Collection<String> source, Collection<String> target) {
        Set<String> result = new HashSet<>();
        for (String name : source) {
            if (!target.contains(name)) {
                result.add(name);
            }
        }
        return result;
    }

}
