package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 与1-3-2相同
 */
@Component
public class Integrity002Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Integrity002";
    }

    @Override
    public String modeCode() {
        return "2-1-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            if (context.isCodeTest("Authenticity005")) {
                for (TestRecordItem recordItem : context.getItemList()) {
                    if ("Authenticity005".equals(recordItem.getItemCode())) {
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
