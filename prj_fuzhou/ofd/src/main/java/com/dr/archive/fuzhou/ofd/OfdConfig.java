package com.dr.archive.fuzhou.ofd;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * ofd相关配置
 *
 * @author dr
 */
@ConfigurationProperties("fuzhou.ofd")
public class OfdConfig implements InitializingBean {
    /**
     * 多应用调用云阅读的配置文件参数
     */
    private String extensionApiMode = "archive";
    /**
     * ofd服务ip地址
     */
    private String baseIp;
    /**
     * 转换服务端口
     */
    private Integer apiPort = 8090;
    /**
     * ofd在线阅读端口
     */
    private Integer viewPort = 8088;
    /**
     * 接口访问基础路径
     */
    private String ApiBaseUrl;
    /**
     * 在线阅读访问地址
     */
    private String viewerUrl;
    /**
     * 请求参数aes加密密码
     */
    private String aesPassword = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSfP+4H+XcjTZvDyJC98Ve9Mdr5vSOfE5AMwyZIwBYy3BtrlnUPgQI14ciiuVOTJkxSF0yKoftrw15M7xUMz8HERwfVHGjtfr40q0hCh3G/vnIOX7FdzuAnvqomC+q3MpPYCraNT79Ugz2+5NGrs0ukP7ZzW31+lmsIXvMZZre+wIDAQAB";

    public String getBaseIp() {
        return baseIp;
    }

    public void setBaseIp(String baseIp) {
        this.baseIp = baseIp;
    }

    public Integer getApiPort() {
        return apiPort;
    }

    public void setApiPort(Integer apiPort) {
        this.apiPort = apiPort;
    }

    public Integer getViewPort() {
        return viewPort;
    }

    public void setViewPort(Integer viewPort) {
        this.viewPort = viewPort;
    }

    public String getApiBaseUrl() {
        return ApiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        ApiBaseUrl = apiBaseUrl;
    }

    public String getViewerUrl() {
        return viewerUrl;
    }

    public void setViewerUrl(String viewerUrl) {
        this.viewerUrl = viewerUrl;
    }

    public String getAesPassword() {
        return aesPassword;
    }

    public void setAesPassword(String aesPassword) {
        this.aesPassword = aesPassword;
    }

    public String getExtensionApiMode() {
        return extensionApiMode;
    }

    public void setExtensionApiMode(String extensionApiMode) {
        this.extensionApiMode = extensionApiMode;
    }

    @Override
    public void afterPropertiesSet() {
        if (!StringUtils.hasText(getApiBaseUrl())) {
            //如果没有设置转换服务接口地址，手动设置地址
            setApiBaseUrl(UriComponentsBuilder.newInstance().scheme("http").host(getBaseIp()).port(getApiPort()).path("gsdk-service/").toUriString());
        }
        if (!StringUtils.hasText(getViewerUrl())) {
            //如果没有设置ofd在线阅读地址，手动设置地址
            setViewerUrl(UriComponentsBuilder.newInstance().scheme("http").host(getBaseIp()).port(getViewPort()).path("viewer/pc/index.html").toUriString());
        }
    }
}
