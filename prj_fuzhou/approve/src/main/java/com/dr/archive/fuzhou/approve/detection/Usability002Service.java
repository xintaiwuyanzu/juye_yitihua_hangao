package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

/**
 * @author: qiuyf
 * 判定电子文件是否有效(电子文件0kb为无效文件）
 */
@Component
public class Usability002Service extends AbstractApproveReceiveDetectService {
    @Autowired
    protected CommonFileService commonFileService;

    @Override
    public String code() {
        return "Usability002";
    }

    @Override
    public String modeCode() {
        return "3-2-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        if (detectEnable(context)) {
            if ("Online".equals(gdType)) {
                File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
                //对包内文件遍历
                try {
                    ZipFile zipFile = new ZipFile(receiveFile);
                    Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
                    while (entries.hasMoreElements()) {
                        ZipArchiveEntry entry = entries.nextElement();
                        if (!entry.isDirectory()) {
                            if (entry.getSize() == 0) {
                                context.addRecordItem(code(), modeCode(),
                                        "电子文件0kb!",
                                        TestRecordService.TEST_STATUS_FAIL,
                                        "",
                                        "电子文件",
                                        entry.getName());
                            } else {
                                context.addRecordItem(code(), modeCode(),
                                        "检测通过",
                                        TestRecordService.TEST_STATUS_SUCCESS,
                                        "",
                                        "电子文件",
                                        entry.getName());
                            }
                        }
                    }
                } catch (Exception ignored) {

                }
            } else {
                //获取原文
                List<FileInfo> fileInfoList = commonFileService.list(context.getFormData().getId(), "archive", "default");
                for (FileInfo fileInfo : fileInfoList) {
                    if (fileInfo.getFileSize() == 0) {
                        context.addRecordItem(code(), modeCode(),
                                "电子文件0kb!",
                                TestRecordService.TEST_STATUS_FAIL,
                                "",
                                "电子文件",
                                fileInfo.getName());
                    } else {
                        context.addRecordItem(code(), modeCode(),
                                "检测通过",
                                TestRecordService.TEST_STATUS_SUCCESS,
                                "",
                                "电子文件",
                                fileInfo.getName());
                    }
                }
            }

        }
    }
}
