package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import org.springframework.stereotype.Component;

/**
 * @author: qiuyf
 * 1-5-2 通过1-1-1分配加密密钥，归档信息包在网络传输过程中使用sm4加密算法加密保证传输过程数据安全。系统接收到归档信息包后按照同样的算法解压归档信息包，从而保证归档信息包的一致性。
 */
@Component
public class Authenticity008Service extends AbstractApproveReceiveDetectService {
    @Override
    public String code() {
        return "Authenticity008";
    }

    @Override
    public String modeCode() {
        return "1-5-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            context.addRecordItem(code(), modeCode());
        }
    }
}
