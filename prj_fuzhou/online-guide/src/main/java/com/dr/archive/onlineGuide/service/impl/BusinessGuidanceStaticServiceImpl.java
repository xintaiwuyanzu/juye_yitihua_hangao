package com.dr.archive.onlineGuide.service.impl;

import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchInfo;
import com.dr.archive.onlineGuide.entity.vo.StaticsVo;
import com.dr.archive.onlineGuide.service.BusinessGuidanceStaticService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: caor
 * @Date: 2022-06-12 12:35
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessGuidanceStaticServiceImpl implements BusinessGuidanceStaticService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Override
    public List staticsByYear(String startDate, String endDate, String fondCode) {
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        SqlQuery sqlQuery = SqlQuery.from(BusinessGuidanceBatch.class, false)
                .count(BusinessGuidanceBatchInfo.ID, "count")
                .column(BusinessGuidanceBatchInfo.STATUS)
                .greaterThanEqual(BusinessGuidanceBatchInfo.CREATEDATE, startDate)
                .lessThanEqual(BusinessGuidanceBatchInfo.CREATEDATE, endDate);
        List<Organise> organiseList = null;
        if (!StringUtils.isEmpty(fondCode)){
             organiseList = fondOrganiseService.getOrganiseListByFondCode(fondCode);
        }
        if (!organise.getId().equals("root")){
            if (organiseList==null){
                if (organise.getOrganiseType().equals("dag")){
                    sqlQuery.column(BusinessGuidanceBatchInfo.RECEIVEORGNAME).groupBy(BusinessGuidanceBatchInfo.RECEIVEORGNAME).equal(BusinessGuidanceBatchInfo.RECEIVEORGID, organise.getId());
                }else {
                    sqlQuery.column(BusinessGuidanceBatchInfo.SENDORGNAME).groupBy(BusinessGuidanceBatchInfo.SENDORGNAME).equal(BusinessGuidanceBatchInfo.SENDORGID, organise.getId());
                }
            }else {
                if (organiseList.get(0).getOrganiseType().equals("dag")){
                    sqlQuery.column(BusinessGuidanceBatchInfo.RECEIVEORGNAME).groupBy(BusinessGuidanceBatchInfo.RECEIVEORGNAME).equal(BusinessGuidanceBatchInfo.RECEIVEORGID, organiseList.get(0).getId());
                }else {
                    sqlQuery.column(BusinessGuidanceBatchInfo.SENDORGNAME).groupBy(BusinessGuidanceBatchInfo.SENDORGNAME).equal(BusinessGuidanceBatchInfo.SENDORGID, organiseList.get(0).getId());
                }
            }
        }
        List<Map<String, Object>> list = commonMapper.selectByQuery(sqlQuery.groupBy(BusinessGuidanceBatchInfo.STATUS).orderByDesc(BusinessGuidanceBatchInfo.STATUS).setReturnClass(Map.class));

        List<StaticsVo> staticsVos = new ArrayList();
        list.forEach(map -> {
            StaticsVo vo = new StaticsVo();
            String status = (String) map.get("status");
            if ("0".equals(status)) {
                vo.setType("待指导");
            } else if ("1".equals(status)) {
                vo.setType("指导中");
            } else {
                vo.setType("已指导");
            }
            vo.setQuantity((Long) map.get("count"));
            vo.setOrgName(map.get("sendOrgName") == null ? "" : map.get("sendOrgName").toString());
            staticsVos.add(vo);
        });
        return staticsVos;
    }

    @Override
    public List<BusinessGuidanceBatch> totals() {
        Organise organise = SecurityHolder.get().currentOrganise();
        SqlQuery<BusinessGuidanceBatch> sqlQuery = SqlQuery.from(BusinessGuidanceBatch.class, false)
                .equal(BusinessGuidanceBatchInfo.CREATEORGID, organise.getId())
                .count(BusinessGuidanceBatchInfo.ID, "id", false, false);
        List<BusinessGuidanceBatch> totals = commonMapper.selectByQuery(sqlQuery);
        return totals;
    }
}
