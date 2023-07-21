package com.dr.archive.impexp.entity;

import com.dr.archive.model.entity.BaseYearEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author caor
 * @date 2020/7/31 18:48
 */
@Table(name = Constants.TABLE_PREFIX + "IMP_EXP_SCHEME", module = Constants.MODULE_NAME, comment = "导入导出方案")
public class ImpExpScheme extends BaseYearEntity {
    @Column(comment = "方案类型", length = 50, order = 1)
    private String schemeType;
    @Column(comment = "文件类型", length = 50, order = 2)
    private String fileType;
    @Column(comment = "组织机构id", length = 50, order = 2)
    private String organiseId;
    @Column(comment = "组织机构名称", length = 50, order = 2)
    private String organiseName;
    public String getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(String schemeType) {
        this.schemeType = schemeType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }
}
