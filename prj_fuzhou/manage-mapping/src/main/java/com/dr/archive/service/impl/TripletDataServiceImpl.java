package com.dr.archive.service.impl;

import com.dr.archive.entity.TripletData;
import com.dr.archive.service.TripletDataService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: yang
 * @create: 2022-06-09 11:51
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class TripletDataServiceImpl extends DefaultBaseService<TripletData> implements TripletDataService {
}
