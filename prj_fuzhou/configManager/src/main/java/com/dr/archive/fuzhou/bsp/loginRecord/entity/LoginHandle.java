package com.dr.archive.fuzhou.bsp.loginRecord.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author lych
 * @date 2023/3/20 下午 3:22
 * @mood happy
 */
/*
* 存访问人数总数的。
* */
@Table(name = Constants.TABLE_PREFIX + "Login_Handle_Data", module = Constants.MODULE_NAME, comment = "人员处理类")
public class LoginHandle extends BaseEntity {

    @Column(comment = "列",length = 50,order = 20)
    private String loginKey;
    @Column(comment = "值",length = 50,order = 20)
    private String loginValue;

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getLoginValue() {
        return loginValue;
    }

    public void setLoginValue(String loginValue) {
        this.loginValue = loginValue;
    }
}
