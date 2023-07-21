package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 无
 */
@Component
public class Usability004Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Usability004";
    }

    @Override
    public String modeCode() {
        return "3-3-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            //context.addRecordItem(code(), modeCode());
        }
    }
}
