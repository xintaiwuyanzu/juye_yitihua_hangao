package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-06-03 11:13
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_InterfaceManage", module = Constants.MODULE_NAME, comment = "接口管理")
public class InterfaceManage extends BaseStatusEntity<String> {
    @Column(comment = "接口名称")
    private String interfaceName;
    @Column(comment = "接口地址")
    private String interfaceApi;
    @Column(comment = "接口类型")
    private String interfaceType;
    @Column(comment = "序列号（密钥）")
    private String secretKey;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceApi() {
        return interfaceApi;
    }

    public void setInterfaceApi(String interfaceApi) {
        this.interfaceApi = interfaceApi;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
