package com.dr.archive.utilization.consult.service.impl;

import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsultInfo;
import com.dr.archive.utilization.consult.service.UtilizationStaticsService;
import com.dr.archive.utilization.consult.vo.UtilizationStaticsVo;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mr.Zhu
 * @date 2022/4/26 - 14:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UtilizationStaticsServiceImpl implements UtilizationStaticsService {
    @Autowired
    OrganisePersonService organisePersonService;

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    SysDictService sysDictService;

    @Override
    public List staticsByYear(String startDate, String endDate) {
        Organise organise = SecurityHolder.get().currentOrganise();
        List<Person> personList = organisePersonService.getOrganiseDefaultPersons(organise.getId());
        List<String> personIdList = personList.stream().map(Person::getId).collect(Collectors.toList());
        SqlQuery sqlQuery = SqlQuery.from(ArchiveBatchConsult.class, false)
                .count(ArchiveBatchConsultInfo.ID, "count")
                .column(ArchiveBatchConsultInfo.USEFOR);
        if (!startDate.isEmpty()&&!endDate.isEmpty()) {
            sqlQuery.greaterThanEqual(ArchiveBatchConsultInfo.CREATEDATE, startDate)
                    .lessThanEqual(ArchiveBatchConsultInfo.CREATEDATE, endDate+ 24 * 3600 * 1000);
        }
        if (personIdList.size() > 0) {
            sqlQuery.in(ArchiveBatchConsultInfo.CREATEPERSON, personIdList);
        }

        List<Map<String, Object>> list = commonMapper.selectByQuery(sqlQuery.groupBy(ArchiveBatchConsultInfo.USEFOR).setReturnClass(Map.class));
        //字典表数据
        Map<String, String> defaultKeyMap = sysDictService.dict("utilize").stream().collect(Collectors.toMap(TreeNode::getId, TreeNode::getLabel));

        List<UtilizationStaticsVo> utilizationStaticsVos = new ArrayList();
        list.forEach(i -> {
            UtilizationStaticsVo vo = new UtilizationStaticsVo();
            vo.setUseFor(defaultKeyMap.get(i.get("useFor")));
            vo.setCount(i.get("count"));
            utilizationStaticsVos.add(vo);
        });
        return utilizationStaticsVos;
    }
}
