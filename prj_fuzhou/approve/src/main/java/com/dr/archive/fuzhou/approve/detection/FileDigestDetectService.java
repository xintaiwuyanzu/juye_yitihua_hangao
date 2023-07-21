package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.archive.transfer.bo.TransferInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * 1-1-2 接受包数字摘要检测
 *
 * @author dr
 */
@Component
public class FileDigestDetectService extends AbstractApproveReceiveDetectService {

    @Override
    public String code() {
        return "Authenticity001";
    }

    @Override
    public String modeCode() {
        return "1-1-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        //先根据上下文判断是否需要检测
        if (detectEnable(context) && "Online".equals(gdType)) {
            //这里面先写死检测方法，实际上摘要算法有多种
            /*detectionDigest(context, code(), modeCode());*/
            context.addRecordItem(code(), modeCode());
        }
    }

    public void detectionDigest(ItemDetectContext context, String code, String modeCode) {
        String fileDigest = computeFileDigest(context);
        //对比数字摘要
        TransferInfo.TransferDirectory directory = context.getAttribute(ApprovalOnlineReceiveProvider.DIRECTORY_KEY);
        Assert.notNull(directory, "交接单据未找到");
        if (StringUtils.isEmpty(directory.getDigitalSummary())) {
            context.addRecordItem(code, modeCode,
                    "数字摘要检测不通过,交接单据中存档信息包数字摘要为空",
                    TestRecordService.TEST_STATUS_FAIL,
                    "",
                    "数字摘要",
                    directory.getDigitalSummary());
        } else if (directory.getDigitalSummary().equals(fileDigest)) {
            context.addRecordItem(code, modeCode);
        } else {
            context.addRecordItem(code, modeCode,
                    "数字摘要检测不通过",
                    TestRecordService.TEST_STATUS_FAIL,
                    "",
                    "数字摘要",
                    directory.getDigitalSummary());
        }
    }

    private String computeFileDigest(ItemDetectContext context) {
        //拿到接收数据包
        File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
        Assert.notNull(receiveFile, "文件包为空！");
        //计算数据包摘要
        //TODO 根据数字摘要算法计算文件摘要
        return "";
    }
}
