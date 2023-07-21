package com.dr.archive.managefondsdescriptivefile.service;

import com.dr.archive.managefondsdescriptivefile.entity.DossierType;
import com.dr.framework.common.service.BaseService;

public interface DossierTypeService extends BaseService<DossierType> {
    /**
     * 根据父类id删除
     *
     * @param fondId
     * @return
     */
    long deleteByFondId(String fondId);

    /**
     * 根据分类编码查询小分类
     *
     * @param managementTypeCode
     * @return
     */
    DossierType getDossierTypeByCode(String managementTypeCode);
}
