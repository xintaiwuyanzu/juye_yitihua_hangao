package com.dr.archive.archivecar.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-01-18 14:18
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "CAR_BATCH", comment = "档案车批次", module = Constants.MODULE_NAME)
public class ArchiveCarBatch extends AbstractArchiveBatch {
}
