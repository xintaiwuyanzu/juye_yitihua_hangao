package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.CqbcClassification;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface ClassificationService extends BaseService<CqbcClassification> {
    /**
     * 设置指定的分类为默认
     *
     * @param cqbcClassification
     */
    void updateDefault(CqbcClassification cqbcClassification);

    List<CqbcClassification> findClassByFondAndCategory(String fondId, String categoryId);

    CqbcClassification getDefaultClassification();
}
