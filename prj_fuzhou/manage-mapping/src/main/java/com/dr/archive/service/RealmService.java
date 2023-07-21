package com.dr.archive.service;

import com.dr.archive.entity.Realm;
import com.dr.framework.common.service.BaseService;

/**
 * @author: yang
 * @create: 2022-05-25 11:26
 **/
public interface RealmService extends BaseService<Realm> {
    Object getClassNum();
}
