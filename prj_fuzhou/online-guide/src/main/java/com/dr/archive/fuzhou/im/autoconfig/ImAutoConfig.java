package com.dr.archive.fuzhou.im.autoconfig;

import com.dr.archive.fuzhou.im.ImConfig;
import com.dr.archive.fuzhou.im.service.WordProClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * ocr相关配置
 *
 * @author dr
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ImConfig.class)
@EnableFeignClients(clients = {WordProClient.class})
public class ImAutoConfig {
}
