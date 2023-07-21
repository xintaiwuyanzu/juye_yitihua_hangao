package com.dr.archive.model.entity;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;

import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_FOND_CODE;

/**
 * 抽象全宗关联实体类
 *
 * @author dr
 */
public class AbstractFondRelateEntity extends BaseCreateInfoEntity {
    /**
     * 冗余关联字段  全宗
     */
    @Column(comment = "全宗名称", length = 500)
    private String fondName;
    @Column(name = COLUMN_FOND_CODE, comment = "全宗号")
    private String fondCode;

    /**
     * 绑定全宗信息
     *
     * @param fond
     */
    public void bindFondInfo(Fond fond) {
        if (fond != null) {
            setFondCode(fond.getCode());
            setFondName(fond.getName());
        }
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }
}
