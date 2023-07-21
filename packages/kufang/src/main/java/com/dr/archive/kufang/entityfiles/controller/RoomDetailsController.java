package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.RoomDetails;
import com.dr.archive.kufang.entityfiles.entity.RoomDetailsInfo;
import com.dr.archive.kufang.entityfiles.service.RoomDetailsService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/roomDetails")
public class RoomDetailsController extends BaseServiceController<RoomDetailsService, RoomDetails> {
    @Autowired
    RoomDetailsService roomDetailsService;

    @RequestMapping("updateDetails")
    public ResultEntity getRoomDetails(String kufangId) {
        return roomDetailsService.updateDetail(kufangId);

    }

    @Override
    protected SqlQuery<RoomDetails> buildPageQuery(HttpServletRequest request, RoomDetails entity) {
        return SqlQuery.from(RoomDetails.class).equal(RoomDetailsInfo.KUFANGID, entity.getKufangId());
    }
}
