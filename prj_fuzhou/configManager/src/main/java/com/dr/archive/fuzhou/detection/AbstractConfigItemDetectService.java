package com.dr.archive.fuzhou.detection;

import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.ItemDetectionService;
import com.dr.archive.fuzhou.configManager.bo.TestItem;
import com.dr.archive.manage.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 福州项目四性检测抽象实现类
 *
 * @author dr
 */
public abstract class AbstractConfigItemDetectService implements ItemDetectionService {
    @Autowired
    protected DetectConfigService detectConfigService;
    /**
     * 四性检测相关配置的key
     */
    protected static final String DETECT_CONFIG_KEY = "$_CONFIG_MANAGER_CONFIG";

    /**
     * 执行的检测编码是否需要检测
     *
     * @param context
     * @return
     */
    protected boolean detectEnable(ItemDetectContext context) {
        boolean result = false;
        List<TestItem> list = detectConfigService.loadDetectConfig(context);
        String code = code();
        if (list!=null){
            for(TestItem testItem: list){
                if (code.equals(testItem.getCode())) {
                    result = true;
                    break;
                }
            }
        }
        return true;
    }


    /**
     * 所有四性检测配置都是一直生效的，因为配置是从浪潮配置系统读取的
     *
     * @param linkType
     * @param category
     * @param year
     * @return
     */
    @Override
    public ConfigType configAble(LinkType linkType, Category category, String year) {
        return ConfigType.ALWAYS;
    }

    public DetectConfigService getDetectConfigService() {
        return detectConfigService;
    }
}
