package com.dr.archive.kufang.entityfiles.service;


import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.ImpEntityDetail;
import com.dr.archive.kufang.entityfiles.entity.ImpEntityDetailInfo;
import com.dr.archive.kufang.entityfiles.vo.ImpEntityDetailVo;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ImpEntityDetailServiceImpl extends DefaultBaseService<ImpEntityDetail> implements ImpEntityDetailService {

    @Autowired
    ArchiveTypeService archiveTypeService;

    /**
     * 添加实体档案导入详情
     *
     * @param entityFiles
     */
    @Override
    public void addDetail(EntityFiles entityFiles, String batchId, String aType, String status) {
        ImpEntityDetail detail = new ImpEntityDetail();
        detail.setBatchId(batchId);
        detail.setArchiveCode(entityFiles.getArchiveCode());
        detail.setTitle(entityFiles.getTitle());
        detail.setDataId(entityFiles.getId());
        detail.setFondCode(entityFiles.getFondCode());
        detail.setFondId(entityFiles.getFondId());
        detail.setFondName(entityFiles.getFondName());
        detail.setOrgId(SecurityHolder.get().currentOrganise().getId());
        detail.setArchiveType(aType);
        detail.setStatus(status);
        insert(detail);
    }

    /**
     * 获得实体档案导入详情
     */
    @Override
    public Page<ImpEntityDetailVo> getDetailPage(String batchId, String title, String archiveCode,
                                                 String archiveType, int pageIndex, int pageSize) {
        SqlQuery<ImpEntityDetail> sqlQuery = SqlQuery.from(ImpEntityDetail.class);
        if (StringUtils.hasText(batchId)) {
            sqlQuery.equal(ImpEntityDetailInfo.BATCHID, batchId);
        }
        if (StringUtils.hasText(title)) {
            sqlQuery.like(ImpEntityDetailInfo.TITLE, title);
        }
        if (StringUtils.hasText(archiveCode)) {
            sqlQuery.equal(ImpEntityDetailInfo.ARCHIVECODE, archiveCode);
        }
        if (!"jn".equals(archiveType)) {
            sqlQuery.andNew().equal(ImpEntityDetailInfo.ARCHIVETYPE, "aj").or().equal(ImpEntityDetailInfo.ARCHIVETYPE, "wj");
        }
        Page<ImpEntityDetailVo> details = commonMapper.selectPageByQuery(sqlQuery.setReturnClass(ImpEntityDetailVo.class), pageIndex * pageSize, (pageIndex + 1) * pageSize);
        for (ImpEntityDetailVo detail : details.getData()) {
            if ("aj".equals(detail.getArchiveType())) {
                //查询案卷下的卷内文件
                SqlQuery<ImpEntityDetail> sqlC = SqlQuery.from(ImpEntityDetail.class)
                        .like(ImpEntityDetailInfo.ARCHIVECODE, detail.getArchiveCode()).equal(ImpEntityDetailInfo.ARCHIVETYPE, "jn")
                        .equal(ImpEntityDetailInfo.BATCHID, batchId);
                List<ImpEntityDetailVo> child = commonMapper.selectByQuery(sqlC.setReturnClass(ImpEntityDetailVo.class));
                detail.setVoList(child);
            }
        }
        return details;
    }


}
