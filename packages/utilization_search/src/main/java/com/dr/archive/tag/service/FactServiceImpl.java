package com.dr.archive.tag.service;

import com.dr.archive.tag.entity.FactTag;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: yang
 * @create: 2022-07-14 18:35
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class FactServiceImpl extends DefaultBaseService<FactTag> implements FactTagService {
}
