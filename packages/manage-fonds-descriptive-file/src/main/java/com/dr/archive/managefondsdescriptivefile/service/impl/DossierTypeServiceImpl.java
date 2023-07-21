package com.dr.archive.managefondsdescriptivefile.service.impl;

import com.dr.archive.managefondsdescriptivefile.entity.DossierType;
import com.dr.archive.managefondsdescriptivefile.entity.DossierTypeInfo;
import com.dr.archive.managefondsdescriptivefile.service.DossierTypeService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

@Service
public class DossierTypeServiceImpl extends DefaultBaseService<DossierType> implements DossierTypeService {

    @Override
    public long deleteByFondId(String fondId) {
        SqlQuery<DossierType> sqlQuery = SqlQuery.from(DossierType.class).equal(DossierTypeInfo.BUSINESSID, fondId);
        long delete = delete(sqlQuery);
        return delete;
    }

    @Override
    public DossierType getDossierTypeByCode(String managementTypeCode) {
        return commonMapper.selectOneByQuery(SqlQuery.from(DossierType.class).equal(DossierTypeInfo.CODE, managementTypeCode));
    }
}
