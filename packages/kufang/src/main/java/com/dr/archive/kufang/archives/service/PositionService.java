package com.dr.archive.kufang.archives.service;

import com.dr.archive.kufang.archives.entity.Holding;
import com.dr.archive.kufang.archives.entity.Layer;
import com.dr.archive.kufang.archives.entity.Position;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 密集架业务接口类
 *
 * @author dr
 */
public interface PositionService extends BaseService<Position> {

    /**
     * 新增密集架
     */
//    void addPosition(Position position, String personId);

    /**
     * 删除密集架
     */
    void deletePosition(String positionId, String personId, boolean isdelete);

    /**
     * 更新密集架
     */
//    void updatePosition(Position position, String personId);

    /***
     * 获得密集架位置
     */
    Position getPosition(String bookCaseNo);

    Position getPositionById(String id);

    /**
     * 更换密集架状态
     *
     * @param positionId 书架id
     * @param personId   经手人
     */
    void updatePositionStatus(String positionId, String personId);

    /**
     * 添加档案到密集架
     *
     * @param libId
     * @param locId
     * @param bookCaseNo
     * @param layer
     * @param cn084
     */

    void addPositionForBox(String libId, String locId, String bookCaseNo, String ABsurface,
                           int layerNo, int layer, String cn084, boolean update, String personId);

    String bindLabel(String personId, String layerId, String tidNum, String labelNum, String type);

    Layer getLayerByLayerNo(String layerNo);

    Layer getLayerById(String id);

    Layer getLayerByTid(String tid);

    Layer getLayerByParam(String locId, String areaId, String caseId,String columnNo, String layer,String columnNum);

    Map getThisAB(String positionId, String ab);

    /**
     * 根据层号 查询目前存书的数量信息
     *
     * @return
     */
    long getCountByLayer(String layerId);

    List<Holding> inventoryByLayerNo(String layerNo);

    void changeLayer(String layerId, String barCodes, String personId, String personName);

    void putaway(String layerNo, String barCodes, String personId, String personName);
    /**
     * 删除密集架
     */
    void delPosition(String areaId);

}

