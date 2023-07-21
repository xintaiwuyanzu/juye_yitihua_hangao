package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 检测归档信息包内文件内容是否与智能归档系统中的归档范围一致。
 */
@Component
public class Integrity009Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Integrity009";
    }

    @Override
    public String modeCode() {
        return "2-4-3";
    }

    @Override
    public void detection(ItemDetectContext context) {
        context.addRecordItem(code(), modeCode());
    }
}
