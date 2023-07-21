package com.dr.archive.common.ipWhiteList.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * ip白名单
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "IP_WHITE_LIST", comment = "ip白名单表", module = Constants.COMMON_MODULE_NAME)
public class IpWhiteList extends BaseStatusEntity<String> {

    @Column(comment = "服务器ip地址")
    private String ip;

    @Column(comment = "电脑mac地址")
    private String mac;

    @Column(comment = "工作单位")
    private String unit;

    @Column(comment = "部门")
    private String dept;

    @Column(comment = "使用人")
    private String username;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
