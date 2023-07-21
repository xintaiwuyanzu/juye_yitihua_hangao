package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverInfo;
import com.dr.archive.manage.handover.service.HandOverStatics;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author:
 * @create: 2022-04-27 15:10
 **/
@Service
public class HandOverStaticsServiceImpl implements HandOverStatics {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    FondService fondService;

    @Override
    public Map<Object, Object> statics(String fondCode, long time) {
        Fond fond = null;
        if(!"".equals(fondCode)){
            fond = fondService.findFondByCode(fondCode);
        }
        List<Person> personList = organisePersonService.getOrganiseDefaultPersons(SecurityHolder.get().currentOrganise().getId());
        List<String> personIdList = personList.stream().map(Person::getId).collect(Collectors.toList());
        //TODO 目前统计当前登录人所在机构下的数量，档案馆是否统计多个单位数量
        SqlQuery<ArchiveBatchHandOver> query = SqlQuery.from(ArchiveBatchHandOver.class, false)
                .in(ArchiveBatchHandOverInfo.CREATEPERSON, personIdList)
                .column(ArchiveBatchHandOverInfo.CREATEDATE, ArchiveBatchHandOverInfo.DETAILNUM.sum())
                .equal(ArchiveBatchHandOverInfo.FONDID, fond==null?null:fond.getId())
                .groupBy(ArchiveBatchHandOverInfo.CREATEDATE,ArchiveBatchHandOverInfo.DETAILNUM.sum());

        //根据年度统计，获取开始时间在今年的数据
        if (time !=-1) {
            query.greaterThan(ArchiveBatchHandOverInfo.CREATEDATE, time)
                    .lessThan(ArchiveBatchHandOverInfo.CREATEDATE, time + 31536000000L);
        }
        List<ArchiveBatchHandOver> list = commonMapper.selectByQuery(query);
        Map map = new HashMap();
        //把long类型时间转成日期再获取年份
        list.forEach(i -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(i.getCreateDate()));
            int year = calendar.get(Calendar.YEAR);
            if (map.containsKey(year)){
                map.put(year,(long)map.get(year) + i.getDetailNum());
            }else {
                map.put(year,i.getDetailNum());
            }
        });
        return map;
    }
}
