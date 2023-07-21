package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 1-3-1 方式与1-1-2相同
 *
 * @author: qiuyf
 */
@Component
public class Authenticity004Service extends AbstractApproveReceiveDetectService {
    @Autowired
    FileDigestDetectService fileDigestDetectService;

    @Override
    public String code() {
        return "Authenticity004";
    }

    @Override
    public String modeCode() {
        return "1-3-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            if (context.isCodeTest("Authenticity001")) {
                for (TestRecordItem recordItem : context.getItemList()) {
                    if ("Authenticity001".equals(recordItem.getItemCode())) {
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
