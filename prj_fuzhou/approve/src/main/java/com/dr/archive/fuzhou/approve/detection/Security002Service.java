package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 无此项检测（归档数据均依托系统进行检测，不涉及载体检测）
 */
@Component
public class Security002Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Security002";
    }

    @Override
    public String modeCode() {
        return "4-2-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            context.addRecordItem(code(), modeCode());
        }
    }
}
