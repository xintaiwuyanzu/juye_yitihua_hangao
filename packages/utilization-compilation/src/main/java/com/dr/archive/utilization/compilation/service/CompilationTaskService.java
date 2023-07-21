package com.dr.archive.utilization.compilation.service;

import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.utilization.compilation.entity.CompilationTask;
import com.dr.archive.utilization.compilation.vo.ImageVo;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface CompilationTaskService extends BaseService<CompilationTask> {

    /**
     * 自动编研
     *
     * @param materialCarBatchId    素材库列表（档案车批次id）
     * @param compilationTemplateId 编研模板id
     */
    void AISave(String materialCarBatchId, String compilationTemplateId);

    /**
     * 推送至全宗卷
     *
     * @param id
     * @param fondCode
     * @param managementTypeCode
     * @return
     */
    long push(String id, String fondCode, String managementTypeCode);

    /**
     * 获取本年度边沿情况
     *
     * @return
     */
    String getCompilationContent(String templateId, Management management);
    /**
     * 根据档案车批次id,查询所有照片档案
     *
     * @param batchId
     * @return
     */
    List<ImageVo> getImgListByCarBatchId(String batchId);

    String addCarToTask(String selectdetailIds, String taskId);
}
