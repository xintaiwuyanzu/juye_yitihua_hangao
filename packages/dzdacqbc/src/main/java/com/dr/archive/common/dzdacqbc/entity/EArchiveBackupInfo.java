package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * 档案备份信息表
 * 档案与存储空间关系
 */
@Table(name = Constants.DZDNCQBC + "archive_backup_info", module = Constants.MODULE_NAME, comment = "档案备份信息表")
public class EArchiveBackupInfo extends BaseStatusEntity<String> {
    @Column(comment = "长期保存档案信息表id")
    private String archiveId;
    @Column(comment = "表单id", length = 100)
    private String formDefinitionId;
    @Column(comment = "数据id", length = 100)
    private String formDataId;
    //和档案最后检测时间对比 判断是否是最新
    @Column(comment = "最后检测时间", type = ColumnType.DATE)
    private long lastTestDate;
    @Column(comment = "存储方案Id", length = 200)
    private String spaceId;

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public long getLastTestDate() {
        return lastTestDate;
    }

    public void setLastTestDate(long lastTestDate) {
        this.lastTestDate = lastTestDate;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

}
