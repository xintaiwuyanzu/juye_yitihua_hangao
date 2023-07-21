package com.dr.archive.receive.online.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-08-03 19:12
 **/
@Table(name = Constants.TABLE_PREFIX + "BATCH_EXP", comment = "批次导出", module = Constants.MODULE_NAME)
public class ExpBatch extends AbstractArchiveReceiveBatch {
}
