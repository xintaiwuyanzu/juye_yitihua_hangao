package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author: qiuyf
 * 依托云平台网络防火墙和服务器杀毒软件实现病毒检测，通过检测后才能接收到归档信息包
 */
@Component
public class Security001Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Security001";
    }

    @Override
    public String modeCode() {
        return "4-1-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        if (detectEnable(context) && "Online".equals(gdType)) {
            File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
            if (receiveFile == null) {
                context.addRecordItem(code(), modeCode(), "文件包为空!", TestRecordService.TEST_STATUS_FAIL);
            } else {
                context.addRecordItem(code(), modeCode());
            }
        }
    }
}
