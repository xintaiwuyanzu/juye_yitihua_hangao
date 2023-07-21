package com.dr.archive.common.interaction.service.impl;

import com.dr.archive.common.interaction.entity.OnlineAS;
import com.dr.archive.common.interaction.entity.OnlineASInfo;
import com.dr.archive.common.interaction.service.OnlineASService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OnlineASServiceImpl extends DefaultBaseService<OnlineAS> implements OnlineASService {

    @Autowired
    CommonMapper commonMapper;

    @Override
    public void deleteById(String id, Person person) {
        commonMapper.deleteByQuery(SqlQuery.from(OnlineAS.class).equal(OnlineASInfo.ID, id));
    }

    @Override
    public void checkById(OnlineAS onlineAS, Person person) {
        onlineAS.setUpdateDate(System.currentTimeMillis());
        onlineAS.setUpdatePerson(person.getId());
        onlineAS.setStatus("待审核");
        commonMapper.updateById(onlineAS);
    }
}
