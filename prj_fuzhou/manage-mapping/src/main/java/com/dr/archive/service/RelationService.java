package com.dr.archive.service;

import com.dr.archive.entity.Relation;
import com.dr.framework.common.service.BaseService;

/**
 * @author: yang
 * @create: 2022-05-30 16:50
 **/
public interface RelationService extends BaseService<Relation> {
    Object getForms();

    void updateRelationNum(Relation entity);
}
