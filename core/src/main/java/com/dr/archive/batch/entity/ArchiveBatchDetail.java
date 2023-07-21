package com.dr.archive.batch.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 归档批次详情
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAIL_ARCHIVE", comment = "归档", module = Constants.MODULE_NAME)
public class ArchiveBatchDetail extends AbstractBatchDetailEntity {

}
