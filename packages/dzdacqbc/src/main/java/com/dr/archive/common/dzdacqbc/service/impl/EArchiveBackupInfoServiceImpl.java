package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.EArchiveBackupInfo;
import com.dr.archive.common.dzdacqbc.service.EArchiveBackupInfoService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: qiuyf
 * @date: 2022/6/10 14:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EArchiveBackupInfoServiceImpl extends DefaultBaseService<EArchiveBackupInfo> implements EArchiveBackupInfoService {
}
