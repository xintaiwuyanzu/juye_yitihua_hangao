package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalSpecial;
import com.dr.archive.appraisal.entity.AppraisalSpecialDetail;
import com.dr.archive.appraisal.entity.AppraisalSpecialInfo;
import com.dr.archive.appraisal.service.AppraisalSpecialDetailService;
import com.dr.archive.appraisal.service.AppraisalSpecialService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppraisalSpecialServiceImpl extends DefaultBaseService<AppraisalSpecial> implements AppraisalSpecialService {

    @Autowired
    AppraisalSpecialDetailService appraisalSpecialDetailService;

    public Map transMap = new HashMap();
    {
        transMap.put("TITLE","标题");
        transMap.put("ARCHIVE_CODE","档号");
        transMap.put("FOND_CODE","全宗号");
        transMap.put("VINTAGES","年度");
        transMap.put("content","原文");

        transMap.put("like","相似");
        transMap.put("notEqual","不相等");
        transMap.put("greaterThan","大于");
        transMap.put("lessThan","小于");
        transMap.put("equal","相等");
        transMap.put("startWith","以关键词开头");
        transMap.put("endWith","以关键词结尾");
        transMap.put("between","在指定范围内");
    }

    @Override
    public long insert(AppraisalSpecial entity) {
        entity.setId(UUIDUtils.getUUID());
        entity.setSpecialRemarks(transSpecialDetail(entity));
        return getCommonService().insert(entity);
    }

    @Override
    public long updateById(AppraisalSpecial entity) {
        appraisalSpecialDetailService.deleteBySpecialId(entity.getId());
        entity.setSpecialRemarks(transSpecialDetail(entity));
        return getCommonService().update(entity);
    }


    @Override
    public void deleteByRules(String rulesId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalSpecial.class).equal(AppraisalSpecialInfo.RULESID,rulesId);
        commonMapper.deleteByQuery(sqlQuery);
    }

    @Override
    public List<AppraisalSpecial> getSpecialByOrgId(String rulesId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalSpecial.class)
                .equal(AppraisalSpecialInfo.RULESID,rulesId);
        List<AppraisalSpecial> specialList =  commonMapper.selectByQuery(sqlQuery);
        for(AppraisalSpecial appraisalSpecial:specialList){
            appraisalSpecial.setSpecialDetailList(appraisalSpecialDetailService.getBySpecialId(appraisalSpecial.getId()));
        }
        return specialList;
    }

    @Override
    public void deleteByBasis(String basisId) {
        delete(SqlQuery.from(AppraisalSpecial.class).equal(AppraisalSpecialInfo.BASISID,basisId));
    }

    public String transSpecialDetail(AppraisalSpecial entity){
        StringBuilder specialRemarks = new StringBuilder();
        String[] specialDetails = entity.getSpecialRemarks().split("#");
        for(String specialDetail:specialDetails){
            String[] tempDetail = specialDetail.split("@");
            AppraisalSpecialDetail appraisalSpecialDetail = new AppraisalSpecialDetail();
            appraisalSpecialDetail.setSpecialId(entity.getId());
            appraisalSpecialDetail.setField(tempDetail[0]);
            appraisalSpecialDetail.setRelation(tempDetail[1]);
            appraisalSpecialDetail.setValue1(tempDetail[2]);
            if(tempDetail.length>3){
                appraisalSpecialDetail.setValue2(tempDetail[3]);
            }
            appraisalSpecialDetailService.insert(appraisalSpecialDetail);
            if(tempDetail[1].contains("than")){
                specialRemarks.append(transMap.get(tempDetail[0])).append(transMap.get(tempDetail[1])).append("[").append(tempDetail[2]).append("],且");
            }else if("between".equals(tempDetail[1])){
                specialRemarks.append(transMap.get(tempDetail[0])).append("在[").append(tempDetail[2]).append("]至[").append(tempDetail[3]).append("]范围内,且");
            }else if("endWith".equals(tempDetail[1])){
                specialRemarks.append(transMap.get(tempDetail[0])).append("以[").append(tempDetail[2]).append("]结尾,且");
            }else if("startWith".equals(tempDetail[1])){
                specialRemarks.append(transMap.get(tempDetail[0])).append("以[").append(tempDetail[2]).append("]开头,且");
            }else{
                specialRemarks.append(transMap.get(tempDetail[0])).append("与[").append(tempDetail[2]).append("]").append(transMap.get(tempDetail[1])).append(",且");
            }
        }
        specialRemarks = new StringBuilder(specialRemarks.substring(0, specialRemarks.length() - 2));
        return specialRemarks.toString();
    }
}
