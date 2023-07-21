package com.dr.archive.common;

import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.core.orm.support.mybatis.spring.boot.autoconfigure.EnableAutoMapper;
import com.dr.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.charset.StandardCharsets;


/**
 * 系统启动入口
 *
 * @author dr
 */
@SpringBootApplication(scanBasePackages = {Constants.PACKAGE_NAME, "com.dr.archive"}, scanBasePackageClasses = Application.class)
@EnableAutoMapper(basePackages = {Constants.PACKAGE_NAME, "com.dr.archive", "com.dr.process"})
public class Application implements WebMvcConfigurer {
    static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    CommonFileConfig commonFileConfig;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("启动成功！");
    }

    /**
     * httpClient
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(10000);
        // 设置超时
        requestFactory.setConnectTimeout(10000);
        //利用复杂构造器可以实现超时设置，内部实际实现为 HttpClient
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        for (HttpMessageConverter messageConverter : restTemplate.getMessageConverters()) {
            if (messageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) messageConverter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
        return restTemplate;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("默认文件上传路径：" + commonFileConfig.getRootDirFullPath() + File.separator);
        //上传图片访问路径
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + commonFileConfig.getRootDirFullPath() + File.separator);
    }

}
