package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 根据政务服务事项管理系统同步到智能归档配置系统中的归档文件规则判定包内文件是否缺少文件
 */
@Component
public class Integrity004Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Integrity004";
    }

    @Override
    public String modeCode() {
        return "2-2-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        context.addRecordItem(code(), modeCode());
    }
}
