package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.archive.transfer.bo.TransferInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;

/**
 * @author: qiuyf
 * 完整性
 * 2-1-1 检测交接单据中归档信息包字节数与归档信息包实际字节数是否相同
 */
@Component
public class Integrity001Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Integrity001";
    }

    @Override
    public String modeCode() {
        return "2-1-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        //只对在线接收进行该项检测
        if (detectEnable(context) && "Online".equals(gdType)) {
            //交接单据
            TransferInfo.TransferDirectory directory = context.getAttribute(ApprovalOnlineReceiveProvider.DIRECTORY_KEY);
            Assert.notNull(directory, "交接单据未找到");
            //数据包
            File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
            Assert.notNull(receiveFile, "文件包为空！");
            if (receiveFile.length() == Long.parseLong(directory.getSize())) {
                context.addRecordItem(code(), modeCode());
            } else {
                context.addRecordItem(code(), modeCode(), "交接单据中归档信息包字节数(" + directory.getSize() + ")与归档信息包实际字节数(" + receiveFile.length() + ")不相同!", TestRecordService.TEST_STATUS_FAIL);
            }
        }
    }
}
