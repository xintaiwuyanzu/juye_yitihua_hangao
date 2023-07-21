package com.dr.archive.detection.entity;

import com.dr.archive.model.entity.BaseYearEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 四性检测配置
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "TEST_CONFIG", module = Constants.MODULE_NAME, comment = "四性检测配置项表")
public class TestConfig extends BaseYearEntity {

    @Column(comment = "分类名称", length = 100, order = 3)
    private String categoryName;
    /**
     * ‘0’：‘归档环节’ ‘1’：‘移交与接收环节’ ‘2’：‘长期保存环节’ ‘3’：‘数字化环节’
     */
    @Column(comment = "环节", length = 100, order = 4)
    private String linkName;

    @Column(comment = "本配置所有的四性检测编码", type = ColumnType.CLOB)
    private String detectCodes;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getDetectCodes() {
        return detectCodes;
    }

    public void setDetectCodes(String detectCodes) {
        this.detectCodes = detectCodes;
    }
}
