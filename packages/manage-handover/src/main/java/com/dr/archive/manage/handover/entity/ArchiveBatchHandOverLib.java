package com.dr.archive.manage.handover.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "HAND_OVER_LIB", comment = "档案到期移交库", module = Constants.MODULE_NAME)
public class ArchiveBatchHandOverLib extends AbstractArchiveBatch {

    @Column(comment = "档案年度")
    private String archiveYear;

    @Column(comment = "延期年度")
    private String delayYear;

    @Column(comment = "全宗Id")
    private String fondId;
    @Column(comment = "全宗名称")
    private String fondName;
    @Column(comment = "机构Id")
    private String organiseId;

    public String getArchiveYear() {
        return archiveYear;
    }

    public void setArchiveYear(String archiveYear) {
        this.archiveYear = archiveYear;
    }

    public String getDelayYear() {
        return delayYear;
    }

    public void setDelayYear(String delayYear) {
        this.delayYear = delayYear;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }
}
