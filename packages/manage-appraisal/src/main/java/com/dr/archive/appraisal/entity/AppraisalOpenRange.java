package com.dr.archive.appraisal.entity;


import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_open_range", module = Constants.MODULE_NAME, comment = "档案鉴定依据规则中的专题")
public class AppraisalOpenRange extends BaseCreateInfoEntity {

    @Column(comment = "结果")
    private String auxiliaryResult;

    @Column(comment = "范围")
    private String openRange;

    @Column(comment = "编码")
    private String code;

    @Column(comment = "优先级")
    private String priority;

    public String getAuxiliaryResult() {
        return auxiliaryResult;
    }

    public void setAuxiliaryResult(String auxiliaryResult) {
        this.auxiliaryResult = auxiliaryResult;
    }

    public String getOpenRange() {
        return openRange;
    }

    public void setOpenRange(String openRange) {
        this.openRange = openRange;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
