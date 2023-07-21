package com.dr.archive.examine.service;

import com.dr.archive.examine.entity.PublicInformation;
import com.dr.framework.common.service.BaseService;

public interface PublicInformationService extends BaseService<PublicInformation> {


    default void add(String refId, String sendId, String toUnitId, String infoContent, String infoType, String checkType) {
        add(refId, sendId, "", "", toUnitId, infoContent, infoType, checkType);
    }

    void add(String refId, String sendId, String sendUnitId, String toId, String toUnitId, String infoContent, String infoType, String checkType);


    void reply(String id, String reply, String checkType);
}
