package com.dr.archive.common.dzdacqbc.service;


import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.common.dzdacqbc.entity.EArchiveDetectTaskDetail;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;

import java.io.File;
import java.util.List;

/**
 * 电子档案管理service
 *
 * @author dr
 */
public interface EArchiveService extends BaseService<EArchive> {

    /**
     * 内存中存放的最大大小
     */
    int MAX_CACHE_SIZE = 1000;
    /**
     * 交接单据所在文件夹
     */
    String BATCH_DIR_NAME = ".交接单据";

    /**
     * 统计馆藏量
     *
     * @return
     */
    long countArchive();

    /**
     * 构造档案对应的文件夹
     *
     * @return
     */
    File buildArchiveDir(EArchive eArchive);


    /**
     * 执行检测指定的电子文件
     *
     * @param eArchive
     * @return
     */
    EArchiveDetectTaskDetail detectEArchive(EArchive eArchive);

    /**
     * 根据长期保存分类计算档案数量
     *
     * @param classId
     * @return
     */
    long countByClass(String classId);

    void addEArchive(ArchiveDataQuery query);

    void addEArchiveOne(FormData formData,String modelType, String ajh);

    EArchive dealEArchive(EArchive eArchive, FormData formData);
    /**
     * 档案退回至临时库
     */
    void backArchives(ArchiveDataQuery query);

    List<FormData> getWJS(FormData formData);
}
