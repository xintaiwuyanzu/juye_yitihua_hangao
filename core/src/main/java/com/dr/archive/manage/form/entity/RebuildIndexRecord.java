package com.dr.archive.manage.form.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * @author lirs
 * @date 2023/3/28 9:26
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "rebuildIndex_record", module = Constants.COMMON_MODULE_NAME, comment = "重建索引记录表")
public class RebuildIndexRecord extends BaseStatusEntity<String> {
    /**
     * status: 0 失败  ，1 成功
     */

    /**
     * 0：复制重建，1：删除重建
     */
    @Column(comment = "重建类型", length = 100)
    private String rebuildType;
    @Column(comment = "表单编码", length = 100)
    private String formCode;
    @Column(comment = "表单名称", length = 100)
    private String formName;
    @Column(comment = "操作人", length = 100)
    private String createPersonName;
    @Column(comment = "开始时间", length = 100)
    private Long startTime;
    @Column(comment = "结束时间", length = 100)
    private Long endTime;

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getRebuildType() {
        return rebuildType;
    }

    public void setRebuildType(String rebuildType) {
        this.rebuildType = rebuildType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
