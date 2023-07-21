package com.dr.archive.fuzhou.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.fuzhou.configManager.bo.TestItem;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.ApproveItemConfig;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import feign.RetryableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 福州项目四性检测方案加载类
 *
 * @author dr
 */
@Component
public class DetectConfigService {
    /**
     * 单次检测配置只需要加载一次即可，检测配置信息缓存在检测上下文中
     * 下面的变量是上下文中缓存的主键
     */
    public static final String CONFIG_KEY = "$fz_detect_config";

    @Autowired
    ConfigManagerClient configManagerClient;

    public List<TestItem> loadDetectConfig(ItemDetectContext context) {
        if (!context.containsKey(CONFIG_KEY)) {
            //TODO
            try{
                List<TestItem> testItems = configManagerClient.getTestItems(0, 0, context.getFond().getCode());
                context.addAttribute(CONFIG_KEY, testItems);
            }catch (RetryableException e){
                System.out.println("获取检测方案失败");
            }
        }
        return context.getAttribute(CONFIG_KEY);
    }

    public ApproveItemConfig getItemConfig(String taskCode, String socialCode, String regionCode,String taskVersion,String CategoryCode,String fondsIdentifier){
        ApproveItemConfig approveItemConfig = configManagerClient.getConfigDetailsByItemId(taskCode, socialCode, regionCode, taskVersion, CategoryCode, fondsIdentifier);
        return approveItemConfig;
    }

}
