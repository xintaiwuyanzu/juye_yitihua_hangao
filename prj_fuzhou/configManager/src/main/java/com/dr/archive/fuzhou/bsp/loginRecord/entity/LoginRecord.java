package com.dr.archive.fuzhou.bsp.loginRecord.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseDescriptionEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author lych
 * @date 2023/1/31 上午 10:36
 * @mood happy
 */
@Table(name = Constants.TABLE_PREFIX + "Login_Record_Data", module = Constants.MODULE_NAME, comment = "人员信息表")
public class LoginRecord extends BaseDescriptionEntity<String> {

    @Column(comment = "用户id",length = 50,order = 20)
    private String personId;

    @Column(comment = "最大同时在线数", length = 50,order = 21)
    private Long maxLoginNumber;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Long getMaxLoginNumber() {
        return maxLoginNumber;
    }

    public void setMaxLoginNumber(Long maxLoginNumber) {
        this.maxLoginNumber = maxLoginNumber;
    }
}
