package com.dr.archive.detection.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 主从表检测详情
 * <p>
 * id直接复用检测记录表id
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "TEST_RECORD_ITEMS", module = Constants.MODULE_NAME, comment = "四性检测记录详情表")
public class TestRecordItems extends BaseEntity {
    /**
     * 检测详情直接存json
     */
    @Column(type = ColumnType.CLOB)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
