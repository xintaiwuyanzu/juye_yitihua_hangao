package com.dr.archive.manage.fond.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author caor
 * @date 2021-09-03 9:35
 */
@Table(name = Constants.TABLE_PREFIX + "FOND_ORGANISE", module = Constants.MODULE_NAME, comment = "全宗-机构表")
public class FondOrganise extends BaseStatusEntity<String> {
    @Column(comment = "全宗ID", length = 50)
    private String fondId;
    @Column(comment = "全宗CODE", length = 50)
    private String fondCode;
    @Column(comment = "机构ID", length = 500)
    private String organiseId;
    @Column(comment = "机构CODE", length = 500)
    private String organiseCode;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getOrganiseCode() {
        return organiseCode;
    }

    public void setOrganiseCode(String organiseCode) {
        this.organiseCode = organiseCode;
    }
}
