package com.dr.archive.common.ipWhiteList.service.impl;

import com.dr.archive.common.ipWhiteList.entity.IpWhiteList;
import com.dr.archive.common.ipWhiteList.service.IpWhiteListService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;

/**
 * ip白名单
 */
@Service
public class IpWhiteListServiceImpl extends DefaultBaseService<IpWhiteList> implements IpWhiteListService {

    @Override
    public long insert(IpWhiteList entity) {
        entity.setStatus("0");
        return super.insert(entity);
    }

}
