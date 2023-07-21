package com.dr.archive.fuzhou.autoconfig;

import com.dr.archive.fuzhou.approve.ApproveCenterConfig;
import com.dr.archive.fuzhou.configManager.ConfigManagerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 福州档案相关的自动配置项
 */
@Configuration
@EnableFeignClients(basePackages = {"com.dr.archive.fuzhou.approve"})
@EnableConfigurationProperties(
        {
                //智能归档配置中心相关配置
                ConfigManagerConfig.class,
                //审批中心相关配置
                ApproveCenterConfig.class
        }
)
public class FuzhouApiAutoConfig {


}
