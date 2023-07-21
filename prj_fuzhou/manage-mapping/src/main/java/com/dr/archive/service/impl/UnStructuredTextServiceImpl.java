package com.dr.archive.service.impl;

import com.dr.archive.entity.UnStructuredText;
import com.dr.archive.service.UnStructuredTextService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UnStructuredTextServiceImpl extends DefaultBaseService<UnStructuredText> implements UnStructuredTextService {

}
