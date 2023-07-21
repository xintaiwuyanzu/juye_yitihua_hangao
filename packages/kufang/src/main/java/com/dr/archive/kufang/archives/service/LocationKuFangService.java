package com.dr.archive.kufang.archives.service;


import com.dr.archive.kufang.archives.entity.LocationKuFang;
import com.dr.framework.common.service.BaseService;

import java.util.List;


/**
 * 图书馆管理抽象接口
 *
 * @author dr
 */
public interface LocationKuFangService extends BaseService<LocationKuFang> {

    /**
     * 查询所有馆藏室
     *
     * @return
     */
    List<LocationKuFang> getLocations();

    /**
     * 添加档案室
     * 1、判断类型是否支持
     * 2、判断libid和rootlibid是否存在
     * 3、逻辑判断编号是否重复
     * 4、执行添加
     *
     * @param location
     * @param personId
     */
    void addLocation(LocationKuFang location, String personId);

    /**
     * 更新只是更新基本信息
     *
     * @param location
     * @param personId
     */
    void updateLocation(LocationKuFang location, String personId);

    /**
     * 更新馆藏室状态
     *
     * @param locId
     * @param personId
     * @param enable   是否启用
     */
    void updateLocationStatus(String locId, String personId, boolean enable);

    LocationKuFang getLocById(String locId);

}
