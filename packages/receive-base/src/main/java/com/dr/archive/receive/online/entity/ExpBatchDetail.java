package com.dr.archive.receive.online.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 批次导入详情
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAIL_EXP", comment = "导出", module = Constants.MODULE_NAME)
public class ExpBatchDetail extends AbstractBatchDetailEntity {

}
