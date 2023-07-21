package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.EFileInfo;
import com.dr.archive.common.dzdacqbc.service.EFileInfoService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: qiuyf
 * @date: 2022/6/10 10:17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EFileInfoServiceImpl extends DefaultBaseService<EFileInfo> implements EFileInfoService {
}
