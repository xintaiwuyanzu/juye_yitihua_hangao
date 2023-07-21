package com.dr.archive.managefondsdescriptivefile.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseDescriptionEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.core.security.bo.PermissionResource;

@Table(name = Constants.TABLE_PREFIX + "MANAGEMENT_TYPE", module = Constants.MODULE_NAME, comment = "全宗卷分类")
public class ManagementType extends BaseDescriptionEntity<String> implements PermissionResource {
    @Column(comment = "类型", length = 10, order = 3)
    private Integer fondType;
    /**
     * 关联组织机构，可以为空
     */
    @Column(comment = "机构ID", length = 50, order = 4)
    private String partyId;
    /**
     * 关联组织机构，可以为空
     */
    @Column(comment = "机构名称", length = 500, order = 5)
    private String partyName;
    @Column(comment = "默认值", length = 100, order = 8)
    private String defaultVal;
    @Column(comment = "是否可用", length = 100, order = 8)
    private boolean enabled;

    public Integer getFondType() {
        return fondType;
    }
    public void setFondType(Integer fondType) {
        this.fondType = fondType;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
