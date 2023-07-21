package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 1-4-1 方式与1-3-2相同
 */
@Component
public class Authenticity006Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Authenticity006";
    }

    @Override
    public String modeCode() {
        return "1-4-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
//        if (detectEnable(context)) {
            if (context.isCodeTest("Authenticity005")) {
                for (TestRecordItem recordItem : context.getItemList()) {
                    if ("Authenticity005".equals(recordItem.getItemCode())) {
                        context.addRecordItem(code(), modeCode(), recordItem.getTestResult(), recordItem.getStatus());
                    }
                }
            }
        }
//    }

    @Override
    public int getOrder() {
        return 1;
    }
}
