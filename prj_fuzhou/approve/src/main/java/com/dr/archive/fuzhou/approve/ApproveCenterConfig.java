package com.dr.archive.fuzhou.approve;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 审批中心相关配置
 *
 * @author dr
 */
@ConfigurationProperties("fuzhou.approve")
public class ApproveCenterConfig {
    /**
     * 接口访问基础路径
     */
    private String baseUrl;
    /**
     * 客户端名称
     */
    private String name;
    /**
     * 客户端描述
     */
    private String description;

    public String fullPath(String relativePath) {
        return getBaseUrl() + relativePath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
