package com.dr.archive.fournaturescheck.service.impl;

import com.dr.archive.fournaturescheck.entity.FourNatureScheme;
import com.dr.archive.fournaturescheck.service.FourNatureSchemeService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author caor
 * @date 2021-07-28 16:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FourNatureSchemeServiceImpl extends DefaultBaseService<FourNatureScheme> implements FourNatureSchemeService {
}
