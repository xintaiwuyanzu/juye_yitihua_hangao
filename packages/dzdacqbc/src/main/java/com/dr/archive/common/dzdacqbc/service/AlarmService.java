package com.dr.archive.common.dzdacqbc.service;


import com.dr.archive.common.dzdacqbc.entity.CqbcAlarm;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;

/**
 * 预警管理
 *
 * @author dr
 */
public interface AlarmService extends BaseService<CqbcAlarm> {
    /**
     * 预警类型
     * 存储空间
     */
    String ALARM_TYPE_DISK = "disk";
    /**
     * 预警类型
     * <p>
     * 电子文件
     */
    String ALARM_TYPE_ARCHIVE = "archive";
    /**
     * 预警类型
     * <p>
     * 篡改
     */
    String ALARM_TYPE_TAMPER = "tamper";
    /**
     * 预警类型
     * <p>
     * 调整
     */
    String ALARM_TYPE_ADJUST = "adjust";

    /**
     * 篡改检测恢复
     */
    ResultEntity earchiveRestore(String archiveId, String alarmId);

}
