package com.dr.archive.service;

import com.dr.archive.entity.Atlas;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;

import java.util.Map;

/**
 * @author: yang
 * @create: 2022-05-25 11:26
 **/
public interface AtlasService extends BaseService<Atlas> {
    Page<Map> getImgArchives(String atlasId, int pageIndex, int pageSize);
}
