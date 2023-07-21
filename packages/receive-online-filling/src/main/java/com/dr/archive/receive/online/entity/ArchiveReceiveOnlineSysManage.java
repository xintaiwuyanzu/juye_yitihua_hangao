package com.dr.archive.receive.online.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * @date: 2022/3/7 11:48
 */
@Table(name = Constants.TABLE_PREFIX + "RECEIVE_SYS_MANAGE", comment = "预归档详情", module = Constants.MODULE_NAME)
public class ArchiveReceiveOnlineSysManage extends BaseStatusEntity<String> {
    @Column(comment = "系统名称", length = 100)
    private String sysName;
    @Column(comment = "系统账号", length = 100)
    private String sysAccount;
    @Column(comment = "系统编码", length = 100)
    private String sysCode;
    @Column(comment = "系统ip", length = 100)
    private String sysIp;
    @Column(comment = "描述", length = 300)
    private String description;
    @Column(comment = "公钥", length = 350)
    private String publicKey;
    @Column(comment = "私钥", length = 350)
    private String privateKey;
    @Column(comment = "系统类型", length = 200)
    private String sysType;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysAccount() {
        return sysAccount;
    }

    public void setSysAccount(String sysAccount) {
        this.sysAccount = sysAccount;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysIp() {
        return sysIp;
    }

    public void setSysIp(String sysIp) {
        this.sysIp = sysIp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }
}
