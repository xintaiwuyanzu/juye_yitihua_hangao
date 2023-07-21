package com.dr.archive.archivecar.service.impl;

import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.service.ArchiveCarBatchService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: caor
 * @Date: 2022-01-18 14:30
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveCarBatchServiceImpl extends DefaultBaseService<ArchiveCarBatch> implements ArchiveCarBatchService {
}
