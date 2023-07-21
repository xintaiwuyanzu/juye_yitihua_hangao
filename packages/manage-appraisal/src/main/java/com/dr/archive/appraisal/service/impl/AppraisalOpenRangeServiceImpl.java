package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalOpenRange;
import com.dr.archive.appraisal.service.AppraisalOpenRangeService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AppraisalOpenRangeServiceImpl extends DefaultBaseService<AppraisalOpenRange> implements AppraisalOpenRangeService {


    @Override
    public HashMap<String, Integer> getResultPriority() {
        List<AppraisalOpenRange> appraisalOpenRangeList = commonMapper.selectAll(AppraisalOpenRange.class);
        HashMap<String, Integer> resultPriority = new HashMap();
        for(AppraisalOpenRange appraisalOpenRange:appraisalOpenRangeList){
            resultPriority.put(appraisalOpenRange.getId(),Integer.parseInt(appraisalOpenRange.getPriority()));
        }
        return resultPriority;
    }
}
