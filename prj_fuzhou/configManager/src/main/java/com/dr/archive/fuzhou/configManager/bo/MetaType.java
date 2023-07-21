package com.dr.archive.fuzhou.configManager.bo;

import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;

/**
 * 元数据类别
 *
 * @author dr
 * @see ConfigManagerClient#getMetadataType()
 */
public class MetaType extends AbstractConfigEntity {
    /**
     * "专有信息元数据",——元数据类型名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
