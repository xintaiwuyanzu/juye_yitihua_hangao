package com.dr.archive.kufang.entityfiles.service;

import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.ImpEntityDetail;
import com.dr.archive.kufang.entityfiles.vo.ImpEntityDetailVo;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;

public interface ImpEntityDetailService extends BaseService<ImpEntityDetail> {

    void addDetail(EntityFiles entityFiles, String batchId, String aType, String status);

    Page<ImpEntityDetailVo> getDetailPage(String batchId, String title, String archiveCode, String archiveType, int pageIndex, int pageSize);
}
