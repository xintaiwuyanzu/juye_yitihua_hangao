package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 与1-2-2相同
 */
@Component
public class Integrity008Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Integrity008";
    }

    @Override
    public String modeCode() {
        return "2-4-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            if (context.isCodeTest("Authenticity003")) {
                for (TestRecordItem recordItem : context.getItemList()) {
                    if ("Authenticity003".equals(recordItem.getItemCode())) {
                        context.addRecordItem(code(), modeCode(), recordItem.getTestResult(), recordItem.getStatus());
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
