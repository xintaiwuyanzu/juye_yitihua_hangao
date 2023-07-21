package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.ApproveItemConfig;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.transfer.bo.TransferInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: qiuyf
 * 1-5-1 检测归档信息包文件结构是否符合包结构定义
 */
@Component
public class Authenticity007Service extends AbstractApproveReceiveDetectService {
    @Autowired
    ConfigManagerClient configManagerClient;

    @Override
    public String code() {
        return "Authenticity007";
    }

    @Override
    public String modeCode() {
        return "1-5-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        if (detectEnable(context) && "Online".equals(gdType)) {
            //context.addRecordItem(code());
            //根据接口查询包定义结构
            TransferInfo.TransferDirectory directory = context.getAttribute(ApprovalOnlineReceiveProvider.DIRECTORY_KEY);
            ApproveItemConfig approveItemConfig = new ApproveItemConfig();
            if (StringUtils.isEmpty(directory.getTaskCode())) { //数字化无事项编码数据

                List<ApproveItemConfig> approveItemConfigList = configManagerClient.getConfigDetails(directory.getCategoryCode(), directory.getSocialCode());


                if (approveItemConfigList.size() > 0) {
                    approveItemConfig = approveItemConfigList.get(0);
                }

            } else {
                approveItemConfig = configManagerClient.getConfigDetailsByItemId(
                        directory.getTaskCode(),
                        directory.getSocialCode(),
                        directory.getRegionCode(),
                        directory.getTaskVersion(),
                        directory.getCategoryCode(),
                        directory.getFondsIdentifier());
            }
            if (approveItemConfig == null) {
                context.addRecordItem(code(), modeCode(), "无元数据检测规则!", TestRecordService.TEST_STATUS_FAIL);
            }
        }
    }
}
