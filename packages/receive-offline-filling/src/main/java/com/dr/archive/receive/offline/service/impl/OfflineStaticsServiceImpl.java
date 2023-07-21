package com.dr.archive.receive.offline.service.impl;

import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineInfo;
import com.dr.archive.receive.offline.service.OfflineStaticsService;
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
public class OfflineStaticsServiceImpl implements OfflineStaticsService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    public Map<Object,Object> statics(String fondCode, long time) {
        List<Person> personList = organisePersonService.getOrganiseDefaultPersons(SecurityHolder.get().currentOrganise().getId());
        List<String> personIdList = personList.stream().map(Person::getId).collect(Collectors.toList());
        //TODO 目前统计当前登录人所在机构下的数量，档案馆是否统计多个单位数量
        SqlQuery<ArchiveBatchReceiveOffline> query = SqlQuery.from(ArchiveBatchReceiveOffline.class, false)
                .in(ArchiveBatchReceiveOfflineInfo.CREATEPERSON, personIdList)
                .column(ArchiveBatchReceiveOfflineInfo.CREATEDATE, ArchiveBatchReceiveOfflineInfo.DETAILNUM.sum());

        //根据年度统计，获取开始时间在今年的数据
        if(time!=-1){
            query.greaterThan(ArchiveBatchReceiveOfflineInfo.CREATEDATE, time)
                    .lessThan(ArchiveBatchReceiveOfflineInfo.CREATEDATE, time+31536000000L);
        }
        query.groupBy(ArchiveBatchReceiveOfflineInfo.CREATEDATE, ArchiveBatchReceiveOfflineInfo.DETAILNUM.sum());
        List<ArchiveBatchReceiveOffline> list = commonMapper.selectByQuery(query);
        Map map = new HashMap();
        //把long类型时间转成日期再获取年份
        list.forEach(i->{
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
