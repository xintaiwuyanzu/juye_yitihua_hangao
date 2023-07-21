package com.dr.archive.kufang.archives.service;


import com.dr.archive.kufang.archives.entity.LocationKuFangQu;
import com.dr.framework.common.service.BaseService;


public interface LocationKuFangQuService extends BaseService<LocationKuFangQu> {

    void delArea(String locId);

}
