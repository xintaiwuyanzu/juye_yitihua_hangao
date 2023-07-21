package com.dr.archive.manage.dict.entity;

import com.dr.archive.model.entity.BaseYearEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 字典分组
 *
 * @author caor
 * @date 2020/6/2 15:00
 */
@Table(name = Constants.TABLE_PREFIX + "ARCHIVE_DICT_GROUP", module = Constants.MODULE_NAME, comment = "档案字典组")
public class ArchiveDictGroup extends BaseYearEntity {
    @Column(comment = "字典类型", length = 50, order = 1)
    private String dictType;
    @Column(comment = "模块名称", length = 50, order = 2)
    private String moduleName;

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
