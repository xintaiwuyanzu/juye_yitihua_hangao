package com.dr.archive.common.accessControl.entity;

import com.dr.archive.common.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:03
 */
@Table(name = "data_authority", module = Constants.MODULE_NAME, comment = "数据权限表")
public class DataAuthority extends BaseStatusEntity<String> {

    @Column(comment = "权限编号", length = 100)
    private String accessCode;

    @Column(comment = "权限级别", length = 100)
    private String accessLevel;

    @Column(comment = "级别编码", length = 100)
    private String levelCode;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }
}
