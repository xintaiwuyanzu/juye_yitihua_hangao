package com.dr.archive.kufang.entityfiles.service;

import com.dr.archive.kufang.entityfiles.entity.RoomDetails;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;

public interface RoomDetailsService extends BaseService<RoomDetails> {

    ResultEntity updateDetail(String kufangId);
}
