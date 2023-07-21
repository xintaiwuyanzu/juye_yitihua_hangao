package com.dr.archive.service.impl;

import com.dr.archive.entity.InterfaceManage;
import com.dr.archive.service.InterfaceManageService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: yang
 * @create: 2022-06-03 11:35
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class InterfaceManageServiceImpl extends DefaultBaseService<InterfaceManage> implements InterfaceManageService {
}
