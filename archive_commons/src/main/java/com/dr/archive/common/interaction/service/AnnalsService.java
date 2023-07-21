package com.dr.archive.common.interaction.service;

import com.dr.archive.common.interaction.entity.Annals;
import com.dr.framework.core.organise.entity.Person;
import org.springframework.stereotype.Service;

@Service
public interface AnnalsService {
    void deleteById(String id, Person person);

    Annals findById(String id);

    void checkById(Annals annals,Person person);

}
