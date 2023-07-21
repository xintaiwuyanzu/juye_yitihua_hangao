package com.dr.archive.kufang.archives.service.impl;

import com.dr.archive.kufang.archives.entity.*;
import com.dr.archive.kufang.archives.service.LayerService;
import com.dr.archive.kufang.archives.service.LocationKuFangQuService;
import com.dr.archive.kufang.archives.service.LocationKuFangService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LayerServiceImpl extends DefaultBaseService<Layer> implements LayerService {
    @Autowired
    LocationKuFangService locationKuFangService;
    @Autowired
    LocationKuFangQuService locationKuFangQuService;
    @Autowired
    PositionService positionService;

    /**
     * 新增格子
     *
     * @param kufangId
     * @param areaId
     * @param denseId
     * @param denseSurface 面
     * @param denseColumn 列
     * @param denseLayer 层
     */
    @Override
    public void add(String kufangId, String areaId, String denseId, String denseSurface, String denseColumn, String denseLayer) {
        Layer layer = new Layer();
        layer.setLocId(kufangId);
        layer.setAreaId(areaId);
        layer.setCaseId(denseId);
        layer.setColumnNum(denseSurface);
        layer.setColumnNo(denseColumn);
        layer.setLayer(denseLayer);
        LocationKuFang locationKuFang = locationKuFangService.selectById(kufangId);
        LocationKuFangQu area = locationKuFangQuService.selectById(areaId);
        Position position = positionService.selectById(denseId);
        layer.setRemarks(locationKuFang.getName() + area.getAreaName() + position.getCaseName()
                + ("1".equals(denseSurface) ? "A面" : "B面") + denseColumn + "列" + denseLayer + "层");
        insert(layer);
    }

    /**
     * 删除格子
     * @param caseId
     */
    @Override
    public void deleteLayer(String caseId) {
        SqlQuery<Layer> sqlQuery = SqlQuery.from(Layer.class).equal(LayerInfo.CASEID, caseId);
        delete(sqlQuery);
    }


}

