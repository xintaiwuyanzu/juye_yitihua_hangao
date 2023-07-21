package com.dr.archive.manage.log.service;

import com.dr.archive.manage.log.entity.SysLogEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;

import java.util.Map;

/**
 * @author caor
 * @date 2020/12/22 20:02
 */
public interface SysLogService extends BaseService<SysLogEntity> {
    void insertFormDataLog(FormData formData, String type);

    void deleteEntity(String ids);

    /**
     * 提供日志接口
     * @param sysLogEntity
     */
    void insertLog(SysLogEntity sysLogEntity);

    Map<String, Long> operation();

    Map<String, Long> operationToday();
}
