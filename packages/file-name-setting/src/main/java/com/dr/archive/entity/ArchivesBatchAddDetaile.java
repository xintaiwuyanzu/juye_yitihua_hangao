package com.dr.archive.entity;

import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "ArchivesBatchAddDetails", comment = "批量添加批次详情", module = Constants.MODULE_NAME)

public class ArchivesBatchAddDetaile extends ArchiveBatchReceiveOfflineDetail {
}
