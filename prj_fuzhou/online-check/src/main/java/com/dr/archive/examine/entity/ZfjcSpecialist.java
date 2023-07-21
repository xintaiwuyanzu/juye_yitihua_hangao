package com.dr.archive.examine.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;


/**
 * 单位专家管理
 *
 * @author cuiyj
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "ZfjcSpecialist", module = Constants.COMMON_MODULE_NAME, comment = "单位专家管理")
public class ZfjcSpecialist extends BaseStatusEntity<String> {
    @Column(name = "userId", comment = "用户Id")
    private String userId;

    @Column(name = "user_name", comment = "用户姓名", length = 500)
    private String userName;

    @Column(comment = "机构id")
    private String defaultOrganiseId;

    @Column(comment = "机构名称")
    private String defaultOrganiseName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDefaultOrganiseId() {
        return defaultOrganiseId;
    }

    public void setDefaultOrganiseId(String defaultOrganiseId) {
        this.defaultOrganiseId = defaultOrganiseId;
    }

    public String getDefaultOrganiseName() {
        return defaultOrganiseName;
    }

    public void setDefaultOrganiseName(String defaultOrganiseName) {
        this.defaultOrganiseName = defaultOrganiseName;
    }
}
