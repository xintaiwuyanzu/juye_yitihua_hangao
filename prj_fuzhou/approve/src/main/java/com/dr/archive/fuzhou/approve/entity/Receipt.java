package com.dr.archive.fuzhou.approve.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author caor
 * @date 2021-08-17 16:30
 */
@Table(name = Constants.TABLE_PREFIX + "RECEIPT", module = Constants.MODULE_NAME, comment = "归档回执")
public class Receipt extends BaseStatusEntity<String> {
    @Column(comment = "系统编号", length = 50)
    private String systemNum;
    @Column(comment = "系统名称", length = 500)
    private String systemName;
    @Column(comment = "业务主键")
    private String businessId;
    /**
     * code:1 成功 2归档失败 3文件下载失败 4文件解包失败 5四性检测失败 6数字摘要校验失败
     */
    @Column(comment = "回执编码", length = 10)
    private String receiptCode;
    @Column(comment = "结果描述", type = ColumnType.CLOB)
    private String receiptDescription;

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public String getReceiptDescription() {
        return receiptDescription;
    }

    public void setReceiptDescription(String receiptDescription) {
        this.receiptDescription = receiptDescription;
    }
}
