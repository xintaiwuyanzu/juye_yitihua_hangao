package com.dr.archive.examine.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * 档案类别管理
 *
 * @author cuiyj
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "JdzdCategory", module = Constants.COMMON_MODULE_NAME, comment = "档案类别管理")
public class JdzdCategory extends BaseStatusEntity<String> {

    public static final String largeCategory_one = "贯彻实施档案法规监督检查";
    public static final String largeCategory_two = "对档案工作的监督检查";
    public static final String largeCategory_three = "依法接收档案执法检查";

    @Column(comment = "大类别", length = 100)
    private String largeCategory;

    @Column(comment = "大类别描述", length = 100)
    private String largeCategoryValue;

    @Column(comment = "小类别", length = 100)
    private String smallCategory;

    @Column(comment = "比率", length = 100)
    private String rate;
    @Column(comment = "机构id")
    private String organiseId;
    @Column(comment = "机构名称")
    private String organiseName;
    @Column(comment = "分数")
    private String fraction;
    @Column(comment = "检查细则与评分说明", type = ColumnType.CLOB)
    private String remark;

    public String getLargeCategoryValue() {
        return largeCategoryValue;
    }

    public void setLargeCategoryValue(String largeCategoryValue) {
        this.largeCategoryValue = largeCategoryValue;
    }

    public String getLargeCategory() {
        return largeCategory;
    }

    public void setLargeCategory(String largeCategory) {
        this.largeCategory = largeCategory;
    }

    public String getSmallCategory() {
        return smallCategory;
    }

    public void setSmallCategory(String smallCategory) {
        this.smallCategory = smallCategory;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
