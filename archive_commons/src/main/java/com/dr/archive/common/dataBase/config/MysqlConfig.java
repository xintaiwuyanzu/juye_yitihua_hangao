package com.dr.archive.common.dataBase.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lirs
 * @date 2023/4/3 11:30
 */
@Configuration
@ConfigurationProperties("back.mysql")
public class MysqlConfig {
    private String commandUrl;
    private String myIniUrl;
    private String dataBaseName;
    private String fileUrlWin;
    private String fileUrlLinux;

    public String getCommandUrl() {
        return commandUrl;
    }

    public void setCommandUrl(String commandUrl) {
        this.commandUrl = commandUrl;
    }

    public String getMyIniUrl() {
        return myIniUrl;
    }

    public void setMyIniUrl(String myIniUrl) {
        this.myIniUrl = myIniUrl;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getFileUrlWin() {
        return fileUrlWin;
    }

    public void setFileUrlWin(String fileUrlWin) {
        this.fileUrlWin = fileUrlWin;
    }

    public String getFileUrlLinux() {
        return fileUrlLinux;
    }

    public void setFileUrlLinux(String fileUrlLinux) {
        this.fileUrlLinux = fileUrlLinux;
    }
}
