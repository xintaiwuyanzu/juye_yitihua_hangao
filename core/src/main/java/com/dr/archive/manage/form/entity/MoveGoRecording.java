package com.dr.archive.manage.form.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "MOVE_GO_RECORDING", comment = "移动记录表", module = Constants.MODULE_NAME)
public class MoveGoRecording extends BaseCreateInfoEntity {
    @Column(comment = "原机构名称", length = 50)
    private String organName;
    @Column(comment = "现机构名称", length = 200)
    private String organNameNew;
    @Column(comment = "原文件类型", length = 500)
    private String WJLX;
    @Column(comment = "现文件类型", length = 500)
    private String WJLXNew;

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganNameNew() {
        return organNameNew;
    }

    public void setOrganNameNew(String organNameNew) {
        this.organNameNew = organNameNew;
    }

    public String getWJLX() {
        return WJLX;
    }

    public void setWJLX(String WJLX) {
        this.WJLX = WJLX;
    }

    public String getWJLXNew() {
        return WJLXNew;
    }

    public void setWJLXNew(String WJLXNew) {
        this.WJLXNew = WJLXNew;
    }
}
