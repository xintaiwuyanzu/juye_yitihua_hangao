package com.dr.archive.statistics.service.impl;

import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.statistics.entity.StatisticsSaveTerm;
import com.dr.archive.statistics.entity.StatisticsSaveTermInfo;
import com.dr.archive.statistics.service.ResourceStatisticsService;
import com.dr.archive.statistics.service.StatisticsSaveTermService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

/**
 * @Author: caor
 * @Date: 2022-06-29 11:25
 * @Description:
 */
@Service
public class StatisticsSaveTermServiceImpl extends DefaultBaseService implements StatisticsSaveTermService {
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    FormNameGenerator formNameGenerator;
    @Autowired
    DataBaseService dataBaseService;
    @Autowired
    StatisticsSaveTermService statisticsSaveTermService;
    @Autowired
    ResourceStatisticsService resourceStatisticsService;

    @Override
    public List<StatisticsSaveTerm> getStatisticsSaveTermList(StatisticsSaveTerm statisticsSaveTerm) {
        return commonMapper.selectByQuery(
                SqlQuery.from(StatisticsSaveTerm.class)
                        .equal(StatisticsSaveTermInfo.FONDCODE, statisticsSaveTerm.getFondCode())
                        .orderByDesc(StatisticsSaveTermInfo.SAVETERM)
        );
    }

    @Override
    public long insertStatisticsSaveTerm(StatisticsSaveTerm statisticsSaveTerm) {
        return commonMapper.insert(statisticsSaveTerm);
    }

    /**
     * TODO 根据当前登录人查询全宗权限
     *
     * @return
     */
    @Override
    public List<StatisticsSaveTerm> countBySaveTerm() {
        return commonMapper.selectByQuery(SqlQuery.from(StatisticsSaveTerm.class, false).column(StatisticsSaveTermInfo.NUM.sum(), StatisticsSaveTermInfo.SAVETERM).groupBy(StatisticsSaveTermInfo.SAVETERM).orderByDesc(StatisticsSaveTermInfo.SAVETERM));
    }

    @Override
    public boolean updateCountBySaveTerm() {
        //清空表
        delete(SqlQuery.from(StatisticsSaveTerm.class));
        //查询所有表单
        List<FormDefinition> formDefinitionList = resourceStatisticsService.getForms("0");
        formDefinitionList.forEach(formDefinition -> {
            FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinition.getId());
            Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
            List<Map> list = commonMapper.selectByQuery(
                    SqlQuery.from(tableInfo, false)
                            .column(tableInfo.getColumn(ArchiveEntity.ID_COLUMN_NAME).count().alias("num"), tableInfo.getColumn(ArchiveEntity.COLUMN_SAVE_TERM).alias("saveTerm"))
                            .groupBy(tableInfo.getColumn(ArchiveEntity.COLUMN_SAVE_TERM))
                            .setReturnClass(Map.class)
            );
            list.forEach(map -> {
                StatisticsSaveTerm statisticsSaveTerm = new StatisticsSaveTerm();
                statisticsSaveTerm.setSaveTerm(map.get("saveTerm") + "");
                statisticsSaveTerm.setNum(null == map.get("num") ? "0" : map.get("num") + "");
                statisticsSaveTermService.insertStatisticsSaveTerm(statisticsSaveTerm);
            });
        });
        return true;
    }
}
