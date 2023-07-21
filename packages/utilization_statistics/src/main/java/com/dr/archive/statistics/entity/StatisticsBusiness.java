package com.dr.archive.statistics.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-06-29 15:36
 * @Description: 业务类型统计
 */
@Table(name = Constants.TABLE_PREFIX + "STATISTICS_BUSINESS", module = Constants.MODULE_NAME, comment = "保管期限统计")
public class StatisticsBusiness {
    @Column(comment = "全宗id")
    private String fondId;
    @Column(comment = "全宗编码")
    private String fondCode;
    @Column(comment = "全宗名称")
    private String fondName;
    @Column(comment = "业务类型")
    private String businessType;
    @Column(comment = "总数")
    private long allNum;
    @Column(comment = "办理中数量")
    private long inProcessNum;
    @Column(comment = "通过数量")
    private long passNum;
    @Column(comment = "不通过数量")
    private long unPassNum;

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

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public long getAllNum() {
        return allNum;
    }

    public void setAllNum(long allNum) {
        this.allNum = allNum;
    }

    public long getInProcessNum() {
        return inProcessNum;
    }

    public void setInProcessNum(long inProcessNum) {
        this.inProcessNum = inProcessNum;
    }

    public long getPassNum() {
        return passNum;
    }

    public void setPassNum(long passNum) {
        this.passNum = passNum;
    }

    public long getUnPassNum() {
        return unPassNum;
    }

    public void setUnPassNum(long unPassNum) {
        this.unPassNum = unPassNum;
    }
}
