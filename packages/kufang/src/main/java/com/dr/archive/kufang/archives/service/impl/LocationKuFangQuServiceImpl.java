package com.dr.archive.kufang.archives.service.impl;

import com.dr.archive.kufang.archives.entity.LocationKuFangQu;
import com.dr.archive.kufang.archives.entity.LocationKuFangQuInfo;
import com.dr.archive.kufang.archives.service.LocationKuFangQuService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationKuFangQuServiceImpl extends DefaultBaseService<LocationKuFangQu> implements LocationKuFangQuService {

    @Autowired
    PositionService positionService;

    @Override
    public long insert(LocationKuFangQu entity) {
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        return super.insert(entity);
    }

    /**
     * 删除库房下的区
     * @param locId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delArea(String locId) {
        List<LocationKuFangQu> areas = selectList(SqlQuery.from(LocationKuFangQu.class).equal(LocationKuFangQuInfo.KUFANGID, locId));
        for (LocationKuFangQu area : areas) {
            positionService.delPosition(area.getId());
            deleteById(area.getId());
        }
    }
}

