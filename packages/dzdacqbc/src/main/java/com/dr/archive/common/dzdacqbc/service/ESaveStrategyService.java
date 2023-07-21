package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ESaveStrategy;
import com.dr.archive.manage.category.entity.Category;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface ESaveStrategyService extends BaseService<ESaveStrategy> {

    ESaveStrategy dealData(ESaveStrategy strategy);

    List<Category> getCateByBind(String fondId);

}
