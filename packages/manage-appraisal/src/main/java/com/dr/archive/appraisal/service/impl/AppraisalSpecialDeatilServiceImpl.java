package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalSpecialDetail;
import com.dr.archive.appraisal.entity.AppraisalSpecialDetailInfo;
import com.dr.archive.appraisal.service.AppraisalSpecialDetailService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisalSpecialDeatilServiceImpl extends DefaultBaseService<AppraisalSpecialDetail> implements AppraisalSpecialDetailService {

    @Override
    public void deleteBySpecialId(String specialId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalSpecialDetail.class).equal(AppraisalSpecialDetailInfo.SPECIALID,specialId);
        commonMapper.deleteByQuery(sqlQuery);
    }

    @Override
    public List<AppraisalSpecialDetail> getBySpecialId(String specialId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalSpecialDetail.class).equal(AppraisalSpecialDetailInfo.SPECIALID,specialId);
        return commonMapper.selectByQuery(sqlQuery);
    }
}
