package com.dr.archive.receive.online.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 批次详情信息，用来列表显示和关联查询数据
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_DETAIL_SEND_CHECK", comment = "移交发送", module = Constants.MODULE_NAME)
public class SendCheckBatchDetail extends AbstractBatchDetailEntity {

}
