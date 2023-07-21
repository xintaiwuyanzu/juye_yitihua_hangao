package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ESaveSpaces;
import com.dr.archive.common.dzdacqbc.vo.MonitorDataVo;
import com.dr.framework.common.service.BaseService;

import java.io.File;
import java.util.List;

/**
 * 存储空间service
 *
 * @author dr
 */
public interface ESaveSpacesService extends BaseService<ESaveSpaces> {
    void updateDefault(ESaveSpaces spaces);

    String getCapacity(String catalogue);

    /**
     * 获取所在服务器的监控信息
     *
     * @return
     */
    List<MonitorDataVo> getMonitorData();


    /**
     * 根据id构造存储根目录
     *
     * @return
     */
    File buildRootDir(String id);

}
