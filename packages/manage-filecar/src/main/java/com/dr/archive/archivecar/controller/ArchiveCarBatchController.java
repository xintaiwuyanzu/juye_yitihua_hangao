package com.dr.archive.archivecar.controller;

import com.dr.archive.archivecar.bo.ArchiveCarDetailTag;
import com.dr.archive.archivecar.bo.ArchiveCarType;
import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.entity.ArchiveCarBatchInfo;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.archivecar.service.ArchiveCarBatchService;
import com.dr.archive.archivecar.service.ArchiveCarDetailService;
import com.dr.archive.archivecar.service.ArchiveCarTypeProvider;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 档案车批次controller
 *
 * @Author: caor
 * @Date: 2022-01-18 14:31
 * @Description:
 */
@RestController
@RequestMapping({"${common.api-path:/api}/archiveCarBatch"})
public class ArchiveCarBatchController extends BaseServiceController<ArchiveCarBatchService, ArchiveCarBatch> {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveCarDetailService archiveCarDetailService;
    @Autowired
    List<ArchiveCarTypeProvider> archiveCarTypeProviders;

    /**
     * 处理查询条件
     *
     * @param httpServletRequest
     * @param archiveCarBatch
     * @return
     */
    @Override
    protected SqlQuery<ArchiveCarBatch> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveCarBatch archiveCarBatch) {
        Person person = getUserLogin(httpServletRequest);
        SqlQuery<ArchiveCarBatch> sqlQuery = SqlQuery.from(ArchiveCarBatch.class)
                .equal(ArchiveCarBatchInfo.CREATEPERSON, person.getId())
                .like(ArchiveCarBatchInfo.BATCHNAME, archiveCarBatch.getBatchName())
                .equal(ArchiveCarBatchInfo.BATCHTYPE, archiveCarBatch.getBatchType())
                .orderByDesc(ArchiveCarBatchInfo.CREATEDATE);
        List<ArchiveCarBatch> archiveCarBatches = commonMapper.selectByQuery(sqlQuery);
        archiveCarBatches.forEach(item -> {
            List<ArchiveCarDetail> archiveCarDetails = commonMapper.selectByQuery(SqlQuery.from(ArchiveCarDetail.class).equal(ArchiveCarDetailInfo.BATCHID, item.getId()));
            if (item.getDetailNum()!=archiveCarDetails.size()){
                item.setDetailNum(archiveCarDetails.size());
                commonMapper.updateById(item);
            }
        });
        return sqlQuery;
    }

    /**
     * 获取系统所支持的左右默认档案车类型
     *
     * @param person  登陆人
     * @param withTag 是否包含标签
     * @return
     */
    @PostMapping("/archiveCarType")
    public ResultListEntity<ArchiveCarType> archiveCarType(@Current Person person, boolean withTag) {
        return ResultListEntity.success(
                archiveCarTypeProviders.stream()
                        .map(t -> new ArchiveCarType(t, person.getId(), withTag))
                        .collect(Collectors.toList())
        );
    }


    /**
     * 获取指定类型档案车的标签列表
     *
     * @param person  当前登录人
     * @param carType 档案车类型
     * @return
     */
    @PostMapping("/archiveCarTag")
    public ResultListEntity<ArchiveCarDetailTag> archiveCarTag(@Current Person person, @Nullable String carType, boolean withDynamic) {
        List<ArchiveCarDetailTag> tagList = new ArrayList<>();
        boolean isSingle = StringUtils.hasText(carType);
        for (ArchiveCarTypeProvider archiveCarTypeProvider : archiveCarTypeProviders) {
            if (isSingle) {
                if (archiveCarTypeProvider.getType().equalsIgnoreCase(carType)) {
                    loadArchiveTag(tagList, archiveCarTypeProvider, withDynamic, person);
                    break;
                }
            } else {
                loadArchiveTag(tagList, archiveCarTypeProvider, withDynamic, person);
            }
        }
        return ResultListEntity.success(tagList);
    }

    private void loadArchiveTag(List<ArchiveCarDetailTag> tagList, ArchiveCarTypeProvider archiveCarTypeProvider, boolean withDynamic, Person person) {
        if (archiveCarTypeProvider.isTagStatic()) {
            tagList.addAll(archiveCarTypeProvider.getDetailTags(person.getId()));
        } else {
            if (withDynamic) {
                tagList.addAll(archiveCarTypeProvider.getDetailTags(person.getId()));
            }
        }
    }
    @PostMapping("/updateCarType")
    public ResultListEntity updateCarType(String batchId){
        ArchiveCarBatch archiveCarBatch = commonMapper.selectOneByQuery(SqlQuery.from(ArchiveCarBatch.class).equal(ArchiveCarBatchInfo.ID, batchId));
        archiveCarBatch.setStatus("1");
        commonMapper.updateById(archiveCarBatch);
        return ResultListEntity.success();
    }
}
