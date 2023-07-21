package com.dr.archive.entity;

import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "ArchivesBatchAdd", comment = "批量添加批次", module = Constants.MODULE_NAME)

public class ArchivesBatchAdd extends ArchiveBatchReceiveOffline {

}
