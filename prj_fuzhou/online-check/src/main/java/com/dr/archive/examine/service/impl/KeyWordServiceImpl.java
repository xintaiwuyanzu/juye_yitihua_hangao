package com.dr.archive.examine.service.impl;

import com.dr.archive.examine.service.KeyWordService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-03-17 15:58
 * @Description:
 */
@Service
public class KeyWordServiceImpl implements KeyWordService {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    public List<Organise> getOrganise() {

        Organise organise = SecurityHolder.get().currentOrganise();
        List<Organise> organises = organisePersonService.getChildrenOrganiseList(organise.getId());
        if (!organise.getId().equals("root")) {
            organises.add(organise);
        }

        return organises;
    }
}

