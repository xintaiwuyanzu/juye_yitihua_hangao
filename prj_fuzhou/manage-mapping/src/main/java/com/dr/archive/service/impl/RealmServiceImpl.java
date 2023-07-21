package com.dr.archive.service.impl;

import com.dr.archive.entity.Realm;
import com.dr.archive.entity.RealmClass;
import com.dr.archive.entity.RealmClassInfo;
import com.dr.archive.service.RealmClassService;
import com.dr.archive.service.RealmService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-05-25 11:26
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class RealmServiceImpl extends DefaultBaseService<Realm> implements RealmService {

    @Autowired
    RealmClassService realmClassService;

    @Override
    public Object getClassNum() {
        List<Realm> realms = selectList(SqlQuery.from(Realm.class));
        HashMap<Object, Object> map = new HashMap<>();
        realms.forEach(i -> {
            long count = realmClassService.count(SqlQuery.from(RealmClass.class).like(RealmClassInfo.REALMID, i.getId()));
            map.put(i.getId(), count);
        });
        return map;
    }
}
