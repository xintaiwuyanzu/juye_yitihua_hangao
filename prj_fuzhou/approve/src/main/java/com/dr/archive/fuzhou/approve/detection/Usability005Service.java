package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Enumeration;

/**
 * @author: qiuyf
 * 通过zip压缩率是否超出90%判断归档信息包是否为zip炸弹
 */
@Component
public class Usability005Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Usability005";
    }

    @Override
    public String modeCode() {
        return "3-4-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        if (detectEnable(context) && "Online".equals(gdType)) {
            File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
            try {
                ZipFile zipFile = new ZipFile(receiveFile);

                long ysq = 0; //压缩前总和
                long ysh = 0; //压缩后总和
                Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
                while (entries.hasMoreElements()) {
                    ZipArchiveEntry entry = entries.nextElement();
                    if (!entry.isDirectory()) {
                        ysq += entry.getSize();
                        ysh += entry.getCompressedSize();
                    }
                }
                if (ysq != 0) {
                    BigDecimal ziplv = BigDecimal.valueOf(ysh / ysq);
                    BigDecimal ziplvStu = BigDecimal.valueOf(0.5); //压缩率限定值 先写死

                    NumberFormat percent = NumberFormat.getPercentInstance();
                    percent.setMaximumFractionDigits(2);
                    if (ziplv.compareTo(ziplvStu) < 0) {
                        context.addRecordItem(code(), modeCode(),
                                "zip压缩率小于50%!",
                                TestRecordService.TEST_STATUS_FAIL,
                                "",
                                "压缩率",
                                percent.format(ziplv.setScale(4, RoundingMode.HALF_UP).doubleValue())); //转化为百分比,并保留两位小数
                    } else {
                        context.addRecordItem(code(), modeCode(),
                                "检测通过",
                                TestRecordService.TEST_STATUS_SUCCESS,
                                "",
                                "压缩率",
                                percent.format(ziplv.setScale(4, RoundingMode.HALF_UP).doubleValue()));
                    }
                }

            } catch (Exception ignored) {

            }
        }
    }
}
