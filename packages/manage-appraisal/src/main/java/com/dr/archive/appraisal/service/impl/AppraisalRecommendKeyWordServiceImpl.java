package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalKeyWord;
import com.dr.archive.appraisal.entity.AppraisalRecommendKeyWord;
import com.dr.archive.appraisal.entity.AppraisalRecommendKeyWordInfo;
import com.dr.archive.appraisal.service.AppraisalRecommendKeyWordService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;

@Service
public class AppraisalRecommendKeyWordServiceImpl extends DefaultBaseService<AppraisalRecommendKeyWord> implements AppraisalRecommendKeyWordService {


    @Override
    public long insert(AppraisalRecommendKeyWord entity) {
        entity.setCreatePersonName(SecurityHolder.get().currentPerson().getUserName());
        entity.setStatus("0");
        return super.insert(entity);
    }

    @Override
    public ResultEntity adopt(AppraisalRecommendKeyWord appraisalRecommendKeyWord) {
        updateById(appraisalRecommendKeyWord);
        AppraisalKeyWord appraisalKeyWord = new AppraisalKeyWord();
        appraisalKeyWord.setKeyWord(appraisalRecommendKeyWord.getKeyWord());
        appraisalKeyWord.setRulesId(appraisalRecommendKeyWord.getRuleId());
        getCommonService().insert(appraisalKeyWord);
        return ResultEntity.success();
    }

    @Override
    public ResultEntity countToDo() {
        SqlQuery sqlQuery  = SqlQuery.from(AppraisalRecommendKeyWord.class,false)
                .count(AppraisalRecommendKeyWordInfo.ID)
                .equal(AppraisalRecommendKeyWordInfo.STATUS,"0");
        return ResultEntity.success(commonMapper.countByQuery(sqlQuery));
    }
}
