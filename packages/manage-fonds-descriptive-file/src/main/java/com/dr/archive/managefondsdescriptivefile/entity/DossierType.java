package com.dr.archive.managefondsdescriptivefile.entity;

import com.dr.archive.model.entity.BaseBusinessIdEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.core.security.bo.PermissionResource;

@Table(name = Constants.TABLE_PREFIX + "DOSSIER_TYPE", module = Constants.MODULE_NAME, comment = "分类表")
public class DossierType extends BaseBusinessIdEntity implements PermissionResource {
    @Column(comment = "保管期限")
    private String saveTerm;

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
    }
}
