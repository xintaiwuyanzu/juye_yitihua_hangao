package com.dr.archive.fuzhou.portal.service;

import com.dr.archive.model.to.SearchResultTo;

public interface ExternalQueryService {

    SearchResultTo advancedRetrieval(String keyWord, String type, String orgId, String operato,
                                     boolean multiSearch, Integer index, Integer size);
}
