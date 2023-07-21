package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.*;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.util.DateTimeUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ESaveBackBatchServiceImpl extends DefaultBaseService<ESaveBackBatch> implements ESaveBackBatchService {

    @Autowired
    FondService fondService;
    @Autowired
    ESaveSpacesService eSaveSpacesService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    ESaveBackBatchDetailService eSaveBackBatchDetailService;
    @Autowired
    ESaveStrategyService eSaveStrategyService;
    @Value("${back.count}")
    private String backCount;


    /**
     * 添加
     *
     * @param strategyId
     * @param type
     * @return
     */
    @Override
    public ESaveBackBatch addBatch(String strategyId, String type) {
        ESaveBackBatch backBatch = new ESaveBackBatch();
        backBatch.setBackType(type);
        backBatch.setStrategyId(strategyId);
//        backBatch.setBackCount();
        Person person = SecurityHolder.get().currentPerson();
        backBatch.setPersonId(person.getId());
        backBatch.setBatchName(person.getUserName() + "在" + DateTimeUtils.longToDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "备份数据");
        insert(backBatch);
        return backBatch;
    }

    /**
     * 开始执行
     *
     * @param batchId
     */
    @Override
    public void startWork(String batchId) {
        ESaveBackBatch backBatch = selectById(batchId);
        if (backBatch != null) {
            ESaveStrategy saveStrategy = eSaveStrategyService.selectById(backBatch.getStrategyId());
            ESaveSpaces spaces = eSaveSpacesService.selectById(saveStrategy.getSpacesId());
            String fromPath = spaces.getCatalogue();
            String toPath = spaces.getBackPath();
            Organise organise = SecurityHolder.get().currentOrganise();
            //根据备份策略 查长期保存的档案，在对应的存储路径中，查看是否有zip文件，如果有就复制到备份路径中，同时创建批次详情。
            List<EArchive> list;
            if (organise.getOrganiseType().equals("dag")){
                 list = eArchiveService.selectList(SqlQuery.from(EArchive.class).equal(EArchiveInfo.FONDCODE, saveStrategy.getFond_code())
                        .equal(EArchiveInfo.CATEGORYCODE, saveStrategy.getClassCode()).equal(EArchiveInfo.ORGANISEID,organise.getId()));
            }else {
                list = eArchiveService.selectList(SqlQuery.from(EArchive.class).equal(EArchiveInfo.FONDCODE, saveStrategy.getFond_code())
                        .equal(EArchiveInfo.CATEGORYCODE, saveStrategy.getClassCode()));
            }
            int count = 0;
            for (EArchive eArchive : list) {
                String path = fromPath + File.separator + eArchive.getFondCode() + File.separator + eArchive.getCategoryCode() + File.separator + eArchive.getYear() +
                        File.separator + eArchive.getArchiveCode() + ".zip";
                try {
                    File file = new File(path);
                    if (file.exists()) {
                        count++;
                        eArchive.setLastBackupsDate(System.currentTimeMillis());
                        eArchiveService.updateById(eArchive);
                        //文件存在的话，复制到存储路径中
                        String newPath = toPath + File.separator + backBatch.getBatchCode() + File.separator + eArchive.getFondCode() + File.separator + eArchive.getCategoryCode() + File.separator + eArchive.getYear() +
                                File.separator + eArchive.getArchiveCode() + ".zip";
                        copyFile(file, newPath);
                        //添加备份详情
                        eSaveBackBatchDetailService.addDetail(backBatch.getId(), eArchive.getId(), spaces.getId(), newPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            backBatch.setBackCount(count);
            backBatch.setStatus("1");
            updateById(backBatch);

            //处理过期备份数量
            dealBackData(saveStrategy.getId());
        }


    }

    /**
     * 处理备份过期的批次和批次详情
     */
    public void dealBackData(String strategyId) {
        List<ESaveBackBatch> list = selectList(SqlQuery.from(ESaveBackBatch.class).equal(ESaveBackBatchInfo.STRATEGYID, strategyId)
                .orderByDesc(ESaveBackBatchInfo.CREATEDATE));
        //备份数量大于要求数量时才处理
        if (list.size() > Integer.parseInt(backCount)) {
            List<ESaveBackBatch> list1 = list.subList(Integer.parseInt(backCount), list.size());
            for (ESaveBackBatch backBatch : list1) {
                if ("0".equals(backBatch.getIsExpire())) {
                    backBatch.setIsExpire("1");//设置为已过期
                    backBatch.setUpdateDate(System.currentTimeMillis());
                    updateById(backBatch);
                    //把备份详情也设置为已过期
                    commonMapper.updateIgnoreNullByQuery(SqlQuery.from(ESaveBackBatchDetail.class)
                            .set(ESaveBackBatchDetailInfo.ISEXPIRE, "1").equal(ESaveBackBatchDetailInfo.BATCHID, backBatch.getId()));
                    //删除压缩包
                    deleteFile(strategyId, backBatch.getBatchCode());
                }

            }
        }

    }

    private static void copyFile(File source, String toPath) throws IOException {
        try {
            File file = new File(toPath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            Files.copy(source.toPath(), Paths.get(toPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String strategyId, String batchCode){
        ESaveStrategy saveStrategy = eSaveStrategyService.selectById(strategyId);
        if (saveStrategy != null) {
            ESaveSpaces spaces = eSaveSpacesService.selectById(saveStrategy.getSpacesId());
            if (spaces != null) {
                String newPath = spaces.getBackPath() + File.separator + batchCode;
                try {
                    File file = new File(newPath);
                    if (file.exists()) {
                        FileUtils.deleteDirectory(new File(newPath));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除批次、批次详情、备份数据
     *
     * @param batchId
     * @return
     */
    @Override
    public ResultEntity<Boolean> delDetail(String batchId) {
        ESaveBackBatch batch = selectById(batchId);
        if (batch != null) {
            commonMapper.deleteByQuery(SqlQuery.from(ESaveBackBatchDetail.class)
                    .set(ESaveBackBatchDetailInfo.ISEXPIRE, "1").equal(ESaveBackBatchDetailInfo.BATCHID, batchId));

            ESaveStrategy saveStrategy = eSaveStrategyService.selectById(batch.getStrategyId());
            if (saveStrategy != null) {
                try {
                    deleteFile(saveStrategy.getId(), batch.getBatchCode());
                }catch (Exception e){
                    e.printStackTrace();
                }
                deleteById(batchId);
                return ResultEntity.success(true);
            }

        }
        return ResultEntity.error(false);
    }
}
