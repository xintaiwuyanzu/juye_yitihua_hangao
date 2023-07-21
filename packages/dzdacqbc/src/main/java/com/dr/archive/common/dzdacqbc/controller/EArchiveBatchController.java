package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.annotation.CqbcLog;
import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.common.dzdacqbc.entity.EArchiveBatch;
import com.dr.archive.common.dzdacqbc.entity.EArchiveBatchInfo;
import com.dr.archive.common.dzdacqbc.entity.EArchiveInfo;
import com.dr.archive.common.dzdacqbc.service.EArchiveBatchService;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 出入库批次
 */
@RestController
@RequestMapping("/api/earchive/batch")
public class EArchiveBatchController extends BaseServiceController<EArchiveBatchService, EArchiveBatch> {

    @Autowired
    EArchiveBatchService eArchiveBatchService;
    @Autowired
    EArchiveService eArchiveService;

    @RequestMapping("addToBatch")
    @CqbcLog("添加到批次")
    public ResultEntity addToCar(EArchive eArchive, @Current Person person) {
        EArchiveBatch archiveBatch = service.addToBatch(eArchive, person);
        if("1".equals(eArchive.getModelType())){//案卷类型 查询卷内文件
            List<EArchive> archiveList = eArchiveService.selectList(SqlQuery.from(EArchive.class)
                    .equal(EArchiveInfo.AJDH, eArchive.getArchiveCode()));
            for (EArchive jnarchive : archiveList) {
                service.addToBatch(jnarchive, person);
            }
        }
        if (StringUtils.isEmpty(archiveBatch)) {
            return ResultEntity.error("已添加至最新档案车，请在出入库模块完成出库！");
        }
        return ResultEntity.success(archiveBatch);
    }

    //提交到数据维护客户端
    @RequestMapping("addDownLoad")
    public ResultEntity addDownLoad(String batchId) {
        return ResultEntity.success(service.addDownLoad(batchId));
    }

    @Override
    protected SqlQuery<EArchiveBatch> buildPageQuery(HttpServletRequest request, EArchiveBatch entity) {
        return SqlQuery.from(EArchiveBatch.class)
                .like(EArchiveBatchInfo.BATCHNAME, entity.getBatchName())
                .equal(EArchiveBatchInfo.BATCHTYPE, entity.getBatchType())
                .equal(EArchiveBatchInfo.CREATEPERSON, SecurityHolder.get().currentPerson().getId())
                .orderByDesc(EArchiveBatchInfo.CREATEDATE);
    }

    @RequestMapping("updateById")
    public ResultEntity updateById(String id) {
        EArchiveBatch eArchive = service.selectById(id);
        eArchive.setDetailNum(eArchive.getDetailNum() - 1);
        eArchive.setUpdateDate(System.currentTimeMillis());
        String su = "asd";
        return ResultEntity.success(eArchiveBatchService.updateNotNull(eArchive, su));
    }
}
