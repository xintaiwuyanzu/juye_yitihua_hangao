package com.dr.archive.common.interaction.service;

import com.dr.archive.common.interaction.entity.OnlineAS;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;
import org.springframework.stereotype.Service;

@Service
public interface OnlineASService extends BaseService<OnlineAS> {
    void deleteById(String id, Person person);

    void checkById(OnlineAS onlineAS, Person person);
}
