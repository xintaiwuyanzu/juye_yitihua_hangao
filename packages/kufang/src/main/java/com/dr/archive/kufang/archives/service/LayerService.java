package com.dr.archive.kufang.archives.service;


import com.dr.archive.kufang.archives.entity.Layer;
import com.dr.framework.common.service.BaseService;


public interface LayerService extends BaseService<Layer> {

    void add(String kufangId, String areaId, String denseId, String denseSurface, String denseColumn, String denseLayer);

    void deleteLayer(String caseId);

}
