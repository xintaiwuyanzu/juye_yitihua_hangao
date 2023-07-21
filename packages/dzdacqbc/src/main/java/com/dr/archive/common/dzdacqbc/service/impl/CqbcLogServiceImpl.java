package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.CqbcLogEntity;
import com.dr.archive.common.dzdacqbc.entity.CqbcLogEntityInfo;
import com.dr.archive.common.dzdacqbc.service.CqbcLogService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyj
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CqbcLogServiceImpl extends DefaultBaseService<CqbcLogEntity> implements CqbcLogService {

    @Override
    public Map<String, Long> operation() {

        //根据存在的名词查询数量
        List<CqbcLogEntity> addTo = commonMapper.selectByQuery(SqlQuery.from(CqbcLogEntity.class).like(CqbcLogEntityInfo.CONTENT, "添加").orNew().like(CqbcLogEntityInfo.CONTENT, "新增"));
        List<CqbcLogEntity> query = commonMapper.selectByQuery(SqlQuery.from(CqbcLogEntity.class).like(CqbcLogEntityInfo.CONTENT, "查询"));
        List<CqbcLogEntity> revise = commonMapper.selectByQuery(SqlQuery.from(CqbcLogEntity.class).like(CqbcLogEntityInfo.CONTENT, "修改"));
        List<CqbcLogEntity> delete = commonMapper.selectByQuery(SqlQuery.from(CqbcLogEntity.class).like(CqbcLogEntityInfo.CONTENT, "删除"));
        Map<String, Long> integerMap = new HashMap<>();
        integerMap.put("添加", (long) addTo.size());
        integerMap.put("查询", (long) query.size());
        integerMap.put("修改", (long) revise.size());
        integerMap.put("删除", (long) delete.size());

        return integerMap;
    }
}
