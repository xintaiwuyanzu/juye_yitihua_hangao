package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 通过解压并提取归档信息包内容方式判断归档信息包是否可用
 */
@Component
public class Usability006Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Usability006";
    }

    @Override
    public String modeCode() {
        return "3-4-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            //若档案包内的电子文件元数据文件可以被正常访问,那么档案包可用
            if (context.isCodeTest("Usability001")) {
                for (TestRecordItem recordItem : context.getItemList()) {
                    if ("Usability001".equals(recordItem.getItemCode())) {
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
