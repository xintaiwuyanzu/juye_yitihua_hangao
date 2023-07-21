package com.dr.archive.receive.online.service.impl;

import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnlineInfo;
import com.dr.archive.receive.online.service.OnlineStaticsService;
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
public class OnlineStaticsServiceImpl implements OnlineStaticsService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    public Map<Object,Object> statics(String fondCode, long time) {
        List<Person> personList = organisePersonService.getOrganiseDefaultPersons(SecurityHolder.get().currentOrganise().getId());
        List<String> personIdList = personList.stream().map(Person::getId).collect(Collectors.toList());
        //TODO 目前统计当前登录人所在机构下的数量，档案馆是否统计多个单位数量
        SqlQuery<ArchiveBatchReceiveOnline> query = SqlQuery.from(ArchiveBatchReceiveOnline.class, false)
                .in(ArchiveBatchReceiveOnlineInfo.CREATEPERSON, personIdList)
                .column(ArchiveBatchReceiveOnlineInfo.CREATEDATE, ArchiveBatchReceiveOnlineInfo.DETAILNUM.sum())
                .equal(ArchiveBatchReceiveOnlineInfo.FONDCODE, fondCode)
                .groupBy(ArchiveBatchReceiveOnlineInfo.CREATEDATE,ArchiveBatchReceiveOnlineInfo.FONDCODE);

        //根据年度统计，获取开始时间在今年的数据
        if(time!=-1){
            query.greaterThan(ArchiveBatchReceiveOnlineInfo.CREATEDATE, time)
                    .lessThan(ArchiveBatchReceiveOnlineInfo.CREATEDATE, time+31536000000L);
        }
        List<ArchiveBatchReceiveOnline> list = commonMapper.selectByQuery(query);
        Map map = new HashMap();
        if(list.size()>0){
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
        }
        return map;
    }
}
