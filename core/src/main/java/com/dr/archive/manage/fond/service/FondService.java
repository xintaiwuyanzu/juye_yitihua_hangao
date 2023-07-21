package com.dr.archive.manage.fond.service;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.service.BaseService;

/**
 * @author
 */
public interface FondService extends BaseService<Fond> {
    String RESOURCE_TYPE_FOND = "fond";

    /**
     * 功能描述: 根据全宗code查询全宗
     *
     * @param fondCode
     * @return 指定的全宗
     * @author : tzl
     * @date : 2020/6/18 11:23
     */
    Fond findFondByCode(String fondCode);

    Fond findFondByPartyId(String partyId);
}
