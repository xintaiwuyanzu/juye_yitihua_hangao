package com.dr.archive.kufang.entityfiles.service.impl;

import com.dr.archive.kufang.entityfiles.entity.AnQuan;
import com.dr.archive.kufang.entityfiles.service.AnQuanService;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnQuanServiceImpl extends DefaultBaseService<AnQuan> implements AnQuanService {

    @Autowired
    CommonService commonService;

    @Override
    public long insert(AnQuan entity) {
        entity.setId(null);
        entity.setUpdateDate(null);
        entity.setUpdatePerson(null);
        //需要当前登录人， 登录人组织机构，
        Person person = SecurityHolder.get().currentPerson();
        entity.setCreateDate(System.currentTimeMillis());
        entity.setCreatePerson(person.getId());
        entity.setPersonName(person.getUserName());
        return commonService.insert(entity);
    }
}
