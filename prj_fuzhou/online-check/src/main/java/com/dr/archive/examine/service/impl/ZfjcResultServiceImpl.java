package com.dr.archive.examine.service.impl;


import com.dr.archive.examine.entity.ZfjcCheckResult;
import com.dr.archive.examine.entity.ZfjcCheckResultInfo;
import com.dr.archive.examine.service.ZfjcResultService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZfjcResultServiceImpl extends DefaultBaseService<ZfjcCheckResult> implements ZfjcResultService {


    @Override
    public List<ZfjcCheckResult> getCheckResultListByPId(String pId) {
        return commonMapper.selectByQuery(SqlQuery.from(ZfjcCheckResult.class).equal(ZfjcCheckResultInfo.PID, pId));
    }

    @Override
    public List<ZfjcCheckResult> getCheckResultListByPIdAndCateNum(String pId, String num) {
        return commonMapper.selectByQuery(SqlQuery.from(ZfjcCheckResult.class).equal(ZfjcCheckResultInfo.PID, pId));

    }
}
