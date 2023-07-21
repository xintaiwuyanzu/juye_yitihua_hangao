package com.dr.archive.examine.service;

import com.dr.archive.examine.entity.JdzdWeight;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface JdzdWeightService extends BaseService<JdzdWeight> {
    JdzdWeight getOneById(String id);

    JdzdWeight getOneByOId(String oId);

    void clear();

    void addAuto();

    List<String> random(Integer num);
}
