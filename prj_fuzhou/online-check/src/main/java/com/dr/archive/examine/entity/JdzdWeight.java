package com.dr.archive.examine.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * 单位权重管理
 *
 * @author cuiyj
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "JdzdWeight", module = Constants.COMMON_MODULE_NAME, comment = "单位权重管理")
public class JdzdWeight extends BaseStatusEntity<String> {

    public static final Long defaultWeight=5L;

    @Column(comment = "organiseId", length = 100)
    private String organiseId;

    @Column(comment = "organiseCode", length = 100)
    private String organiseCode;

    @Column(comment = "单位名", length = 200)
    private String organiseName;

    @Column(comment = "权重", length = 50)
    private Long weight;

    public String getOrganiseCode() {
        return organiseCode;
    }

    public void setOrganiseCode(String organiseCode) {
        this.organiseCode = organiseCode;
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

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }



}
