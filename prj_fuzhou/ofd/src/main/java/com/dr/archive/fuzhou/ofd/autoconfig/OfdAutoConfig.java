package com.dr.archive.fuzhou.ofd.autoconfig;

import com.dr.archive.fuzhou.ofd.OfdConfig;
import com.dr.archive.fuzhou.ofd.service.OfdClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * ofd相关的自动配置
 *
 * @author dr
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OfdConfig.class)
@EnableFeignClients(clients = OfdClient.class)
public class OfdAutoConfig {

}
