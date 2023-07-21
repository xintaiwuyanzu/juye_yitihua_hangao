package com.dr.archive.common.statistics.service;

import com.dr.archive.common.statistics.entity.ExportEntity;

public interface ExportService {

    String exportByType(ExportEntity exportEntity);
}
