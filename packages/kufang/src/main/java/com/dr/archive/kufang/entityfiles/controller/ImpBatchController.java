package com.dr.archive.kufang.entityfiles.controller;


import com.dr.archive.kufang.entityfiles.entity.*;
import com.dr.archive.kufang.entityfiles.service.ArchiveTypeService;
import com.dr.archive.kufang.entityfiles.service.EntityFilesService;
import com.dr.archive.kufang.entityfiles.service.ImpBatchService;
import com.dr.archive.kufang.entityfiles.service.ImpEntityDetailService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondInfo;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.util.ExcelUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * 实体档案
 *
 * @author cuiyj
 * @Date 2021-08-12
 */
@RestController
@RequestMapping("api/impBatch")
public class ImpBatchController extends BaseServiceController<ImpBatchService, ImpBatch> {
    @Autowired
    CommonService commonService;
    @Autowired
    FondService fondService;
    @Autowired
    EntityFilesService entityFilesService;
    @Autowired
    ArchiveTypeService archiveTypeService;
    @Autowired
    ImpEntityDetailService impEntityDetailService;

    @Override
    protected SqlQuery<ImpBatch> buildPageQuery(HttpServletRequest httpServletRequest, ImpBatch impBatch) {
        return SqlQuery.from(ImpBatch.class)
                .equal(ImpBatchInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId())
                .like(ImpBatchInfo.RECORDNAME, impBatch.getRecordName())
                .equal(ImpBatchInfo.ARCHIVETYPE, impBatch.getArchiveType())
                .orderByDesc(ImpBatchInfo.CREATEDATE);
    }

    @RequestMapping("/impArchive")
    public ResultEntity impArchive(HttpServletRequest request,
                                   MultipartFile file,
                                   @RequestParam(value = "recordName", required = false) String recordName,
                                   @RequestParam(value = "archiveType", required = false) String archiveType,
                                   @RequestParam(value = "isJnwj", required = false) String isJnwj,
                                   @RequestParam(value = "remarks", required = false) String remarks) {
        Organise org = SecurityHolder.get().currentOrganise();
        Fond fond = fondService.selectOne(SqlQuery.from(Fond.class).equal(FondInfo.PARTYID, org.getId()));
        ArchiveType nowType = archiveTypeService.selectById(archiveType);
        ImpBatch impBatch = new ImpBatch();
        impBatch.setOrganiseId(org.getId());
        impBatch.setRecordName(recordName);
        impBatch.setRemarks(remarks);
        impBatch.setId(UUID.randomUUID().toString());
        impBatch.setStatus(StatusEntity.STATUS_ENABLE_STR);
        String[] headers = {"档案名称", "档案编号"};
        if ("jnwj".equals(isJnwj)) {
            headers = new String[]{"档案名称", "档案编号", "案卷档号"};
            ArchiveType type = archiveTypeService.selectOne(SqlQuery.from(ArchiveType.class)
                    .equal(ArchiveTypeInfo.PID, archiveType).equal(ArchiveTypeInfo.ATYPE, "jn"));
            impBatch.setArchiveType(type.getaType());
        } else {
            impBatch.setArchiveType(nowType.getaType());
        }
        LinkedList<Map<String, Object>> maps = new LinkedList<>();
        try {
            maps = ExcelUtils.readExcel(file, headers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.error(e.getMessage());
        }
        impBatch.setQuantity(maps.size() + "");
        int count1 = 0;
        int count2 = 0;
        for (Map m : maps) {
            EntityFiles entityFiles = new EntityFiles();
            entityFiles.setArchiveCode(m.get("档案编号").toString());
            entityFiles.setFondId(fond.getId());
            entityFiles.setFondCode(fond.getCode());
            entityFiles.setArchiveType(archiveType);
            entityFiles.setTitle(m.get("档案名称").toString());
            entityFiles.setOrganiseId(org.getId());
            entityFiles.setBatchId(impBatch.getId());
            if ("jnwj".equals(isJnwj)) {
                entityFiles.setAjdh(m.get("案卷档号").toString());
                entityFiles.setIsJNWJ("1");
                //同步卷内文件的位置信息
                entityFilesService.updatePos(entityFiles.getAjdh(), entityFiles);
            }
            EntityFiles old = commonService.selectOne(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.ARCHIVECODE, m.get("档案编号").toString())
                    .equal(EntityFilesInfo.FONDID, fond.getId()));
            if (old == null) {//根据档案和全宗id去重
                count1++;
                commonService.insert(entityFiles);
                impEntityDetailService.addDetail(entityFiles, impBatch.getId(), "jnwj".equals(isJnwj) ? "jn" : nowType.getaType(), StatusEntity.STATUS_ENABLE_STR);//添加导入详情
            } else {
                count2++;
                impBatch.setStatus(StatusEntity.STATUS_DISABLE_STR);
                impEntityDetailService.addDetail(entityFiles, impBatch.getId(), "jnwj".equals(isJnwj) ? "jn" : nowType.getaType(), StatusEntity.STATUS_DISABLE_STR);//添加导入详情
            }
        }
        impBatch.setQuantitySuc(count1 + "");
        impBatch.setQuantityErr(count2 + "");
        insert(request, impBatch);
        String msg = "共导入" + maps.size() + "个，其中成功" + count1 + "个";
        return ResultEntity.success(msg);
    }
}
