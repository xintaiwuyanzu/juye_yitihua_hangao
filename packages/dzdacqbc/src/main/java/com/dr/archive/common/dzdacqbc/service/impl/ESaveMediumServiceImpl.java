package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.ESaveMedium;
import com.dr.archive.common.dzdacqbc.service.ESaveMediumService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;

@Service
public class ESaveMediumServiceImpl extends DefaultBaseService<ESaveMedium> implements ESaveMediumService {

    @Override
    public long insert(ESaveMedium entity) {
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        return super.insert(entity);
    }
}
