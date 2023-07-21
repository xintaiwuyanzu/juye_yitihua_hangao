package com.dr.archive.service;

import com.dr.archive.entity.BorrowingRegistrationDetails;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;

/**
 * @author lych
 * @date 2023/2/3 上午 9:15
 * @mood happy
 */
public interface RegistrationDetailsService extends BaseService<BorrowingRegistrationDetails> {
    ResultEntity check(String id);
}
