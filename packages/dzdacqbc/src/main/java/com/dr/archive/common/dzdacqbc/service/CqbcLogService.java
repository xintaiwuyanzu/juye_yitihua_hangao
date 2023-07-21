package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.CqbcLogEntity;
import com.dr.framework.common.service.BaseService;

import java.util.Map;

/**
 * @author hyj
 */
public interface CqbcLogService extends BaseService<CqbcLogEntity> {

    //获取 查询添加删除修改 个数。
    Map<String, Long> operation();
}
