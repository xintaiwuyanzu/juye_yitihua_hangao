package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.ScanArchiveHistory;
import com.dr.archive.appraisal.service.ScanArchiveHistoryService;
import com.dr.archive.appraisal.vo.ScanArchiveHistoryVO;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScanArchiveHistoryServiceImpl extends DefaultBaseService<ScanArchiveHistory> implements ScanArchiveHistoryService {


    @Override
    public ResultEntity queryScanArchiveHistoryVo(Page<ScanArchiveHistory> scanArchiveHistoryPage) {

        List<ScanArchiveHistoryVO> scanArchiveHistoryVOS = new ArrayList<>();
        scanArchiveHistoryPage.getData().forEach(i->{
            ScanArchiveHistoryVO scanArchiveHistoryVO = new ScanArchiveHistoryVO();
            scanArchiveHistoryVO.setId(i.getId());
            scanArchiveHistoryVO.setOrgId(i.getOrgId());
            scanArchiveHistoryVO.setStatus(i.getStatus());
            if(!StringUtils.isEmpty(i.getStartTime())){
                scanArchiveHistoryVO.setStartTime(Long.parseLong(i.getStartTime()));
            }
            if(!StringUtils.isEmpty(i.getEndTime())){
                scanArchiveHistoryVO.setEndTime(Long.parseLong(i.getEndTime()));
            }

            scanArchiveHistoryVOS.add(scanArchiveHistoryVO);

        });

        //Page<ScanArchiveHistoryVO> page1 = new Page<ScanArchiveHistoryVO>(scanArchiveHistoryPage.getStart(),scanArchiveHistoryPage.getSize(),scanArchiveHistoryPage.getSize(),scanArchiveHistoryVOS);


        return ResultEntity.success(scanArchiveHistoryVOS);
    }
}
