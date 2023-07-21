package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author Mr.Zhu
 * @date 2022/6/24 - 10:41
 */

@Table(name = Constants.TABLE_PREFIX + "Fond_RelationTable", module = Constants.MODULE_NAME, comment = "全宗关系表")
public class FondRelation extends BaseStatusEntity<String> {

    @Column(comment = "源对象名称")
    private String sourceName;

    @Column(comment = "关系名字")
    private String relationName;

    @Column(comment = "目标对象名称")
    private String targetName;

    @Column(comment = "全宗id")
    private String fondId;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }
}
