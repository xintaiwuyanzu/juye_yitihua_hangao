package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 归档信息包在归档过程的安全实现方式与1-5-2相同，在保存过程中主要依托云平台定期检测并反馈结果。
 */
@Component
public class Security003Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Security003";
    }

    @Override
    public String modeCode() {
        return "4-3-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            if (context.isCodeTest("Authenticity008")) {
                for (TestRecordItem recordItem : context.getItemList()) {
                    if ("Authenticity008".equals(recordItem.getItemCode())) {
                        context.addRecordItem(code(), modeCode(), recordItem.getTestResult(), recordItem.getStatus());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
