package com.dr.archive.fuzhou.portal.service;

import com.dr.framework.common.page.Page;

import java.util.Map;

public interface PeopleLivelihoodService {
    Page<Map> searchPeopleLivelihood(String idNo, String type, Integer index, Integer size);
}
