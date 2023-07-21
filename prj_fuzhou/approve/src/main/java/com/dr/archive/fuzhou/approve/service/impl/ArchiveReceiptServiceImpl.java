package com.dr.archive.fuzhou.approve.service.impl;

import com.dr.archive.fuzhou.approve.entity.Receipt;
import com.dr.archive.fuzhou.approve.service.ArchiveReceiptService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author caor
 * @date 2021-08-31 10:10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveReceiptServiceImpl extends DefaultBaseService<Receipt> implements ArchiveReceiptService {
}
