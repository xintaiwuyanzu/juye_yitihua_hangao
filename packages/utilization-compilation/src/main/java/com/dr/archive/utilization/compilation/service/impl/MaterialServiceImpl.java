package com.dr.archive.utilization.compilation.service.impl;

import com.dr.archive.utilization.compilation.entity.Material;
import com.dr.archive.utilization.compilation.service.MaterialService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: caor
 * @Date: 2022-02-20 17:38
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialServiceImpl extends DefaultBaseService<Material> implements MaterialService {
}
